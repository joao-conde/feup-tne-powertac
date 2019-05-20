package org.powertac.samplebroker.domain;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PartialCleared {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double quantity;

    private ArrayList<Double> pricesArray = new ArrayList<>();

    public PartialCleared(Double quantity, Double price) {
        this.quantity = quantity;
        this.pricesArray.add(price);
    }

    public Double getMeanPrice() {
        Double sum = pricesArray.stream().reduce(0.0, Double::sum);
        return sum / pricesArray.size();
    }

    public PartialCleared addPrice(Double price) {
        this.pricesArray.add(price);
        return this;
    }

    public Double getQuantity() {
        return quantity;
    }

    public PartialCleared addQuantity(Double quantity) {
        this.quantity += quantity;
        return this;
    }

}