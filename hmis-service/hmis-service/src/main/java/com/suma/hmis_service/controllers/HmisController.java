package com.suma.hmis_service.controllers;

import com.suma.hmis_service.models.ApiResponse;
import com.suma.hmis_service.models.constants.ApiConstant;
import com.suma.hmis_service.services.HmisService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstant.Controller.HMIS)
@AllArgsConstructor
public class HmisController {
    @Autowired
    private HmisService hmisService;

    @GetMapping(ApiConstant.Hmis.PATIENT)
    public ResponseEntity<ApiResponse> getPatientByAbhaId(@RequestParam String abhaId) {
        return ResponseEntity.ok().body(hmisService.getPatientByAbhaId(abhaId));
    }}
