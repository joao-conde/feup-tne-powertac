package org.powertac.samplebroker.repos;

import org.powertac.samplebroker.domain.WeatherPrediction;
import org.powertac.samplebroker.domain.PredictionKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherForecastRepo extends CrudRepository<WeatherPrediction, PredictionKey> {

}
