package com.suma.hmis_service.feature.preauth;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PreAuthPayload {

    private Patient patient;
    private BasicDetails basicDetails;
    private PatientObservationHistory observationHistory;
    private List<DoctorDetails> doctors;
    private List<DiagnosisDetails> diagnoses;
    private List<ProcedureDetails> procedures;
    private Billing billing;
    private List<DocumentDetails> documents;

    @Getter
    @Setter
    public static class Patient {
        private String name;
        private String abhaId;
        private String walletBalance;
        private String mobileNumber;
        private String mrNumber;
        private String gender;
    }

    @Getter
    @Setter
    public static class BasicDetails {
        private String claimId;
        private Long insuranceId;
        private String wardNumber;
        private String bedNumber;
        private String ipdNumber;
        private String admissionType;
        private Boolean medicoLegalCase;
        private String specialtyDepartment;
        private String admissionDateTime;
        private String dischargeDateTime;
        private String claimType;
        private String claimSubType;
        private String claimUse;
        private String priority;
    }

    @Getter
    @Setter
    public static class PatientObservationHistory {
        private List<AlcoholHistory> alcoholHistory;
        private List<GeneralHistory> generalHistory;
    }

    @Getter
    @Setter
    public static class AlcoholHistory {
        private String category;
        private String additionalInformation;
    }

    @Getter
    @Setter
    public static class GeneralHistory {
        private String category;
        private String additionalInformation;
    }

    @Getter
    @Setter
    public static class DoctorDetails {
        private String name;
        private String healthcareProfessionalId;
        private String role;
        private String contactNumber;
        private String qualification;
    }

    @Getter
    @Setter
    public static class DiagnosisDetails {
        private String type;
        private String presentOnAdmission;
        private String name;
        private String code;
        private String clinicalStatus;
        private String severity;
    }

    @Getter
    @Setter
    public static class ProcedureDetails {
        private String category;
        private String procedureName;
        private String customProcedureName;
        private String stratification;
        private Integer daysOrQuantity;
        private String amount;
        private String startDate;
        private String endDate;
    }

    @Getter
    @Setter
    public static class Billing {
        private List<BillingService> consultation;
        private List<BillingService> investigation;
        private List<BillingService> operationTheatre;
        private List<BillingService> other;
    }

    @Getter
    @Setter
    public static class BillingService {
        private String code;
        private String description;
        private String amount;
        private Integer quantity;
    }

    @Getter
    @Setter
    public static class DocumentDetails {
        private String documentId;
        private String fileName;
        private String contentType;
        private Long fileSize;
        private String documentType;
        private String url;
    }
}
