package org.powertac.samplebroker.domain;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Amount and average prices of cleared trades done in each of the next 24 timeslots
 */
@Entity
public class Cleared {
    @Id
    private Integer currentTimeslot;

    // size 24
    private ArrayList<PartialCleared> futureCleared;

    public Cleared(Integer currentTimeslot, ArrayList<PartialCleared> futureCleared) {
        this.currentTimeslot = currentTimeslot;
        this.futureCleared = futureCleared;
    }

    public ArrayList<PartialCleared> getFutureCleared() {
        return futureCleared;
    }

    public void setFutureCleared(ArrayList<PartialCleared> futureCleared) {
        this.futureCleared = futureCleared;
    }
}