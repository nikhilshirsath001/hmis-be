package com.suma.hmis_service.feature.treatment;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentResponse {

    private Long id;

    private String name;

    private String description;

    private String diagnosis;

    private String medication;

    private String dosage;

    private LocalDate startDate;

    private LocalDate endDate;

    private String status;

    // Package details
    private String packageCode;

    private String packageName;

    private String admissionType;

    private String proposedAdmissionDate;

    private Integer estimatedLengthOfStay;
}
