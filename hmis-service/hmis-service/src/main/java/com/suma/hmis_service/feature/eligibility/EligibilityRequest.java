package com.suma.hmis_service.feature.eligibility;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EligibilityRequest {

    @NotNull
    private Long patientId;

    @NotNull
    private Long insurancePolicyId;

    @NotBlank
    private String claimId;

    @NotBlank
    private String wardName;

    @NotBlank
    private String bedNumber;

    @NotBlank
    private String ipdNumber;

    @NotNull
    private Long doctorId;

    @NotEmpty
    private List<String> eligibilityPurposes;

    @NotEmpty
    private List<Long> diagnosisIds;

    @NotEmpty
    @Valid
    private List<EligibilityServiceRequest> services;
}

