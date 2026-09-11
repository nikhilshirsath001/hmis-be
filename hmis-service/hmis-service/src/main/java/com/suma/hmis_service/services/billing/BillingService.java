package com.suma.hmis_service.services.billing;

import com.suma.hmis_service.models.ApiResponse;
import com.suma.hmis_service.models.billing.CreateBillingRequest;

public interface BillingService {
    ApiResponse getBillingByClaimId(String claimId);

    ApiResponse createBilling(CreateBillingRequest createBillingRequest);

    ApiResponse getBillingByPatientId(String patientId);
}
