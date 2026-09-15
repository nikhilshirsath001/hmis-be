package com.suma.hmis_service.feature.encounter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EncounterRepository extends JpaRepository<Encounter, Long> {

    Optional<Encounter> findByEncounterNumber(String encounterNumber);

    List<Encounter> findByPatientIdOrderByCreatedAtDesc(Long patientId);

    List<Encounter> findByPatientIdAndStatusOrderByCreatedAtDesc(
            Long patientId,
            EncounterStatus status
    );

    boolean existsByEncounterNumber(String encounterNumber);
}
