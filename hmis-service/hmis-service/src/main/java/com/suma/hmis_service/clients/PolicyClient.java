package com.suma.hmis_service.clients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tools.jackson.databind.JsonNode;

@FeignClient(
        name = "health-insurance-client",
        url = "https://hapi.fhir.org/baseR4"
)
public interface PolicyClient {

    @GetMapping("/Coverage")
    JsonNode getCoverage(@RequestParam("identifier") String policyNumber);

    @GetMapping("/Coverage")
    JsonNode getAllCoverage();

}
