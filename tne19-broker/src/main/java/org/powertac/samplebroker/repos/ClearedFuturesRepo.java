package org.powertac.samplebroker.repos;

import java.util.ArrayList;
import java.util.HashMap;

import org.powertac.samplebroker.domain.PartialCleared;

public class ClearedFuturesRepo implements IRepo<Integer, PartialCleared> {
    private static HashMap<Integer, PartialCleared> data = new HashMap<>();

    public synchronized void updateFutureTimeslot(Integer timeslot, Double quantity, Double price) {
        PartialCleared currentValue = data.get(timeslot);
        if(currentValue != null) {
            currentValue.addPrice(price).addQuantity(quantity);
        } else {
            data.put(timeslot, new PartialCleared(quantity, price));
        }
    }

    @Override
    public void save(Integer key, PartialCleared newValue) {
    }

    @Override
    public PartialCleared findById(Integer key) {
        return data.get(key);
    }

    public synchronized ArrayList<PartialCleared> getPartialClearedForNext24Timeslots(Integer currentTimeslot) {
        ArrayList<PartialCleared> result = new ArrayList<>();
        for (int i = currentTimeslot + 1; i <= currentTimeslot + 24; i++) {
            result.add(data.get(i));
        }
        return result;
    }

}