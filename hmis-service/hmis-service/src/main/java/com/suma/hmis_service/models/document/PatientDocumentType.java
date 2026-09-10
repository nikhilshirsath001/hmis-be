package com.suma.hmis_service.models.document;

public enum PatientDocumentType {

    PHOTO,
    PAN,
    AADHAAR,
    PASSPORT,
    DRIVING_LICENSE,
    VOTER_ID,
    INSURANCE,
    MEDICAL_REPORT,
    PRESCRIPTION,
    DISCHARGE_SUMMARY,
    OTHER;
    public static PatientDocumentType fromValue(String value) {
        return PatientDocumentType.valueOf(value.toUpperCase());
    }
}


