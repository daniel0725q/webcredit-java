package com.quinterodaniel.webcreditjava.service;

import com.quinterodaniel.webcreditjava.dto.QuotaDTO;
import com.quinterodaniel.webcreditjava.dto.SimulationDTO;
import com.quinterodaniel.webcreditjava.entity.Quota;
import com.quinterodaniel.webcreditjava.entity.Simulation;

import java.math.BigDecimal;
import java.util.List;

public interface SimulationService {
    SimulationDTO generate(BigDecimal value, int timeLimit, int productType) throws Exception;

    void save(SimulationDTO simulationDTO, String username);

    List<Simulation> getAllByUser(String username);

    Simulation getById(Long id, String username) throws Exception;

    Quota createQuotaFromDTO(Simulation simulation, QuotaDTO quotaDTO);

}
