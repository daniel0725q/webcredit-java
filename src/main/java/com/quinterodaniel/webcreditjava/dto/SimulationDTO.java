package com.quinterodaniel.webcreditjava.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class SimulationDTO {
    private BigDecimal value;

    private List<QuotaDTO> paymentPlan;

    private Integer productType;
}
