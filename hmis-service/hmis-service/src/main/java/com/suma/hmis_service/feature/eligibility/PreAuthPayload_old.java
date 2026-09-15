package com.suma.hmis_service.feature.eligibility;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PreAuthPayload_old {

    private String id;
    private String requestTimestamp;

    private BeneficiaryDetails beneficiary;
    private HospitalDetails hospital;
    private ClinicalDetails clinical;
    private TreatmentDetails treatment;
    private EstimateDetails estimate;
    private List<DocumentDetails> documents;
    private ConsentDetails consent;

    @Getter
    @Setter
    public static class BeneficiaryDetails {
        private String scheme;
        private String beneficiaryId;
        private String name;
        private String gender;
        private String dateOfBirth;
        private String state;
    }

    @Getter
    @Setter
    public static class HospitalDetails {
        private String facilityId;
        private String pmjayHospitalId;
        private String name;
        private String state;
    }

    @Getter
    @Setter
    public static class ClinicalDetails {
        private DiagnosisDetails diagnosis;
        private ProcedureDetails procedure;
    }

    @Getter
    @Setter
    public static class DiagnosisDetails {
        private String codeSystem;
        private String code;
        private String description;
    }

    @Getter
    @Setter
    public static class ProcedureDetails {
        private String codeSystem;
        private String code;
        private String description;
    }

    @Getter
    @Setter
    public static class TreatmentDetails {
        private String packageCode;
        private String packageName;
        private String admissionType;
        private String proposedAdmissionDate;
        private Integer estimatedLengthOfStay;
    }

    @Getter
    @Setter
    public static class EstimateDetails {
        private String currency;
        private BigDecimal totalAmount;
        private BigDecimal patientPayable;
        private List<BreakdownDetails> breakdown;
    }

    @Getter
    @Setter
    public static class BreakdownDetails {
        private String item;
        private BigDecimal amount;
    }

    @Getter
    @Setter
    public static class DocumentDetails {
        private String type;
        private String reference;
    }

    @Getter
    @Setter
    public static class ConsentDetails {
        private boolean patientConsent;
    }
}
