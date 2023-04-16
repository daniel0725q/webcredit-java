package com.quinterodaniel.webcreditjava.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="simulations")
public class Simulation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "simulation_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(nullable = false)
    private Integer productType;

    @OneToMany(mappedBy="simulation")
    private List<Quota> paymentPlan;
}
