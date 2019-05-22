package org.powertac.brobroker.domain;

import java.util.ArrayList;

/**
 * Amount and average prices of cleared trades done in each of the next 24 timeslots
 */
public class Cleared {

    // size 24
    private ArrayList<PartialCleared> futureCleared;

    public Cleared(ArrayList<PartialCleared> futureCleared) {
        this.futureCleared = futureCleared;
    }

    public ArrayList<PartialCleared> getFutureCleared() {
        return futureCleared;
    }

    public void setFutureCleared(ArrayList<PartialCleared> futureCleared) {
        this.futureCleared = futureCleared;
    }
}