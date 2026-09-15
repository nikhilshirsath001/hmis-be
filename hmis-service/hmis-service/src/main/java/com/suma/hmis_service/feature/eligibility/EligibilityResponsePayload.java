package com.suma.hmis_service.feature.eligibility;

import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class EligibilityResponsePayload {

    private String requestId;

    private BeneficiaryDetails patient;
    private WardDetails ward;
    private List<DoctorDetails> doctor;
    private List<DiagnosisDetails> diagnosis;
    private List<BillingDetails> billing;

    @Getter
    @Setter
    public static class BeneficiaryDetails {
        private Long patientId;
        private String name;
        private String abdmId;
        private String walletBalance;
        private String contactNumber;
        private String mrNumber;
        private String gender;
    }

    @Getter
    @Setter
    public static class WardDetails {
        private String id;
        private String badNumber;
        private String name;
        private String ipdNumber;
        private List<String> eligibilityPurpose;
    }

    @Getter
    @Setter
    public static class BillingDetails {
        private String facilityCategory;
        private String facilityName;
        private String facilityCode;
    }

    @Getter
    @Setter
    public static class DiagnosisDetails {
        private String name;
        private String code;
        private String description;
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

    @PrePersist
    private void generateId(){
        if (this.requestId==null){
            this.requestId = UUID.randomUUID().toString();
        }
    }
}
