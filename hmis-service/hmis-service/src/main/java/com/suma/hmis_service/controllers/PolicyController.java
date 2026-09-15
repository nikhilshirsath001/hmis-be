package com.suma.hmis_service.controllers;
import com.suma.hmis_service.models.ApiResponse;
import com.suma.hmis_service.models.CreatePolicyRequest;
import com.suma.hmis_service.models.constants.ApiConstant;
import com.suma.hmis_service.services.PolicyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(ApiConstant.Controller.HMIS)
public class PolicyController {

    @Autowired
    private PolicyServiceImpl policyService ;


    @GetMapping(ApiConstant.Hmis.GET_POLICY_BY_PATIENT)
    public ResponseEntity<ApiResponse> getPoliciesByPatient(@RequestParam (name = "patientId") Long patientId) {
        return  ResponseEntity.ok().body(policyService.getPoliciesByPatient(patientId));
    }

    @PostMapping(ApiConstant.Hmis.CREATE_POLICIES)
    public ResponseEntity<ApiResponse> saveExternalPolicies(@RequestBody CreatePolicyRequest request) {
        return ResponseEntity.ok().body(policyService.getExternalPolicies(request));
    }

}


