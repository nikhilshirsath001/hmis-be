package com.suma.hmis_service.feature.preauth;


import com.suma.hmis_service.models.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pre-auth")
@RequiredArgsConstructor
public class PreAuthController {

    private final PreAuthService preAuthService;
    private final PreAuthGeneratorService preAuthGeneratorService;

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
}
