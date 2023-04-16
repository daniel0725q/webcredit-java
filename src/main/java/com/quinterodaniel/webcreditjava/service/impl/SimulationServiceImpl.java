package com.quinterodaniel.webcreditjava.service.impl;

import com.quinterodaniel.webcreditjava.dto.QuotaDTO;
import com.quinterodaniel.webcreditjava.dto.SimulationDTO;
import com.quinterodaniel.webcreditjava.entity.Quota;
import com.quinterodaniel.webcreditjava.entity.Simulation;
import com.quinterodaniel.webcreditjava.repository.QuotaRepository;
import com.quinterodaniel.webcreditjava.repository.SimulationRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SimulationServiceImpl {

    private static final BigDecimal TAX = BigDecimal.valueOf(0.017);

    private static final BigDecimal TAX_PLUS_ONE = BigDecimal.valueOf(1.017);

    @Autowired
    private SimulationRepository simulationRepository;

    @Autowired
    private QuotaRepository quotaRepository;

    public SimulationDTO generate(BigDecimal value, int timeLimit, int productType) {
        BigDecimal total = value;

        var dividend = TAX
                .multiply(
                    TAX_PLUS_ONE.pow(timeLimit)
                );
        var divisor  = TAX_PLUS_ONE.pow(timeLimit)
                .subtract(BigDecimal.ONE);

        var quota = dividend.divide(divisor, RoundingMode.HALF_UP)
                .multiply(value).setScale(2, RoundingMode.HALF_UP) ;

        List<QuotaDTO> quotas = new ArrayList<>();
        for (int i = 1; i <= timeLimit; i++) {
            BigDecimal d = value.multiply(TAX);
            BigDecimal capitalFee = quota.subtract(d);
            QuotaDTO quotaDTO = QuotaDTO.builder()
                    .quotaNumber(i)
                    .interestFee(d.setScale(0, RoundingMode.HALF_UP))
                    .capitalFee(capitalFee.setScale(0, RoundingMode.HALF_UP))
                    .remainingValue(
                            value.subtract(capitalFee).setScale(0, RoundingMode.HALF_UP)
                    ).build();

            quotas.add(quotaDTO);
            value = value.subtract(capitalFee).setScale(2, RoundingMode.HALF_UP);
        }

        var lastQuota = quotas.get(quotas.size() - 1);
        lastQuota.setRemainingValue(BigDecimal.ZERO);

        quotas.set(quotas.size() - 1, lastQuota);

        return SimulationDTO.builder()
                .value(total)
                .paymentPlan(quotas)
                .productType(productType)
                .build();
    }

    public void save(SimulationDTO simulationDTO, String username) {
        Simulation simulation = new Simulation();

        List<Quota> quotas = simulationDTO.getPaymentPlan().stream().map((quotaDTO) -> {
            Quota quota = new Quota();

            quota.setQuotaNumber(quotaDTO.getQuotaNumber());
            quota.setCapitalFee(quotaDTO.getCapitalFee());
            quota.setInterestFee(quotaDTO.getInterestFee());
            quota.setRemainingValue(quotaDTO.getRemainingValue());
            quota.setPaid(Boolean.FALSE);
            quota.setPaidAmount(BigDecimal.ZERO);
            quota.setSimulation(simulation);

            return quota;
        }).collect(Collectors.toList());

        simulation.setValue(simulationDTO.getValue());
        simulation.setPaymentPlan(quotas);
        simulation.setUserId(username);
        simulation.setProductType(simulationDTO.getProductType());

        simulationRepository.save(simulation);
        quotaRepository.saveAll(quotas);
    }

    public List<Simulation> getAllByUser(String username) {
        return simulationRepository.findByUserId(username);
    }

    public Simulation getById(Long id, String username) throws Exception {
        var simulationOptional = simulationRepository.findById(id);

        if (!simulationOptional.isPresent()) {
            throw new Exception("Simulation does not exist");
        }

        var simulation = simulationOptional.get();
        if (simulation.getUserId().equals(username)) {
            return simulation;
        }

        throw new Exception(("Given simulation was not created by user"));
    }
}
