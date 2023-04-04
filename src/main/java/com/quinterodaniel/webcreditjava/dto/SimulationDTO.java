package com.quinterodaniel.webcreditjava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SimulationDTO {

    private List<QuotaDTO> paymentPlan;
}
