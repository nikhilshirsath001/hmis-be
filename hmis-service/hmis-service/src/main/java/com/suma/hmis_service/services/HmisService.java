package com.suma.hmis_service.services;

import com.suma.hmis_service.models.ApiResponse;

public interface HmisService {
    ApiResponse getPatientByAbhaId(String abhaId);
}
