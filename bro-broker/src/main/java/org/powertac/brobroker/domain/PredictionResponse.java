package org.powertac.brobroker.domain;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PredictionResponse {

    @JsonProperty
    private ArrayList<Double> prediction;

    public ArrayList<Double> getPrediction() {
        return prediction;
    }

    public void setPrediction(ArrayList<Double> prediction) {
        this.prediction = prediction;
    }

    public ArrayList<Double> getPredictedAmounts() {
        ArrayList<Double> amounts = new ArrayList<>();
        for (int i = 0; i < prediction.size(); i+=2) {
            amounts.add(prediction.get(i));
        }
        return amounts;
    }
    public ArrayList<Double> getPredictedPrices() {
        ArrayList<Double> prices = new ArrayList<>();
        for (int i = 1; i < prices.size(); i+=2) {
            prices.add(prediction.get(i));
        }
        return prices;
    }
}