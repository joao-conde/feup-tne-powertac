package org.powertac.samplebroker;

import org.powertac.common.WeatherForecastPrediction;
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
    ArrayList<WeatherForecastPrediction> weather = new ArrayList<>();
    ArrayList<Double> imbalances = new ArrayList<>();
    Double tempAsks = 0.0;
    Double tempBids = 0.0;
    ArrayList<Double> asksQuantity = new ArrayList<>();
    ArrayList<Double> bidsQuantity = new ArrayList<>();
    Map<Integer, Double> clearedQuantity = new HashMap<>();
    Integer numberOfBrokers;
    Integer numberOfConsumers;
    Boolean initialized = false;

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
            // CAT - Cleared Amount Total - Sum of CA24...CA1
            // CA24 - Cleared Amount for timeslot t - 24
            // CAN - Cleared Amount in the Next slot, aka t+1
            out.write(
                    "Timeslot,CAT,CA24,CA23,CA22,CA21,CA20,CA19,CA18,CA17,CA16,CA15,CA14,CA13,CA12,CA11,CA10,CA9,CA8,CA7,CA6,CA5,CA4,CA3,CA2,CA1,CAN\n");
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

    public void addWeatherForecast(WeatherForecastPrediction prediction) {
        weather.add(prediction);
        System.out.println("addWeatherForecast");
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
                    sumClearedInLast24h += clearedQuantity.get(i-j);
                }
                sb.append(sumClearedInLast24h + ",");
                for (int j = 24; j > 0; j--) {
                    sb.append(clearedQuantity.get(i - j).toString() + ",");
                }
                sb.append(clearedQuantity.get(i + 1).toString() + ",\n");
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
