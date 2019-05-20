package org.powertac.samplebroker.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WeatherPrediction {

    @Id
    private PredictionKey key;

    private Double windSpeed;

    private Double temperature;

    public WeatherPrediction(Integer currentTimeslot, Integer predictionTimeslot, Double windSpeed, Double temperature) {
        this.key = new PredictionKey(currentTimeslot, predictionTimeslot);
        this.setWindSpeed(windSpeed);
        this.setTemperature(temperature);
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getGeneratedTimeslot() {
        return this.key.getGeneratedTimeslot();
    }

    public Integer getPredicitonTimeslot() {
        return this.key.getFutureTimeslot();
    }
}

