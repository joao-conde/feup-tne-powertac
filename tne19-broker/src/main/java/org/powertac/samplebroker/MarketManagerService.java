/*
 * Copyright (c) 2012-2014 by the original author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.powertac.samplebroker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.SortedSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.powertac.common.BalancingTransaction;
import org.powertac.common.CapacityTransaction;
import org.powertac.common.ClearedTrade;
import org.powertac.common.Competition;
import org.powertac.common.DistributionTransaction;
import org.powertac.common.MarketPosition;
import org.powertac.common.MarketTransaction;
import org.powertac.common.Order;
import org.powertac.common.Orderbook;
import org.powertac.common.OrderbookOrder;
import org.powertac.common.WeatherForecast;
import org.powertac.common.WeatherForecastPrediction;
import org.powertac.common.WeatherReport;
import org.powertac.common.config.ConfigurableValue;
import org.powertac.common.msg.BalanceReport;
import org.powertac.common.msg.MarketBootstrapData;
import org.powertac.common.repo.TimeslotRepo;
import org.powertac.samplebroker.core.BrokerPropertiesService;
import org.powertac.samplebroker.domain.Cleared;
import org.powertac.samplebroker.domain.PartialCleared;
import org.powertac.samplebroker.domain.PredictionKey;
import org.powertac.samplebroker.domain.Weather;
import org.powertac.samplebroker.domain.WeatherPrediction;
import org.powertac.samplebroker.interfaces.Activatable;
import org.powertac.samplebroker.interfaces.BrokerContext;
import org.powertac.samplebroker.interfaces.Initializable;
import org.powertac.samplebroker.interfaces.MarketManager;
import org.powertac.samplebroker.interfaces.PortfolioManager;
import org.powertac.samplebroker.repos.ClearedFuturesRepo;
import org.powertac.samplebroker.repos.ClearedRepo;
import org.powertac.samplebroker.repos.WeatherForecastRepo;
import org.powertac.samplebroker.repos.WeatherReportRepo;
import org.powertac.samplebroker.services.API;
import org.powertac.samplebroker.services.PrintService;
import org.powertac.samplebroker.utils.MaxDifference;
import org.powertac.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Handles market interactions on behalf of the broker.
 * 
 * @author John Collins
 */
@Service
public class MarketManagerService implements MarketManager, Initializable, Activatable {
  static private Logger log = LogManager.getLogger(MarketManagerService.class);

  private BrokerContext broker; // broker

  // Spring fills in Autowired dependencies through a naming convention
  @Autowired
  private BrokerPropertiesService propertiesService;

  @Autowired
  private TimeslotRepo timeslotRepo;

  @Autowired
  private PortfolioManager portfolioManager;

  @Autowired
  private API api;

  private WeatherForecastRepo weatherForecastRepo = new WeatherForecastRepo();

  private WeatherReportRepo weatherReportRepo = new WeatherReportRepo();

  private ClearedRepo clearedRepo = new ClearedRepo();

  private ClearedFuturesRepo clearedFuturesRepo = new ClearedFuturesRepo();

  // ------------ Configurable parameters --------------
  // max and min offer prices. Max means "sure to trade"
  @ConfigurableValue(valueType = "Double", description = "Upper end (least negative) of bid price range")
  private double buyLimitPriceMax = -1.0; // broker pays

  @ConfigurableValue(valueType = "Double", description = "Lower end (most negative) of bid price range")
  private double buyLimitPriceMin = -70.0; // broker pays

  @ConfigurableValue(valueType = "Double", description = "Upper end (most positive) of ask price range")
  private double sellLimitPriceMax = 70.0; // other broker pays

  @ConfigurableValue(valueType = "Double", description = "Lower end (least positive) of ask price range")
  private double sellLimitPriceMin = 0.5; // other broker pays

  @ConfigurableValue(valueType = "Double", description = "Minimum bid/ask quantity in MWh")
  private double minMWh = 0.001; // don't worry about 1 KWh or less

  @ConfigurableValue(valueType = "Integer", description = "If set, seed the random generator")
  private Integer seedNumber = null;

  // ---------------- local state ------------------
  private Random randomGen; // to randomize bid/ask prices

  // Bid recording
  private HashMap<Integer, ArrayList<Order>> lastOrders;
  private double[] marketMWh;
  private double[] marketPrice;
  private double meanMarketPrice = 0.0;
  private ArrayList<Double> balacingQuantity = new ArrayList<>();
  private ArrayList<Double> balacingPrice = new ArrayList<>();
  private int currentTimeslot;
  private int sellingIndex;
  private int buyingIndex = 0;
  private double buyingOrderQuantity = 0.0;
  private Double sellingPrice = 0.0;

  private double oldLimitPrice = 50;
  private double buyPriceMultiplier = 2.0;

  public MarketManagerService() {
    super();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.powertac.brobroker.MarketManager#init(org.powertac.brobroker.
   * broBroker)
   */
  @Override
  public void initialize(BrokerContext broker) {
    this.broker = broker;
    lastOrders = new HashMap<>();
    propertiesService.configureMe(this);
    System.out.println("  name=" + broker.getBrokerUsername());
    if (seedNumber != null) {
      System.out.println("  seeding=" + seedNumber);
      log.info("Seeding with : " + seedNumber);
      randomGen = new Random(seedNumber);
    } else {
      randomGen = new Random();
    }
  }

  // ----------------- data access -------------------
  /**
   * Returns the mean price observed in the market
   */
  @Override
  public double getMeanMarketPrice() {

    return meanMarketPrice;
  }

  // --------------- message handling -----------------
  /**
   * Handles the Competition instance that arrives at beginning of game. Here we
   * capture minimum order size to avoid running into the limit and generating
   * unhelpful error messages.
   */
  public synchronized void handleMessage(Competition comp) {
    PrintService.getInstance().addBrokersAndConsumers(comp.getBrokers().size(), comp.getCustomers().size());
    System.out.println("Competition");
    minMWh = Math.max(minMWh, comp.getMinimumOrderQuantity());
  }

  /**
   * Handles a BalancingTransaction message.
   */
  public synchronized void handleMessage(BalancingTransaction tx) {
    // System.out.println("Balancing Transaction: "+tx.getKWh()+ " charge:
    // "+tx.getCharge());
    balacingQuantity.add(tx.getKWh());
    balacingPrice.add(tx.getCharge());
    System.out.println("Balancing charge: " + tx.getCharge());
  }

  /**
   * Handles a ClearedTrade message - this is where you would want to keep track
   * of market prices.
   */
  public synchronized void handleMessage(ClearedTrade ct) {
    clearedFuturesRepo.updateFutureTimeslot(ct.getTimeslotIndex(), ct.getExecutionMWh(), ct.getExecutionPrice());
    ArrayList<PartialCleared> next24Cleared = clearedFuturesRepo.getPartialClearedForNext24Timeslots(currentTimeslot);
    Cleared cleared = new Cleared(next24Cleared);
    clearedRepo.save(currentTimeslot, cleared);
    //System.out.println("Cleared for "+ct.getTimeslotIndex()+" by " +ct.getExecutionMWh());
    log.info("Cleared Trade: Mwh - " + ct.getExecutionMWh() + "; Price: " + ct.getExecutionPrice() + " timeslot: "
        + ct.getTimeslotIndex());
  }

  /**
   * Handles a DistributionTransaction - charges for transporting power
   */
  public synchronized void handleMessage(DistributionTransaction dt) {
    //System.out.println("Distribution charge: " + dt.getCharge());
  }

  /**
   * Handles a CapacityTransaction - a charge for contribution to overall peak
   * demand over the recent past.
   */
  public synchronized void handleMessage(CapacityTransaction dt) {
    //System.out.println("Capacity charge: " + dt.getCharge());
  }

  /**
   * Receives a MarketBootstrapData message, reporting usage and prices for the
   * bootstrap period. We record the overall weighted mean price, as well as the
   * mean price and usage for a week.
   */
  public synchronized void handleMessage(MarketBootstrapData data) {
    marketMWh = new double[broker.getUsageRecordLength()];
    marketPrice = new double[broker.getUsageRecordLength()];
    double totalUsage = 0.0;
    double totalValue = 0.0;
    for (int i = 0; i < data.getMwh().length; i++) {
      totalUsage += data.getMwh()[i];
      totalValue += data.getMarketPrice()[i] * data.getMwh()[i];
      if (i < broker.getUsageRecordLength()) {
        // first pass, just copy the data
        marketMWh[i] = data.getMwh()[i];
        marketPrice[i] = data.getMarketPrice()[i];
      } else {
        // subsequent passes, accumulate mean values
        int pass = i / broker.getUsageRecordLength();
        int index = i % broker.getUsageRecordLength();
        marketMWh[index] = (marketMWh[index] * pass + data.getMwh()[i]) / (pass + 1);
        marketPrice[index] = (marketPrice[index] * pass + data.getMarketPrice()[i]) / (pass + 1);
      }
    }
    meanMarketPrice = totalValue / totalUsage;
  }

  /**
   * Receives a MarketPosition message, representing our commitments on the
   * wholesale market
   */
  public synchronized void handleMessage(MarketPosition posn) {
    log.info("Market position: " + posn.toString());
    broker.getBroker().addMarketPosition(posn, posn.getTimeslotIndex());
  }

  /**
   * Receives a new MarketTransaction. We look to see whether an order we have
   * placed has cleared.
   */
  public synchronized void handleMessage(MarketTransaction tx) {
    log.info("Market transaction:" + tx.toString());
    // reset price escalation when a trade fully clears.
    ArrayList<Order> lastTries = lastOrders.get(tx.getTimeslotIndex());
    for (Order lastTry : lastTries) {
      if (tx.getMWh() == lastTry.getMWh()) {
        System.out.println("Cleared "+lastTry.getMWh() + " for timeslot "+tx.getTimeslotIndex());
        lastTries.remove(lastTry);
      }
    }
  }

  /**
   * Receives market orderbooks. These list un-cleared bids and asks, from which a
   * broker can construct approximate supply and demand curves for the following
   * timeslot.
   */
  public synchronized void handleMessage(Orderbook orderbook) {
    log.info("Order book received");
    SortedSet<OrderbookOrder> asks = orderbook.getAsks();
    SortedSet<OrderbookOrder> bids = orderbook.getBids();
    double totalAmountAsks = 0;
    double totalAmountBids = 0;
    for (OrderbookOrder ask : asks) {
      totalAmountAsks += ask.getMWh();
    }
    for (OrderbookOrder bid : bids) {
      totalAmountBids += bid.getMWh();
    }
    PrintService.getInstance().addAsksAndBids(totalAmountAsks, totalAmountBids);
  }

  /**
   * Receives a new WeatherForecast.
   */
  public synchronized void handleMessage(WeatherForecast forecast) {
    log.info("Weather forecast received");
    forecast.getPredictions().forEach(p -> log.info("; temp: " + p.getTemperature() + "; clouds: " + p.getCloudCover()
        + "; time: " + p.getForecastTime() + "; wind speed: " + p.getWindSpeed()));
    for (int i = 0; i < 24; i++) {
      WeatherForecastPrediction nextDayForecast = forecast.getPredictions().get(i);
      PredictionKey key = new PredictionKey(forecast.getTimeslotIndex(), forecast.getTimeslotIndex() + i + 1);
      weatherForecastRepo.save(key,
          new WeatherPrediction(nextDayForecast.getWindSpeed(), nextDayForecast.getTemperature()));
    }
  }

  /**
   * Receives a new WeatherReport.
   */
  public synchronized void handleMessage(WeatherReport report) {
    log.info("Weather Report received");
    log.info("temp: " + report.getTemperature() + "; clouds: " + report.getCloudCover() + "; wind: "
        + report.getWindSpeed());
    weatherReportRepo.save(report.getTimeslotIndex(), new Weather(report.getWindSpeed(), report.getTemperature()));
  }

  /**
   * Receives a BalanceReport containing information about imbalance in the
   * current timeslot.
   */
  public synchronized void handleMessage(BalanceReport report) {
    PrintService.getInstance().addImbalance(report.getNetImbalance());
  }

  // ----------- per-timeslot activation ---------------

  /**
   * Compute needed quantities for each open timeslot, then submit orders for
   * those quantities.
   *
   * @see org.powertac.brobroker.interfaces.Activatable#activate(int)
   */
  @Override
  public synchronized void activate(int timeslotIndex) {
    double neededMWh = 0.0;
    this.currentTimeslot = timeslotIndex;
    System.out.println("Timeslot " + timeslotRepo.currentTimeslot().getSerialNumber());
    applyWholeSaleStrategy();
  }

  private void applyWholeSaleStrategy() {
    Double energyBalance = broker.getBroker().findMarketPositionByTimeslot(this.currentTimeslot).getOverallBalance();
    //place first 24h orders
    // if(this.currentTimeslot < 386){
    //  MUST FIND A STRATEGY FOR FIRST 24 BECAUSE OF RETAIL TARIFFS
    //   if(energyBalance == 0){
    //     submitOrder(100, -20, this.currentTimeslot + 1);
    //   }
    //   else if(energyBalance > 0){
    //     submitOrder(-energyBalance * 1.5, 25, this.currentTimeslot + 1);
    //   }
    //   else if(energyBalance < 0){
    //     submitOrder(-energyBalance * 1.5, -20, this.currentTimeslot + 1);
    //   }
    // }
    // else 
    if (this.currentTimeslot == 386) {
      ArrayList<Double> prices = api.predictPrices(this.currentTimeslot);
      ArrayList<Double> amounts = api.predictAmounts(this.currentTimeslot);
      Double averagePrice = averagePrice(prices);
      for(Integer i=0; i<prices.size(); i++) {
        if(prices.get(i) <= averagePrice) {
          submitOrder(amounts.get(i), -prices.get(i) * 0.8, 386+i+1);
        }
      }      
    }
    else if(this.currentTimeslot > 386){
      ArrayList<Double> prices = api.predictPrices(this.currentTimeslot);
      ArrayList<Double> amounts = api.predictAmounts(this.currentTimeslot);
      Double averagePrice = averagePrice(prices);
      int lastIdx = prices.size() - 1;
      System.out.println("Energy balance: " + energyBalance);
      if(energyBalance == 0){
        if(prices.get(lastIdx) <= averagePrice) {
          submitOrder(amounts.get(lastIdx), -prices.get(lastIdx) * 0.6, this.currentTimeslot + 24);
        }
      }
      // else if(energyBalance > 0){
      //   submitOrder(-energyBalance * 2, prices.get(0) * 0.8, this.currentTimeslot + 1);
      // }
      else if(energyBalance < 0){
        submitOrder(-energyBalance * 2, -prices.get(0), this.currentTimeslot + 1);
      }
    }
  }

  private Double averagePrice(ArrayList<Double> prices) {
      Double sum = 0.0;
      if(!prices.isEmpty()) {
        for (Double price : prices) {
            sum += price;
        }
        return sum.doubleValue() / prices.size();
      }
      return sum;
  }

  private double computeLimitPrice(int timeslot) {
    // set price between oldLimitPrice and maxPrice, according to number of
    // remaining chances we have to get what we need.
    double newLimitPrice = 50; // default value
    double maxPrice = 20;
    int current = timeslotRepo.currentSerialNumber();
    int remainingTries = (timeslot - current);
    if (remainingTries > 0) {
      double range = (maxPrice - oldLimitPrice) * 2.0 / (double) remainingTries;
      double computedPrice = oldLimitPrice + randomGen.nextDouble() * range;
      oldLimitPrice = computedPrice;
      return Math.max(newLimitPrice, computedPrice);
    }
    return 0;
  }

  private void buyInWholesale(ArrayList<Double> prices, ArrayList<Double> amounts) {
    System.out.println("Buying");
    Pair<Integer, Integer> pricesMaxDiff = MaxDifference.maxDiff(prices);
    Integer maxPriceIndex = pricesMaxDiff.cdr();
    Integer minPriceIndex = pricesMaxDiff.car();
    sellingIndex = currentTimeslot + maxPriceIndex + 1;
    buyingIndex = this.currentTimeslot + minPriceIndex + 1;
    sellingPrice = prices.get(maxPriceIndex);
    Double predictedMaxAmount = amounts.get(maxPriceIndex);
    System.out.println("Min price index: " + minPriceIndex + "; max price index: " + maxPriceIndex);
    
    Double alreadyClearedQuantityForMax = 0.0;
    if (maxPriceIndex < 23) {
      alreadyClearedQuantityForMax = clearedFuturesRepo.findById(currentTimeslot + maxPriceIndex + 1).getQuantity();
    }
    buyingOrderQuantity = predictedMaxAmount - alreadyClearedQuantityForMax;
    submitOrder(buyingOrderQuantity, -prices.get(minPriceIndex)*buyPriceMultiplier, buyingIndex);  
  }

  private void sellInWholesale() {
    System.out.println("Selling");
    submitOrder(-buyingOrderQuantity, sellingPrice, sellingIndex);
  }

  /**
   * Composes and submits the appropriate order for the given timeslot.
   */
  private void submitOrder(double neededMWh, double price, int timeslot) {

    System.out.println("new order for " + neededMWh + " at " + price + " for timeslot "+timeslot);
    Order order = new Order(broker.getBroker(), timeslot, neededMWh, price);
    if (lastOrders.get(timeslot) == null) {
      lastOrders.put(timeslot, new ArrayList<>());
    }
    lastOrders.get(timeslot).add(order);
    broker.sendMessage(order);
  }

}
