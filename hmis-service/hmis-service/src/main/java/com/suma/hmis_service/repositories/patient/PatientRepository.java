package com.suma.hmis_service.repositories.patient;

import com.suma.hmis_service.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {
    @Query(value = "SELECT * FROM patient WHERE abha_id = :abhaId", nativeQuery = true)
    Optional<Patient> findByAbhaId(@Param("abhaId") String abhaId);

}
