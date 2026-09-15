package com.suma.hmis_service.services;
import com.suma.hmis_service.models.ApiResponse;


public interface PolicyService {

  ApiResponse getExternalPolicies(Long patientId, String policyNumber);

  ApiResponse getPoliciesByPatient(Long patientId);



}
