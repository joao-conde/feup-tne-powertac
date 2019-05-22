package org.powertac.brobroker.repos;

import java.util.HashMap;

import org.powertac.brobroker.domain.Weather;

public class WeatherReportRepo implements IRepo<Integer, Weather> {

    private static HashMap<Integer, Weather> data = new HashMap<>();

    @Override
    public void save(Integer key, Weather value) {
        data.put(key, value);
    }

    @Override
    public Weather findById(Integer key) {
        return data.get(key);
    }

}
