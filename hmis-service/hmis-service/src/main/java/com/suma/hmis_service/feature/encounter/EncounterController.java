package com.suma.hmis_service.feature.encounter;

import com.suma.hmis_service.models.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encounters")
@RequiredArgsConstructor
public class EncounterController {

    private final EncounterService encounterService;

    @PostMapping
    public ResponseEntity<ApiResponse> createEncounter(
            @Valid @RequestBody EncounterRequest request) {

        EncounterResponse response =
                encounterService.createEncounter(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse(
                                1,
                                "Encounter created successfully",
                                response
                        )
                );
    }

    @GetMapping("/{encounterId}")
    public ResponseEntity<ApiResponse> getEncounter(
            @PathVariable Long encounterId) {

        EncounterResponse response =
                encounterService.getEncounter(encounterId);

        return ResponseEntity.ok(
                new ApiResponse(
                        1,
                        "Encounter retrieved successfully",
                        response
                )
        );
    }

    @GetMapping("/number/{encounterNumber}")
    public ResponseEntity<ApiResponse> getEncounterByNumber(
            @PathVariable String encounterNumber) {

        EncounterResponse response =
                encounterService.getEncounterByNumber(encounterNumber);

        return ResponseEntity.ok(
                new ApiResponse(
                        1,
                        "Encounter retrieved successfully",
                        response
                )
        );
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse> getPatientEncounters(
            @PathVariable Long patientId) {

        List<EncounterResponse> response =
                encounterService.getPatientEncounters(patientId);

        return ResponseEntity.ok(
                new ApiResponse(
                        1,
                        "Patient encounters retrieved successfully",
                        response
                )
        );
    }

    @PatchMapping("/{encounterId}/complete")
    public ResponseEntity<ApiResponse> completeEncounter(
            @PathVariable Long encounterId) {

        EncounterResponse response =
                encounterService.completeEncounter(encounterId);

        return ResponseEntity.ok(
                new ApiResponse(
                        1,
                        "Encounter completed successfully",
                        response
                )
        );
    }

    @PatchMapping("/{encounterId}/cancel")
    public ResponseEntity<ApiResponse> cancelEncounter(
            @PathVariable Long encounterId) {

        EncounterResponse response =
                encounterService.cancelEncounter(encounterId);

        return ResponseEntity.ok(
                new ApiResponse(
                        1,
                        "Encounter cancelled successfully",
                        response
                )
        );
    }
}
