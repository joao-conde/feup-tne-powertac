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

    @Override
    public int hashCode() {
        return generatedTimeslot * futureTimeslot - futureTimeslot;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PredictionKey other = (PredictionKey) obj;
        if (generatedTimeslot != other.generatedTimeslot)
            return false;
        if (futureTimeslot != other.futureTimeslot)
            return false;
        return true;
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