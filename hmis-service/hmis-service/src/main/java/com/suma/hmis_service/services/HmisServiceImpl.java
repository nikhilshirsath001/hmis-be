package com.suma.hmis_service.services;

import com.suma.hmis_service.entities.Patient;
import com.suma.hmis_service.models.ApiResponse;
import com.suma.hmis_service.models.patient.PatientResponse;
import com.suma.hmis_service.repositories.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class HmisServiceImpl implements HmisService{

    private final PatientRepository patientRepository;

    public HmisServiceImpl(PatientRepository patientRepository ){
        this.patientRepository= patientRepository;
    }


    public ApiResponse getPatientByAbhaId(String abhaId) {
        PatientResponse patientResponse = null;
        try {
            Patient patient = patientRepository.findByAbhaId(abhaId).orElseThrow(() -> new RuntimeException("Patient not found"));
                patientResponse = PatientResponse.toPatientResponseUsingPatient(patient);
            return new ApiResponse(1, "", patientResponse);
        } catch (Exception e) {
            log.error("Error occurred in getPatients by abhaId: {}, error: {}", abhaId, e.getMessage());
            return new ApiResponse(2, "", patientResponse
            );
        }
    }
}
