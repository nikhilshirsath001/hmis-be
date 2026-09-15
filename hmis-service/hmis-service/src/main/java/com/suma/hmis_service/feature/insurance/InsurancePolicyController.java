package com.suma.hmis_service.feature.insurance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Slf4j
public class InsurancePolicyController {

    private final InsurancePolicyRepository insurancePolicyRepository;

    @GetMapping("/{patientId}/insurance")
    public ResponseEntity<List<InsurancePolicy>> getPatientInsurance(
            @PathVariable Long patientId) {

        log.info("Fetching insurance policies patientId={}", patientId);

        List<InsurancePolicy> policies =
                insurancePolicyRepository
                        .findByPatientIdAndActiveTrue(patientId);

        return ResponseEntity.ok(policies);
    }
}

