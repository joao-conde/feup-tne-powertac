package org.powertac.samplebroker;

import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

@Service
public class PrintService {

    PrintWriter writer;



    public void startCSV() {
        try {
            writer = new PrintWriter("data.csv");
            writer.println("Timeslot,NoBrokers,NoCustomers,Consumption,Production,Consumption24hAgo,Production24Ago,BidAmount24h,AskAmount24h,TempForecast,WindForecast,CloudsForecast,ClearedAmount24h,Imbalance,Consumption-Production In 24h\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addData(ArrayList<Double> data) {
        data.forEach(d -> writer.print(d + ","));
        writer.println();
    }
    public void print() {
        writer.close();
    }
}
