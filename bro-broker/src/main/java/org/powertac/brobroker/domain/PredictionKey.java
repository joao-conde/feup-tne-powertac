package org.powertac.brobroker.domain;

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
        return 31 * generatedTimeslot + futureTimeslot;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        PredictionKey other = (PredictionKey) obj;
        if (generatedTimeslot.intValue() != other.generatedTimeslot.intValue())
            return false;
        if (futureTimeslot.intValue() != other.futureTimeslot.intValue())
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