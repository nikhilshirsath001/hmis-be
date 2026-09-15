package com.suma.hmis_service.feature.eligibility;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OneEligibilityPayload {

    private Patient patient;
    private Insurance insurance;
    private BasicDetails basicDetails;
    private DoctorDetails doctorDetails;
    private EligibilityPurposes eligibilityPurposes;
    private List<Diagnosis> diagnosis;
    private Billing billing;

    @Getter
    @Setter
    public static class Patient {
        private Long patientId;
    }

    @Getter
    @Setter
    public static class Insurance {
        private Long insurancePolicyId;
        private String policyNumber;
        private String providerName;
        private String planName;
        private String memberId;
        private String groupNumber;
        private String coverageAmount;
        private String claimedAmount;
        private String remainingAmount;
        private String startDate;
        private String endDate;
    }

    @Getter
    @Setter
    public static class BasicDetails {
        private String claimId;
        private String wardName;
        private String bedNumber;
        private String ipdNumber;
    }

    @Getter
    @Setter
    public static class DoctorDetails {
        private String healthcareProfessionId;
        private String role;
        private String contactNumber;
        private String qualification;
    }

    @Getter
    @Setter
    public static class EligibilityPurposes {
        private boolean authRequirement;
        private boolean benefits;
        private boolean discovery;
        private boolean validation;
        private List<String> eligibilityPurposes;
    }

    @Getter
    @Setter
    public static class Diagnosis {
        private Long id;
        private String code;
        private String name;
        private String description;
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
}

