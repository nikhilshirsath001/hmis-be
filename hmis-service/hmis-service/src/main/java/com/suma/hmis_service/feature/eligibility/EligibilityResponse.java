package com.suma.hmis_service.feature.eligibility;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EligibilityResponse {

    private String message;
    private OneEligibilityPayload providerPayload;
}

