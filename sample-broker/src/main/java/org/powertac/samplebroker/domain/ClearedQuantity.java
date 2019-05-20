package org.powertac.samplebroker.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ClearedQuantity {

    @Id
    private Integer timeslot;

    private Double quantity;

    public ClearedQuantity(Integer timeslot, Double quantity) {
        this.timeslot = timeslot;
        this.quantity = quantity;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Integer getTimeslot() {
        return timeslot;
    }

    public void addQuantity(Double q) {
        this.quantity += q;
    }
}