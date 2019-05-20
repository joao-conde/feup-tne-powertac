package org.powertac.samplebroker.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.powertac.samplebroker.domain.PartialCleared;
import org.springframework.stereotype.Service;

@Service
public class ClearedService {
    private Map<Integer, PartialCleared> totalCleared = new HashMap<>();

    public void updateFutureTimeslot(Integer timeslot, Double quantity, Double price) {
        PartialCleared value = totalCleared.get(timeslot);
        if(value != null) {
            totalCleared.get(timeslot).addPrice(price).addQuantity(quantity);
        } else {
            totalCleared.put(timeslot, new PartialCleared(quantity, price));
        }
    }

    public PartialCleared getTotalClearedForTimeslot(Integer timeslot) {
        return totalCleared.get(timeslot);
    }

    public ArrayList<PartialCleared> getPartialClearedForNext24Timeslots(Integer currentTimeslot) {
        ArrayList<PartialCleared> result = new ArrayList<>();
        for (int i = currentTimeslot + 1; i <= currentTimeslot + 24; i++) {
            result.add(totalCleared.get(i));
        }
        return result;
    }
}