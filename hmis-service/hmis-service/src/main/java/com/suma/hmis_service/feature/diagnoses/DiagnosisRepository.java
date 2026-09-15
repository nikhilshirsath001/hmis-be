package com.suma.hmis_service.feature.diagnoses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    Optional<Diagnosis> findByCode(String code);

    List<Diagnosis> findByActiveTrue();

    boolean existsByCode(String code);
}

