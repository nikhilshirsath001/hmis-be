package com.suma.hmis_service.feature.preauth;


public final class PreAuthEnums {

    private PreAuthEnums() {
    }

    public enum AdmissionType {
        GENERAL, EMERGENCY
    }

    public enum AlcoholHistoryCategory {
        EVERY_DAY, WEEKLY, EVENTS, NON_DRINKER
    }

    public enum GeneralHistoryCategory {
        ALLERGY_INTOLERANCE, ADVERSE_EVENT, CONDITION, FAMILY_MEMBER_HISTORY
    }

    public enum DiagnosisType {
        ADMITTING_DIAGNOSIS, AUTOPSY_DIAGNOSIS, CLINICAL_DIAGNOSIS, CYTOLOGY_DIAGNOSIS
    }

    public enum PresentOnAdmission {
        YES, NO, UNDETERMINED, UNKNOWN
    }

    public enum ClinicalStatus {
        ACTIVE, INACTIVE, RECURRENCE, RELAPSE
    }

    public enum Severity {
        MILD, MODERATE, SEVERE
    }

    public enum ProcedureCategory {
        BURNS_MANAGEMENT,
        CARDIO_THORACIC_VASCULAR_SURGERY,
        CARDIOLOGY,
        EMERGENCY_ROOM_PACKAGE
    }
}
