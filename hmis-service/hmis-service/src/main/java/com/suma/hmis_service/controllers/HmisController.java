package com.suma.hmis_service.controllers;

import com.suma.hmis_service.models.ApiResponse;
import com.suma.hmis_service.models.constants.ApiConstant;
import com.suma.hmis_service.models.patient.CreatePatientRequest;
import com.suma.hmis_service.services.HmisService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse> createPatients(@RequestBody CreatePatientRequest request) {
        return ResponseEntity.ok().body(hmisService.createPatients(request));
    }


//    Contact Person APIS
    @GetMapping(ApiConstant.Hmis.CONTACT_PERSON)
    public ResponseEntity<ApiResponse> getContanctPersonByAbhaId(@RequestParam String abhaId) {
        return ResponseEntity.ok().body(hmisService.getContanctPersonByAbhaId(abhaId));
    }

}
