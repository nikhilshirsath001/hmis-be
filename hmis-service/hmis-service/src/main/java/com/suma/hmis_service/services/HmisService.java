package com.suma.hmis_service.services;

import com.suma.hmis_service.models.ApiResponse;
import com.suma.hmis_service.models.patient.CreatePatientRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface HmisService {
    public ApiResponse getPatientByAbhaId(String abhaId);
    public ApiResponse createPatients(CreatePatientRequest request, Map<String, MultipartFile> files);

    public ApiResponse getContanctPersonByAbhaId(String abhaId);
}
