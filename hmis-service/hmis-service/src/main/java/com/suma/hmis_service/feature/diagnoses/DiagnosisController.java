package com.suma.hmis_service.feature.diagnoses;

import com.suma.hmis_service.models.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagnoses")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @GetMapping
    public ResponseEntity<ApiResponse> getDiagnoses() {

        List<Diagnosis> activeDiagnoses = diagnosisService.getActiveDiagnoses();
        return ResponseEntity.ok(
                new ApiResponse(
                        1,
                        "Diagnoses retrieved successfully",
                        activeDiagnoses
                )
        );
    }

    @GetMapping("/{diagnosisId}")
    public ResponseEntity<ApiResponse> getDiagnosis(
            @PathVariable Long diagnosisId) {
        Diagnosis diagnosis = diagnosisService.getDiagnosis(diagnosisId);
        return ResponseEntity.ok(
                new ApiResponse(1,
                        "Diagnosis retrieved successfully",
                        diagnosis
                )
        );
    }
}

