package com.suma.hmis_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import tools.jackson.databind.JsonNode;

@FeignClient(
        name = "patientClient",
        url = "${patient.api.url}"
)
public interface PatientClient {

    @GetMapping("/")
    JsonNode getRanddomUser();
}
