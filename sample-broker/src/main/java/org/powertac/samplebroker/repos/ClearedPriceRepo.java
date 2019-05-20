package org.powertac.samplebroker.repos;

import org.powertac.samplebroker.domain.ClearedPrice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClearedPriceRepo extends CrudRepository<ClearedPrice, Integer> {

}
