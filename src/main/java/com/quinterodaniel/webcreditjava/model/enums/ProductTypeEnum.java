package com.quinterodaniel.webcreditjava.model.enums;

public enum ProductTypeEnum {
    VEHICLE(1),
    MORTGAGE(2),
    EDUCATION(3),
    FREE(4);

    public final int id;

    ProductTypeEnum(int id) {
        this.id = id;
    }

    public static ProductTypeEnum valueOf(int id) {
        for (ProductTypeEnum e : values()) {
            if (e.id == id) {
                return e;
            }
        }
        return null;
    }
}
