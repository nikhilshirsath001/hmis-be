package com.suma.hmis_service.controllers;

import com.suma.hmis_service.models.ApiResponse;
import com.suma.hmis_service.models.billing.BillingDto;
import com.suma.hmis_service.models.billing.CreateBillingRequest;
import com.suma.hmis_service.models.constants.ApiConstant;
import com.suma.hmis_service.models.patient.CreatePatientRequest;
import com.suma.hmis_service.services.HmisService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Map;

@RestController
@RequestMapping(ApiConstant.Controller.HMIS)
@AllArgsConstructor
public class HmisController {
    @Autowired
    private HmisService hmisService;

    @GetMapping(ApiConstant.Hmis.PATIENT)
    public ResponseEntity<ApiResponse> getPatientByAbhaId(@RequestParam String abhaId) {
        return ResponseEntity.ok().body(hmisService.getPatientByAbhaId(abhaId));
    }

    @PostMapping(ApiConstant.Hmis.CREATE_PATIENT)
    public ResponseEntity<ApiResponse> createPatients(
            @RequestPart("metadata") CreatePatientRequest createPatientRequest,
            MultipartHttpServletRequest multipartHttpServletRequest
    ) {
        Map<String, MultipartFile> files = multipartHttpServletRequest.getFileMap();
        files.remove("metadata");
        return ResponseEntity.ok().body(hmisService.createPatients(createPatientRequest, files));
    }


//    Contact Person APIS
    @GetMapping(ApiConstant.Hmis.CONTACT_PERSON)
    public ResponseEntity<ApiResponse> getContanctPersonByAbhaId(@RequestParam String abhaId) {
        return ResponseEntity.ok().body(hmisService.getContanctPersonByAbhaId(abhaId));
    }


    // Billing modules Apis
    @GetMapping(ApiConstant.Hmis.BILLING)
    public ResponseEntity<ApiResponse> getBillingByClaimId(@RequestParam String claimId) {
        return ResponseEntity.ok().body(hmisService.getBillingByClaimId(claimId));
    }

    // Billing modules Apis
    @GetMapping(ApiConstant.Hmis.BILLING+"/{patientId}")
    public ResponseEntity<ApiResponse> getBillingByPatientId(@PathVariable(name = "patientId") String patientId) {
        return ResponseEntity.ok().body(hmisService.getBillingByPatientId(patientId));
    }

    @PostMapping(ApiConstant.Hmis.BILLING_CREATE)
    public ResponseEntity<ApiResponse> createBilling(@RequestBody CreateBillingRequest createBillingRequest) {
        return ResponseEntity.ok().body(hmisService.createBilling(createBillingRequest));
    }


}
