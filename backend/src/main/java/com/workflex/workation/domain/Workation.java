package com.workflex.workation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "workations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workation {

    @Id
    @Column(name = "workation_id", nullable = false, length = 36)
    private String workationId;

    @Column(nullable = false)
    private String employee;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "working_days", nullable = false)
    private int workingDays;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private RiskLevel risk;
}
