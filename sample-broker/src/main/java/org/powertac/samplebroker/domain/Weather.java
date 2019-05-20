package org.powertac.samplebroker.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Weather {

    @Id
    private Integer timeslot;

    private Double windSpeed;

    private Double temperature;

    public Weather(Integer timeslot, Double windSpeed, Double temperature) {
        this.timeslot = timeslot;
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

    public Integer getTimeslot() {
        return timeslot;
    }
}