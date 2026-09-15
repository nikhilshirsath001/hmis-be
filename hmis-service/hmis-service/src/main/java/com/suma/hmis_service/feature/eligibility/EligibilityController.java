package com.suma.hmis_service.feature.eligibility;

import com.suma.hmis_service.models.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eligibility")
@RequiredArgsConstructor
public class EligibilityController {

    private final EligibilityService eligibilityService;

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
}

