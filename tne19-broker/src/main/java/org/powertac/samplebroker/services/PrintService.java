package org.powertac.samplebroker.services;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.powertac.samplebroker.domain.PartialCleared;
import org.powertac.samplebroker.domain.PredictionKey;
import org.powertac.samplebroker.repos.ClearedFuturesRepo;
import org.powertac.samplebroker.repos.ClearedRepo;
import org.powertac.samplebroker.repos.WeatherForecastRepo;
import org.powertac.samplebroker.repos.WeatherReportRepo;
import org.springframework.stereotype.Service;

@Service
public class PrintService {

    private static PrintService printer;

    BufferedWriter out = null;
    ArrayList<Integer> timeslots = new ArrayList<>();
    ArrayList<Double> consumptions = new ArrayList<>();;
    ArrayList<Double> productions = new ArrayList<>();;
    ArrayList<Double> imbalances = new ArrayList<>();
    Double tempAsks = 0.0;
    Double tempBids = 0.0;
    ArrayList<Double> asksQuantity = new ArrayList<>();
    ArrayList<Double> bidsQuantity = new ArrayList<>();
    Integer numberOfBrokers;
    Integer numberOfConsumers;
    Boolean initialized = false;


    private WeatherForecastRepo weatherForecastRepo = new WeatherForecastRepo();

    private WeatherReportRepo weatherReportRepo = new WeatherReportRepo();
  
    private ClearedRepo clearedRepo = new ClearedRepo();

    private ClearedFuturesRepo clearedFuturesRepo = new ClearedFuturesRepo();


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
            // CA24 - Cleared Amount for timeslot t - 24
            // T24 - Temperature in timeslot t - 24
            // WS24 - Wind speed in timeslot t - 24
            // TF1 - Temperature forecast for t + 1
            // WSF1 - Wind speed forecast for t + 1
            // CT - Current Temperature
            // CWS - Current Windspeed
            // PCA - Partial Cleared Amount
            // TCA - Total Cleared Amount
            // PCP - Partial Cleared Price
            // TCP - Total Cleared Price
            StringBuilder sb = new StringBuilder();
            sb.append("Timeslot,WeekDay,");
            for (int i = 24; i > 0; i--) {
                sb.append("CA"+i+",CP"+i+",T"+i+",WS"+i+",");
            }
            sb.append("CT,CWS,");
            for (int i = 1; i <= 24; i++) {
                sb.append("PCA"+i+",PCP"+i+",");
            }
            for (int i = 1; i <= 24; i++) {
                sb.append("TF"+i+",WSF"+i+",");
            }
            for (int i = 1; i <= 24; i++) {
                sb.append("TCA"+i+",TCP"+i+",");
            }
            out.write(sb.toString() + "\n");
            initialized = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDistributionReport(int timeslot, double production, double consumption) {

    }

    public void addBrokersAndConsumers(int numberOfBrokers, int numberOfConsumers) {
        this.numberOfBrokers = numberOfBrokers;
        this.numberOfConsumers = numberOfConsumers;
    }

    public void addImbalance(Double imbalance) {
    }

    public void addAsksAndBids(Double asks, Double bids) {
        tempAsks += asks;
        tempBids += bids;
    }

    public void printData() {
        try {
            for (int i = 385; i < 360 + timeslots.size()-25; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append(i % 24 + ",");
                sb.append(i % 168 + ",");
                for (int j = 24; j > 0; j--) {
                    sb.append(clearedFuturesRepo.findById(i-j).getQuantity().toString() + ",");
                    sb.append(clearedFuturesRepo.findById(i-j).getMeanPrice().toString() + ",");
                    sb.append(weatherReportRepo.findById(i - j).getTemperature() + ",");
                    sb.append(weatherReportRepo.findById(i-j).getWindSpeed() + ",");
                }
                sb.append(weatherReportRepo.findById(i).getTemperature() + ",");
                sb.append(weatherReportRepo.findById(i).getWindSpeed() + ",");
                ArrayList<PartialCleared> partialCleared = clearedRepo.findById(i-1).getFutureCleared();
                for (int k = 0; k < 24; k++) {
                    sb.append(partialCleared.get(k).getQuantity() + ",");
                    sb.append(partialCleared.get(k).getMeanPrice() + ",");
                }

                for (int j = 1; j <= 24; j++) {
                    sb.append(weatherForecastRepo.findById(new PredictionKey(i, i+j)).getTemperature() + ",");
                    sb.append(weatherForecastRepo.findById(new PredictionKey(i, i+j)).getWindSpeed() + ",");
                }
                for (int j = 1; j <= 24; j++) {
                    sb.append(clearedFuturesRepo.findById(i + j).getQuantity().toString() + ",");
                    sb.append(clearedFuturesRepo.findById(i + j).getMeanPrice().toString() + ",");
                }
                sb.append("\n");
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
