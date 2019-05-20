package org.powertac.samplebroker.domain;

import java.io.Serializable;

public class PredictionKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer generatedTimeslot;

    private Integer futureTimeslot;

    public PredictionKey(Integer currentTimeslot, Integer predictTimeslot) {
        this.setGeneratedTimeslot(currentTimeslot);
        this.setFutureTimeslot(predictTimeslot);
    }

    public Integer getFutureTimeslot() {
        return futureTimeslot;
    }

    public void setFutureTimeslot(Integer predictionTimeslot) {
        this.futureTimeslot = predictionTimeslot;
    }

    public Integer getGeneratedTimeslot() {
        return generatedTimeslot;
    }

    public void setGeneratedTimeslot(Integer currentTimeslot) {
        this.generatedTimeslot = currentTimeslot;
    }

    
}