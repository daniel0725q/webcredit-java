package com.quinterodaniel.webcreditjava.service.impl;

import com.quinterodaniel.webcreditjava.dto.PaymentDTO;
import com.quinterodaniel.webcreditjava.dto.QuotaDTO;
import com.quinterodaniel.webcreditjava.entity.Quota;
import com.quinterodaniel.webcreditjava.repository.QuotaRepository;
import com.quinterodaniel.webcreditjava.repository.SimulationRepository;
import com.quinterodaniel.webcreditjava.service.SimulationService;
import com.quinterodaniel.webcreditjava.service.UserService;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl {
    private final QuotaRepository quotaRepository;

    private final SimulationService simulationService;
    private final SimulationRepository simulationRepository;

    public PaymentServiceImpl(QuotaRepository quotaRepository, SimulationService simulationService,
                              SimulationRepository simulationRepository) {
        this.quotaRepository = quotaRepository;
        this.simulationService = simulationService;
        this.simulationRepository = simulationRepository;
    }

    public Quota performPayment(PaymentDTO paymentDTO) throws Exception {
        var quotaOptional = quotaRepository.findById(paymentDTO.getId());

        if (quotaOptional.isEmpty()) {
            throw new Exception("Quota does not exist");
        }

        var quota = quotaOptional.get();
        Quota previousQuota = null;

        quota.setPaidAmount(paymentDTO.getValue());
        quota.setCapitalFee(quota.getPaidAmount().subtract(quota.getInterestFee()));
        if (quota.getQuotaNumber() != 1) {
            previousQuota = quota.getSimulation().getPaymentPlan().get(quota.getQuotaNumber() - 2);
        }
        quota.setRemainingValue(previousQuota != null ? previousQuota.getRemainingValue().subtract(quota.getCapitalFee()) :
                quota.getSimulation().getValue().subtract(quota.getCapitalFee())
                );

        quota.setPaid(Boolean.TRUE);

        quotaRepository.save(quota);
        return quota;
    }

    public void recalculateQuotas(Quota quota) throws Exception {
        var simulation = quota.getSimulation();

        var remainingQuotas = simulation.getPaymentPlan().size() - quota.getQuotaNumber();
        var newSimulation = simulationService.generate(quota.getRemainingValue(),
                remainingQuotas, simulation.getProductType());

        List<QuotaDTO> quotasDTO = new ArrayList<>();
        quotasDTO.addAll(newSimulation.getPaymentPlan());

        List<Quota> quotas = simulation.getPaymentPlan();
        quotas = quotas.stream().sorted().collect(Collectors.toList());
        var n = 0;
        for (int i = quota.getQuotaNumber() + 1; i <= remainingQuotas + 1; i++) {
            int finalI = i;
            var q = quotas.stream().filter(qr -> qr.getQuotaNumber() == finalI).findFirst().orElse(null);

            var nq = quotasDTO.get(n);
            q.setRemainingValue(nq.getRemainingValue());
            q.setValue(nq.getValue());
            q.setCapitalFee(nq.getCapitalFee());
            q.setInterestFee(nq.getInterestFee());
            n++;

        }
        quotaRepository.saveAll(quotas);
        simulation.setPaymentPlan(quotas);
        simulationRepository.save(simulation);
    }
}
