package com.quinterodaniel.webcreditjava.repository;

import com.quinterodaniel.webcreditjava.entity.Simulation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SimulationRepository extends CrudRepository<Simulation, Long> {
    List<Simulation> findByUserId(String userId);
}
