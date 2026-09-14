package com.suma.hmis_service.controllers;
import com.suma.hmis_service.models.ApiResponse;
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


    @GetMapping(ApiConstant.Hmis.GETPOLICYBYPATIENT)
    public ApiResponse getPoliciesByPatient(@PathVariable Long patientId) {
        return policyService.getPoliciesByPatient(patientId);
    }

    @PostMapping(ApiConstant.Hmis.CREATEPOLICIES)
    public ApiResponse saveExternalPolicies(@PathVariable Long patientId , @RequestParam String policyNumber) {
        return policyService.getExternalPolicies(patientId,policyNumber);
    }

}


