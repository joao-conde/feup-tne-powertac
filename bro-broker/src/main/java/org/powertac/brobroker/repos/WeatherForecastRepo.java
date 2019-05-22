package org.powertac.brobroker.repos;

import org.powertac.brobroker.domain.WeatherPrediction;

import java.util.HashMap;

import org.powertac.brobroker.domain.PredictionKey;

public class WeatherForecastRepo implements IRepo<PredictionKey, WeatherPrediction> {

    private static HashMap<PredictionKey, WeatherPrediction> data = new HashMap<>();

    @Override
    public void save(PredictionKey key, WeatherPrediction value) {
        data.put(key, value);
    }

    @Override
    public WeatherPrediction findById(PredictionKey key) {
        return data.get(key);
    }

}
