package org.powertac.samplebroker.domain;

public class Weather {

    private Double windSpeed;

    private Double temperature;

    public Weather(Double windSpeed, Double temperature) {
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
}