package org.powertac.samplebroker.domain;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ClearedPrice {

    @Id
    private Integer timeslot;

    private ArrayList<Double> priceArray = new ArrayList<>();

    public ClearedPrice(Integer timeslot, Double quantity) {
        this.timeslot = timeslot;
        this.priceArray.add(quantity);
    }

    public Double getMeanPrice() {
        Double sum = priceArray.stream().reduce(0.0, Double::sum);
        return sum / priceArray.size();
    }

    public void addPrice(Double p) {
        this.priceArray.add(p);
    }

    public Integer getTimeslot() {
        return timeslot;
    }
}