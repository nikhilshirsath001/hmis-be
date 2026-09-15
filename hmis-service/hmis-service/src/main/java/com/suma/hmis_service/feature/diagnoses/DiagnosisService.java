package com.suma.hmis_service.feature.diagnoses;

import com.suma.hmis_service.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;

    public List<Diagnosis> getActiveDiagnoses() {
        log.info("Fetching active diagnoses");
        return diagnosisRepository.findByActiveTrue();
    }

    public Diagnosis getDiagnosis(Long diagnosisId) {
        log.info("Fetching diagnosis diagnosisId={}", diagnosisId);

        return diagnosisRepository.findById(diagnosisId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Diagnosis not found: " + diagnosisId
                        )
                );
    }
}

