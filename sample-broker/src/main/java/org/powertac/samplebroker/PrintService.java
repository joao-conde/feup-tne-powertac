package org.powertac.samplebroker;

import org.powertac.common.WeatherForecastPrediction;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class PrintService {

    private static PrintService printer;

    BufferedWriter out = null;
    ArrayList<Integer> timeslots = new ArrayList<>();
    ArrayList<Double> consumptions = new ArrayList<>();;
    ArrayList<Double> productions = new ArrayList<>();;
    ArrayList<WeatherForecastPrediction> weather =  new ArrayList<>();
    ArrayList<Double> imbalances = new ArrayList<>();
    Double tempAsks = 0.0;
    Double tempBids = 0.0;
    Map<Integer, Double> asksQuantity = new HashMap<>();
    Map<Integer, Double> bidsQuantity = new HashMap<>();
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

    public void startCSV(){
        try {
            FileWriter writer = new FileWriter("data.csv", true);
            out = new BufferedWriter(writer);
            out.write("Timeslot,NoBrokers,NoCustomers,Consumption,Production,Consumption24hAgo,Production24Ago,TempForecast,WindSpeedForecast,WindDirectionForecast,CloudsForecast,ClearedQuantity24h,Imbalance,Consumption-Production In 24h\n");
            initialized = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void addDistributionReport(int timeslot, double production, double consumption) {
        timeslots.add(timeslot);
        productions.add(production);
        consumptions.add(consumption);
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
        /*tempAsks += asks;
        tempBids += bids;*/
    }

    public void addClearedQuantity(int timeslot, Double cleared) {
        if(clearedQuantity.containsKey(timeslot)) {
            clearedQuantity.put(timeslot, clearedQuantity.get(timeslot)+cleared);
        } else {
            clearedQuantity.put(timeslot, cleared);
            System.out.println("addClearedQuantity for timeslot "+ timeslot);
        }
    }

    public void addData() {
    
        try {
            for(int i=0; i<timeslots.size(); i++) {
                if(!clearedQuantity.containsKey(360+i)) {
                    clearedQuantity.put((360+i),-1.0);
                    System.out.println(i);
                }
                if(i >= 24) {
                    out.write(timeslots.get(i) + "," + numberOfBrokers + "," + numberOfConsumers + "," + consumptions.get(i)
                        + "," + productions.get(i) +","+ consumptions.get(i-24)+","+ productions.get(i-24)+","+weather.get(i).getTemperature()+","+weather.get(i).getWindSpeed()+","+weather.get(i).getWindDirection()+","+weather.get(i).getCloudCover()+","+clearedQuantity.get(360+i)+","+imbalances.get(i)+","+(consumptions.get(i)-productions.get(i))+"\n");
                } else {
                    out.write(timeslots.get(i) + "," + numberOfBrokers + "," + numberOfConsumers + "," + consumptions.get(i)
                        + "," + productions.get(i) + ",-1,-1,"+ weather.get(i).getTemperature()+","+weather.get(i).getWindSpeed()+","+weather.get(i).getWindDirection()+","+weather.get(i).getCloudCover()+","+clearedQuantity.get(360+i)+","+imbalances.get(i)+","+(consumptions.get(i)-productions.get(i))+"\n");
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
