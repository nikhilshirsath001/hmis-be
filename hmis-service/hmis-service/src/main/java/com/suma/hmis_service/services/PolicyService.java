package com.suma.hmis_service.services;
import com.suma.hmis_service.models.ApiResponse;
import com.suma.hmis_service.models.CreatePolicyRequest;


public interface PolicyService {

  ApiResponse getExternalPolicies(CreatePolicyRequest request);

  ApiResponse getPoliciesByPatient(Long patientId);



}
