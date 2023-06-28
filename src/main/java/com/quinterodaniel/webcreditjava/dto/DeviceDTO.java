package com.quinterodaniel.webcreditjava.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class DeviceDTO {
    private long id;

    private Boolean available;

    private Date lastModified;

    private Date lendUntil;
}
