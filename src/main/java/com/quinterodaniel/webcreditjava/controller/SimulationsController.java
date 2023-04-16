package com.quinterodaniel.webcreditjava.controller;

import com.quinterodaniel.webcreditjava.dto.SimulationDTO;
import com.quinterodaniel.webcreditjava.model.request.SimulationParams;
import com.quinterodaniel.webcreditjava.service.impl.SimulationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;

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

    @PostMapping
    @RequestMapping("/save")
    public ResponseEntity saveSimulation(@RequestBody SimulationDTO simulationDTO, Authentication authentication) {
        simulationService.save(simulationDTO, authentication.getName());

        return ResponseEntity.ok(null);
    }

    @GetMapping
    @RequestMapping("/get")
    public ResponseEntity getSimulations(Authentication authentication) {
        return ResponseEntity.ok(simulationService.getAllByUser(authentication.getName()));
    }

    @GetMapping
    @RequestMapping("/get/{id}")
    public ResponseEntity getSimulations(Authentication authentication, @PathVariable("id") String id) throws Exception {
        return ResponseEntity.ok(simulationService.getById(Long.valueOf(id), authentication.getName()));
    }

    @GetMapping
    @RequestMapping("/compare/{firstSimulation}/{secondSimulation}")
    public ResponseEntity getSimulations(Authentication authentication,
                                         @PathVariable("firstSimulation") String firstSimulation,
                                         @PathVariable("secondSimulation") String secondSimulation
                                         ) throws Exception {
        var simulations = Arrays.asList(
                simulationService.getById(Long.valueOf(firstSimulation), authentication.getName()),
                simulationService.getById(Long.valueOf(secondSimulation), authentication.getName())
        );
        return ResponseEntity.ok(simulations);
    }
}
