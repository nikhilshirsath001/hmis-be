package com.suma.hmis_service.feature.insurance;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsurancePolicyRepository
        extends JpaRepository<InsurancePolicy, Long> {

    List<InsurancePolicy> findByPatientIdAndActiveTrue(Long patientId);
}

