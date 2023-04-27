package com.quinterodaniel.webcreditjava.model.request;

import com.quinterodaniel.webcreditjava.model.enums.ProductTypeEnum;
import lombok.Data;

@Data
public class SimulationParams {
    private String value;

    private int timeLimit;

    private int productType;
}
