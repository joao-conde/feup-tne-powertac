package org.powertac.samplebroker.repos;

import org.powertac.samplebroker.domain.Weather;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherReportRepo extends CrudRepository<Weather, Integer> {

}
