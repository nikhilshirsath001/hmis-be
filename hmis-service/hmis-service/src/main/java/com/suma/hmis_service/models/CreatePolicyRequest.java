package com.suma.hmis_service.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePolicyRequest {

    private Long patientId;
    private String policyNumber;

}
