package com.suma.hmis_service.feature.eligibility;

import com.suma.hmis_service.feature.preauth.PreAuthPayload;
import com.suma.hmis_service.feature.preauth.PreAuthPayloadEntity;
import com.suma.hmis_service.models.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/eligibility")
@RequiredArgsConstructor
public class EligibilityController {

    private final EligibilityService eligibilityService;
    private final EligibilityPayloadRepository eligibilityPayloadRepository;

    @PostMapping("/prepare")
    public ResponseEntity<ApiResponse> prepareEligibility(@Valid @RequestBody EligibilityRequest request) {
        EligibilityResponse eligibilityResponse = eligibilityService.buildEligibilityPayload(request);
        return ResponseEntity.ok(
                new ApiResponse(
                        1,
                        "Eligibility payload prepared successfully",
                        eligibilityResponse
                )
        );
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse> getEligibilityStatus(@RequestParam String abhaId) {
        EligibilityResponsePayload status = eligibilityService.calculateAndGetEligibilityStatus(abhaId);
        return ResponseEntity.ok(
                new ApiResponse(
                        1,
                        "Eligibility status received.",
                        status
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse> save(@RequestBody JsonNode payload) {
        EligibilityPayloadEntity entity = new EligibilityPayloadEntity();
        entity.setPatientId(payload.path("patientId").asText(null));
        entity.setPayload(payload.toString());
        EligibilityPayloadEntity saved = eligibilityPayloadRepository.save(entity);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(saved.getPayload());
        JsonNode savedPayload = root.get("payload");
        EligibilityResponsePayload payloadEntity = mapper.convertValue(savedPayload, EligibilityResponsePayload.class);

        return ResponseEntity.ok(
                new ApiResponse(1, "Eligibility payload saved successfully", payloadEntity)
        );
    }
}

