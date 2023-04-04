package com.quinterodaniel.webcreditjava.controller;

import com.quinterodaniel.webcreditjava.model.request.SimulationParams;
import com.quinterodaniel.webcreditjava.service.impl.SimulationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/simulations")
public class SimulationsController {

    @Autowired
    private SimulationServiceImpl simulationService;

    @PostMapping
    @RequestMapping("/generate")
    public ResponseEntity generate(@RequestBody SimulationParams params) {
        var simulation = simulationService.generate(new BigDecimal(params.getValue()), params.getTimeLimit(), 1);

        return ResponseEntity.ok(simulation);
    }
}
