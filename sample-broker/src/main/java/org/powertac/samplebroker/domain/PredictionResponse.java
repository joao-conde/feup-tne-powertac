package org.powertac.samplebroker.domain;

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
}