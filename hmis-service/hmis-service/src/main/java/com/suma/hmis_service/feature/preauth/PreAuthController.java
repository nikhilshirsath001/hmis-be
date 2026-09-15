package com.suma.hmis_service.feature.preauth;


import com.suma.hmis_service.models.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;

@RestController
@RequestMapping("/api/pre-auth")
@RequiredArgsConstructor
public class PreAuthController {

    private final PreAuthService preAuthService;
    private final PreAuthGeneratorService preAuthGeneratorService;
    private final PreAuthPayloadRepository repository;

    @PostMapping("/prepare")
    public ResponseEntity<ApiResponse> preparePreAuth(@Valid @RequestBody PreAuthRequest request) {
        PreAuthResponse response = preAuthService.buildPreAuthPayload(request);
        return ResponseEntity.ok(
                new ApiResponse(
                        1,
                        response.getMessage(),
                        response.getPayload()
                )
        );
    }


    @GetMapping("/generate")
    public ResponseEntity<ApiResponse> generatePreAuth(@RequestParam Long patientId) {
        PreAuthPayload payload = preAuthGeneratorService.generate(patientId);
        return ResponseEntity.ok(
                new ApiResponse(1, "Pre-auth payload generated successfully", payload)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse> save(@RequestBody JsonNode payload) {
        PreAuthPayloadEntity entity = new PreAuthPayloadEntity();
//        entity.setPatientId(payload.path("data").path("basicDetails").path("claimId").asText(null));
        entity.setPatientId(payload.path("patientId").asText(null));
        entity.setPayload(payload.toString());
        PreAuthPayloadEntity saved = repository.save(entity);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(saved.getPayload());
        JsonNode savedPayload = root.get("payload");
        PreAuthPayload preAuthPayloadEntity = mapper.convertValue(savedPayload, PreAuthPayload.class);

        return ResponseEntity.ok(
                new ApiResponse(1, "Pre-auth payload saved successfully", preAuthPayloadEntity)
        );
    }
}
