package com.quinterodaniel.webcreditjava.service.impl;

import com.quinterodaniel.webcreditjava.dto.QuotaDTO;
import com.quinterodaniel.webcreditjava.dto.SimulationDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimulationServiceImpl {

    private static final BigDecimal TAX = BigDecimal.valueOf(0.017);

    private static final BigDecimal TAX_PLUS_ONE = BigDecimal.valueOf(1.017);

    public SimulationDTO generate(BigDecimal value, int timeLimit, int productType) {

        var dividend = TAX
                .multiply(
                    TAX_PLUS_ONE.pow(timeLimit)
                );
        var divisor  = TAX_PLUS_ONE.pow(timeLimit)
                .subtract(BigDecimal.ONE);

        var quota = dividend.divide(divisor, RoundingMode.HALF_UP)
                .multiply(value).setScale(2, RoundingMode.HALF_UP) ;;

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

        return new SimulationDTO(quotas);
    }
}
