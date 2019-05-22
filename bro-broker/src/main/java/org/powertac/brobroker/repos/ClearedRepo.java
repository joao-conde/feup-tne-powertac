package org.powertac.brobroker.repos;

import java.util.HashMap;

import org.powertac.brobroker.domain.Cleared;

public class ClearedRepo implements IRepo<Integer, Cleared> {
    private static HashMap<Integer, Cleared> data = new HashMap<>();

    @Override
    public void save(Integer key, Cleared value) {
        data.put(key, value);
    }

    @Override
    public Cleared findById(Integer key) {
        return data.get(key);
    }

}