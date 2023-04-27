package com.quinterodaniel.webcreditjava.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class QuotaDTO {

    private BigDecimal capitalFee;

    private BigDecimal interestFee;

    private int quotaNumber;

    private BigDecimal remainingValue;

    private BigDecimal value;
}
