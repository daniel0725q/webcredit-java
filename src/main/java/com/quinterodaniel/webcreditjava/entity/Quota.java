package com.quinterodaniel.webcreditjava.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name="quotas")
public class Quota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private BigDecimal capitalFee;

    @Column(nullable = false)
    private BigDecimal interestFee;

    @Column(nullable = false)
    private int quotaNumber;

    @Column(nullable = false)
    private BigDecimal remainingValue;

    @Column(nullable = false)
    private Boolean paid;

    @Column(nullable = false)
    private BigDecimal paidAmount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="simulation_id", nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Simulation simulation;
}
