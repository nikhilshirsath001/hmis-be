package com.suma.hmis_service.feature.treatment;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "treatments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String diagnosis;

    private String medication;

    private String dosage;

    private LocalDate startDate;

    private LocalDate endDate;

    private String status;

    private String packageCode;

    private String packageName;

    private String admissionType;

    private LocalDate proposedAdmissionDate;

    private Integer estimatedLengthOfStay;
}
