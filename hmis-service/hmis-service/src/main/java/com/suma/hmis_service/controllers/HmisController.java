package com.suma.hmis_service.controllers;

import com.suma.hmis_service.models.constants.ApiConstant;
import com.suma.hmis_service.services.HmisService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstant.Controller.HMIS)
@AllArgsConstructor
public class HmisController {
    @Autowired
    private HmisService hmisService;
}
