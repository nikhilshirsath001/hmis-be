package com.suma.hmis_service.repositories;

import com.suma.hmis_service.entities.PatientAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<PatientAttachment, String> {

    Optional<List<PatientAttachment>> findByPatientId(Long patientId);

    Optional<PatientAttachment> findByIdAndPatientId(String attachmentId, Long patientId);

    Optional<PatientAttachment> findByPatientIdAndId(Long patientId, String attachmentId);
}
