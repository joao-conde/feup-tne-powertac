package org.powertac.samplebroker.repos;


import java.util.HashMap;

import org.powertac.samplebroker.domain.PredictionKey;
import org.powertac.samplebroker.domain.WeatherPrediction;

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
