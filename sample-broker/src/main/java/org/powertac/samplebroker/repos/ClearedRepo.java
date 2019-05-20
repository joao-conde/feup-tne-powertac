package org.powertac.samplebroker.repos;

import org.powertac.samplebroker.domain.Cleared;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClearedRepo extends CrudRepository<Cleared, Integer> {
    
}