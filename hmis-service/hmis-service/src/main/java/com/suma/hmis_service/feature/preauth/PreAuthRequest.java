package com.suma.hmis_service.feature.preauth;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PreAuthRequest {

    @NotNull
    private Long patientId;

    @NotNull
    private Long insurancePolicyId;

    @NotBlank
    private String claimId;

    @NotBlank
    private String wardNumber;

    @NotBlank
    private String bedNumber;

    @NotBlank
    private String ipdNumber;

    @NotNull
    private PreAuthEnums.AdmissionType admissionType;

    @NotNull
    private Boolean medicoLegalCase;

    @NotBlank
    private String specialtyDepartment;

    @NotNull
    private LocalDateTime admissionDateTime;

    @NotNull
    private LocalDateTime dischargeDateTime;

    @NotBlank
    private String claimType;

    @NotBlank
    private String claimSubType;

    @NotBlank
    private String claimUse;

    @NotBlank
    private String priority;

    @Valid
    private List<AlcoholHistoryRequest> alcoholHistories;

    @Valid
    private List<GeneralHistoryRequest> generalHistories;

    @NotEmpty
    private List<Long> doctorIds;

    @NotEmpty
    @Valid
    private List<DiagnosisRequest> diagnoses;

    @NotEmpty
    @Valid
    private List<ProcedureRequest> procedures;

    @NotEmpty
    @Valid
    private List<ServiceRequest> services;

    @NotEmpty
    @Size(min = 2, message = "At least 2 documents are required")
    private List<String> documentIds;

    @Getter
    @Setter
    public static class AlcoholHistoryRequest {
        @NotNull
        private PreAuthEnums.AlcoholHistoryCategory category;
        private String additionalInformation;
    }

    @Getter
    @Setter
    public static class GeneralHistoryRequest {
        @NotNull
        private PreAuthEnums.GeneralHistoryCategory category;
        private String additionalInformation;
    }

    @Getter
    @Setter
    public static class DiagnosisRequest {
        @NotNull
        private Long diagnosisId;
        @NotNull
        private PreAuthEnums.DiagnosisType type;
        @NotNull
        private PreAuthEnums.PresentOnAdmission presentOnAdmission;
        @NotNull
        private PreAuthEnums.ClinicalStatus clinicalStatus;
        @NotNull
        private PreAuthEnums.Severity severity;
    }

    @Getter
    @Setter
    public static class ProcedureRequest {
        @NotNull
        private PreAuthEnums.ProcedureCategory category;
        @NotBlank
        private String procedureName;
        private String customProcedureName;
        private String stratification;
        @NotNull
        @Min(1)
        private Integer daysOrQuantity;
        @NotNull
        private BigDecimal amount;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }

    @Getter
    @Setter
    public static class ServiceRequest {
        @NotNull
        private Long serviceId;
        @NotNull
        @Min(1)
        private Integer quantity;
    }
}
