package org.powertac.samplebroker;

import org.powertac.common.WeatherForecastPrediction;
import org.powertac.common.WeatherReport;
import org.powertac.common.repo.WeatherForecastRepo;
import org.powertac.common.repo.WeatherReportRepo;
import org.powertac.samplebroker.repos.ClearedPriceRepo;
import org.powertac.samplebroker.repos.ClearedQuantityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PrintService {

    private static PrintService printer;

    BufferedWriter out = null;
    ArrayList<Integer> timeslots = new ArrayList<>();
    ArrayList<Double> consumptions = new ArrayList<>();;
    ArrayList<Double> productions = new ArrayList<>();;
    Map<Integer, WeatherForecastPrediction> weatherForecast = new HashMap<>();
    Map<Integer, WeatherReport> weatherReports = new HashMap<>();
    ArrayList<Double> imbalances = new ArrayList<>();
    Double tempAsks = 0.0;
    Double tempBids = 0.0;
    ArrayList<Double> asksQuantity = new ArrayList<>();
    ArrayList<Double> bidsQuantity = new ArrayList<>();
    Map<Integer, Double> clearedQuantity = new HashMap<>();
    Integer numberOfBrokers;
    Integer numberOfConsumers;
    Boolean initialized = false;

    @Autowired
    private ClearedQuantityRepo clearedQuantityRepo;
  
    @Autowired
    private ClearedPriceRepo clearedPriceRepo;

    @Autowired
    private WeatherForecastRepo weatherForecastRepo;
  
    @Autowired
    private WeatherReportRepo weatherReportRepo;

    public static PrintService getInstance() {
        if (printer == null)
            printer = new PrintService();
        return printer;
    }

    public Boolean isInitialized() {
        return initialized;
    }

    public void startCSV() {
        try {
            FileWriter writer = new FileWriter(new Date(System.currentTimeMillis()).toString() + "_data.csv", true);
            out = new BufferedWriter(writer);
            // let t be the current timeslot
            // CAT - Cleared Amount Total - Sum of CA24...CA1
            // CA24 - Cleared Amount for timeslot t - 24
            // CAN - Cleared Amount in the Next slot, aka t+1
            // T24 - Temperature in timeslot t - 24
            // WS24 - Wind speed in timeslot t - 24
            // TF1 - Temperature forecast for t + 1
            // WSF1 - Wind speed forecast for t + 1
            out.write(
                    "Timeslot,CAT,CPA,CA24,CP24,CA23,CP23,CA22,CP22,CA21,CP21,CA20,CP20,CA19,CP19,CA18,CP18,CA17,CP17,CA16,CP16,CA15,CP15,CA14,CP14,CA13,CP13,CA12,CP12,CA11,CP11,CA10,CP10,CA9,CP9,CA8,CP8,CA7,CP7,CA6,CP6,CA5,CP5,CA4,CP4,CA3,CP3,CA2,CP2,CA1,CP1,T24,WS24,T23,WS23,T22,WS22,T21,WS21,T20,WS20,T19,WS19,T18,WS18,T17,WS17,T16,WS16,T15,WS15,T14,WS14,T13,WS13,T12,WS12,T11,WS11,T10,WS10,T9,WS9,T8,WS8,T7,WS7,T6,WS6,T5,WS5,T4,WS4,T3,WS3,T2,WS2,T1,WS1,TF1,WSF1,CAN\n");
            initialized = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDistributionReport(int timeslot, double production, double consumption) {
        timeslots.add(timeslot);
        productions.add(production);
        consumptions.add(consumption);
        asksQuantity.add(tempAsks);
        bidsQuantity.add(tempBids);
        tempAsks = 0.0;
        tempBids = 0.0;
        System.out.println("addDistributionReport");

    }

    public void addBrokersAndConsumers(int numberOfBrokers, int numberOfConsumers) {
        this.numberOfBrokers = numberOfBrokers;
        this.numberOfConsumers = numberOfConsumers;
        System.out.println("addBrokersAndConsumers");
    }

    public void addWeatherReport(WeatherReport report) {
        weatherReports.put(report.getTimeslotIndex(), report);
    }

    public void addWeatherForecast(int timeslot, WeatherForecastPrediction prediction) {
        
        weatherForecast.put(timeslot, prediction);
        System.out.println("addWeatherForecast for timeslot" + timeslot);
    }

    public void addImbalance(Double imbalance) {
        imbalances.add(imbalance);
        System.out.println("addImbalance");
    }

    public void addAsksAndBids(Double asks, Double bids) {
        tempAsks += asks;
        tempBids += bids;
    }

    public void addClearedQuantity(int timeslot, Double cleared) {
        if (clearedQuantity.containsKey(timeslot)) {
            clearedQuantity.put(timeslot, clearedQuantity.get(timeslot) + cleared);
        } else {
            clearedQuantity.put(timeslot, cleared);
            System.out.println("addClearedQuantity for timeslot " + timeslot);
        }
    }

    public void printData() {

        clearedQuantity.keySet().forEach(t -> System.out.println("timeslot: " + t));
        try {
            //System.out.println("Timeslots size: " + timeslots.size());
            for (int i = 385; i < 360 + timeslots.size()-1; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append(i % 24 + ",");
                double sumClearedInLast24h = 0;
                for (int j = 24; j > 0; j--) {
                    Optional<ClearedQuantity> optClearedQty = clearedQuantityRepo.findById(i-j);
                    if (optClearedQty.isPresent()) {
                        sumClearedInLast24h += optClearedQty.get();
                }
                sb.append(sumClearedInLast24h + ",");
                for (int j = 24; j > 0; j--) {
                    sb.append(clearedQuantityRepo.findById(i-j).get().toString() + ",");
                }
                for (int j = 24; j > 0; j--) {
                    sb.append(weatherReportRepo.findById(i - j).get().getTemperature() + ",");
                    sb.append(weatherReportRepo.findById(i-j).get().getWindSpeed() + ",");
                }
                sb.append(weatherForecastRepo.findById(i+1).get().getTemperature() + ",");
                sb.append(weatherForecast.findById(i + 1).get().getWindSpeed() + ",");
                sb.append(clearedQuantityRepo.findById(i + 1).get().toString() + ",\n");
                out.write(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
