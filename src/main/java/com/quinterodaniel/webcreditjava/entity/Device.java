package com.quinterodaniel.webcreditjava.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Device {
    @Id
    private long id;

    @Column(nullable = false)
    private Boolean available;

    @Column
    private Date lastModified;

    @OneToOne(mappedBy = "device")
    private User user;

    @Column
    private Date lendUntil;
}
