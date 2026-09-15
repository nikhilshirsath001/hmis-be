package com.suma.hmis_service.feature.insurance;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "insurance_policies",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_insurance_policy_number",
                        columnNames = "policy_number"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class InsurancePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "policy_number", nullable = false, length = 100)
    private String policyNumber;

    @Column(nullable = false, length = 150)
    private String providerName;

    @Column(nullable = false, length = 150)
    private String planName;

    @Column(nullable = false, length = 100)
    private String memberId;

    @Column(length = 100)
    private String groupNumber;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal coverageAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal claimedAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal remainingAmount;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private boolean active;
}

