package org.powertac.samplebroker.repos;

import org.powertac.samplebroker.domain.ClearedQuantity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClearedQuantityRepo extends CrudRepository<ClearedQuantity, Integer> {

}
