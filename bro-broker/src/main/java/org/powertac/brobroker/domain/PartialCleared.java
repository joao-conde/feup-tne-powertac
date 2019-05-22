package org.powertac.brobroker.domain;

import java.util.ArrayList;

public class PartialCleared {

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