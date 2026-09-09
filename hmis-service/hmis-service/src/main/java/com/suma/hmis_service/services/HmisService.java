package com.suma.hmis_service.services;

import com.suma.hmis_service.models.ApiResponse;
import com.suma.hmis_service.models.patient.CreatePatientRequest;

public interface HmisService {
    ApiResponse getPatientByAbhaId(String abhaId);
    ApiResponse createPatients(CreatePatientRequest request);
}
