package com.quinterodaniel.webcreditjava.service.impl;

import com.quinterodaniel.webcreditjava.dto.QuotaDTO;
import com.quinterodaniel.webcreditjava.dto.SimulationDTO;
import com.quinterodaniel.webcreditjava.entity.Quota;
import com.quinterodaniel.webcreditjava.entity.Simulation;
import com.quinterodaniel.webcreditjava.model.enums.ProductTypeEnum;
import com.quinterodaniel.webcreditjava.repository.QuotaRepository;
import com.quinterodaniel.webcreditjava.repository.SimulationRepository;
import com.quinterodaniel.webcreditjava.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SimulationServiceImpl implements SimulationService {

    @Autowired
    private SimulationRepository simulationRepository;

    @Autowired
    private QuotaRepository quotaRepository;

    @Override
    public SimulationDTO generate(BigDecimal value, int timeLimit, int productType) throws Exception {
        BigDecimal total = value;

        BigDecimal tax;

        switch (ProductTypeEnum.valueOf(productType)) {
            case VEHICLE:
                tax = BigDecimal.valueOf(0.0231);
                break;
            case MORTGAGE:
                tax = BigDecimal.valueOf(0.0140);
                break;
            case EDUCATION:
                tax = BigDecimal.valueOf(0.0160);
                break;
            case FREE:
                tax = BigDecimal.valueOf(0.0217);
                break;
            default:
                throw new Exception("Unknown product type");
        }

        BigDecimal taxPlusOne = tax.add(BigDecimal.ONE);

        var dividend = tax
                .multiply(
                    taxPlusOne.pow(timeLimit)
                );
        var divisor  = taxPlusOne.pow(timeLimit)
                .subtract(BigDecimal.ONE);

        var quota = dividend.divide(divisor, RoundingMode.HALF_UP)
                .multiply(value).setScale(2, RoundingMode.HALF_UP) ;

        List<QuotaDTO> quotas = new ArrayList<>();
        for (int i = 1; i <= timeLimit; i++) {
            BigDecimal d = value.multiply(tax);
            BigDecimal capitalFee = quota.subtract(d);
            QuotaDTO quotaDTO = QuotaDTO.builder()
                    .quotaNumber(i)
                    .interestFee(d.setScale(2, RoundingMode.HALF_UP))
                    .capitalFee(capitalFee.setScale(2, RoundingMode.HALF_UP))
                    .value(quota)
                    .remainingValue(
                            value.subtract(capitalFee)
                                    .setScale(2, RoundingMode.HALF_UP)
                    )
                    .build();

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

    @Override
    public void save(SimulationDTO simulationDTO, String username) {
        Simulation simulation = new Simulation();

        List<Quota> quotas = simulationDTO.getPaymentPlan().stream()
                .map((quotaDTO) -> createQuotaFromDTO(simulation, quotaDTO))
                .collect(Collectors.toList());

        simulation.setValue(simulationDTO.getValue());
        simulation.setPaymentPlan(quotas);
        simulation.setUserId(username);
        simulation.setProductType(simulationDTO.getProductType());
        simulation.setIsLoan(Boolean.FALSE);

        simulationRepository.save(simulation);
        quotaRepository.saveAll(quotas);
    }

    public Quota createQuotaFromDTO(Simulation simulation, QuotaDTO quotaDTO) {
        Quota quota = new Quota();

        quota.setQuotaNumber(quotaDTO.getQuotaNumber());
        quota.setCapitalFee(quotaDTO.getCapitalFee());
        quota.setInterestFee(quotaDTO.getInterestFee());
        quota.setRemainingValue(quotaDTO.getRemainingValue());
        quota.setPaid(Boolean.FALSE);
        quota.setPaidAmount(BigDecimal.ZERO);
        quota.setValue(quotaDTO.getValue());
        quota.setSimulation(simulation);

        return quota;
    }

    @Override
    public List<Simulation> getAllByUser(String username) {
        return simulationRepository.findByUserId(username);
    }

    @Override
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
