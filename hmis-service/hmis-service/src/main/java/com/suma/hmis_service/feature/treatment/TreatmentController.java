package com.suma.hmis_service.feature.treatment;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatments")
@RequiredArgsConstructor
public class TreatmentController {

    private final TreatmentService treatmentService;

    @PostMapping
    public ResponseEntity<TreatmentResponse> createTreatment(
            @RequestBody TreatmentRequest request
    ) {

        TreatmentResponse response =
                treatmentService.createTreatment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreatmentResponse> getTreatmentById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                treatmentService.getTreatmentById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<TreatmentResponse>> getAllTreatments() {

        return ResponseEntity.ok(
                treatmentService.getAllTreatments()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreatmentResponse> updateTreatment(
            @PathVariable Long id,
            @RequestBody TreatmentRequest request
    ) {

        return ResponseEntity.ok(
                treatmentService.updateTreatment(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTreatment(
            @PathVariable Long id
    ) {

        treatmentService.deleteTreatment(id);

        return ResponseEntity.noContent().build();
    }
}

