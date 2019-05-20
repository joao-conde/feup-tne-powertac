package org.powertac.samplebroker.repos;

import org.powertac.samplebroker.domain.WeatherPrediction;

import java.util.HashMap;

import org.powertac.samplebroker.domain.PredictionKey;

public class WeatherForecastRepo implements IRepo<PredictionKey, WeatherPrediction> {

    private static HashMap<PredictionKey, WeatherPrediction> data;

    @Override
    public void save(PredictionKey key, WeatherPrediction value) {
        data.put(key, value);
    }

    @Override
    public WeatherPrediction findById(PredictionKey key) {
        return data.get(key);
    }

}
