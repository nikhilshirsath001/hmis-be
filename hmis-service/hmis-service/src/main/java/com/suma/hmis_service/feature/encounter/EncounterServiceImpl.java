package com.suma.hmis_service.feature.encounter;

import com.suma.hmis_service.entities.Patient;
import com.suma.hmis_service.exceptions.ResourceNotFoundException;
import com.suma.hmis_service.repositories.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EncounterServiceImpl implements EncounterService {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    private final EncounterRepository encounterRepository;
    private final PatientRepository patientRepository;

    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    @Override
    public EncounterResponse createEncounter(EncounterRequest request) {

        log.info(
                "Creating encounter patientId={} type={}",
                request.getPatientId(),
                request.getType()
        );

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found: " + request.getPatientId()
                        )
                );

        Encounter encounter = new Encounter();
        encounter.setPatient(patient);
        encounter.setType(request.getType());
        encounter.setStatus(EncounterStatus.ACTIVE);
        encounter.setEncounterNumber(
                generateEncounterNumber(request.getType())
        );

        Encounter savedEncounter = encounterRepository.save(encounter);

        log.info(
                "Encounter created successfully encounterId={} encounterNumber={} patientId={} type={}",
                savedEncounter.getId(),
                savedEncounter.getEncounterNumber(),
                patient.getId(),
                savedEncounter.getType()
        );

        return mapToResponse(savedEncounter);
    }

    @Override
    @Transactional(readOnly = true)
    public EncounterResponse getEncounter(Long encounterId) {

        log.info("Fetching encounter encounterId={}", encounterId);

        Encounter encounter = encounterRepository.findById(encounterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Encounter not found: " + encounterId
                        )
                );

        return mapToResponse(encounter);
    }

    @Override
    @Transactional(readOnly = true)
    public EncounterResponse getEncounterByNumber(String encounterNumber) {

        log.info(
                "Fetching encounter encounterNumber={}",
                encounterNumber
        );

        Encounter encounter =
                encounterRepository.findByEncounterNumber(encounterNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Encounter not found: "
                                                + encounterNumber
                                )
                        );

        return mapToResponse(encounter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EncounterResponse> getPatientEncounters(Long patientId) {

        log.info(
                "Fetching encounters patientId={}",
                patientId
        );

        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(
                    "Patient not found: " + patientId
            );
        }

        return encounterRepository
                .findByPatientIdOrderByCreatedAtDesc(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public EncounterResponse completeEncounter(Long encounterId) {

        log.info(
                "Completing encounter encounterId={}",
                encounterId
        );

        Encounter encounter = getEntity(encounterId);

        if (encounter.getStatus() != EncounterStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Only active encounters can be completed"
            );
        }

        encounter.setStatus(EncounterStatus.COMPLETED);

        Encounter savedEncounter = encounterRepository.save(encounter);

        log.info(
                "Encounter completed successfully encounterId={} encounterNumber={}",
                savedEncounter.getId(),
                savedEncounter.getEncounterNumber()
        );

        return mapToResponse(savedEncounter);
    }

    @Override
    public EncounterResponse cancelEncounter(Long encounterId) {

        log.info(
                "Cancelling encounter encounterId={}",
                encounterId
        );

        Encounter encounter = getEntity(encounterId);

        if (encounter.getStatus() != EncounterStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Only active encounters can be cancelled"
            );
        }

        encounter.setStatus(EncounterStatus.CANCELLED);

        Encounter savedEncounter = encounterRepository.save(encounter);

        log.info(
                "Encounter cancelled successfully encounterId={} encounterNumber={}",
                savedEncounter.getId(),
                savedEncounter.getEncounterNumber()
        );

        return mapToResponse(savedEncounter);
    }

    private Encounter getEntity(Long encounterId) {
        return encounterRepository.findById(encounterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Encounter not found: " + encounterId
                        )
                );
    }

    private String generateEncounterNumber(EncounterType type) {

        String prefix = type == EncounterType.OPD ? "OPD" : "IPD";
        String date = LocalDate.now().format(DATE_FORMATTER);

        int sequence = SEQUENCE.updateAndGet(
                current -> current >= 999999 ? 1 : current + 1
        );

        String encounterNumber = String.format(
                "%s-%s-%06d",
                prefix,
                date,
                sequence
        );

        while (encounterRepository.existsByEncounterNumber(
                encounterNumber)) {

            sequence = SEQUENCE.updateAndGet(
                    current -> current >= 999999 ? 1 : current + 1
            );

            encounterNumber = String.format(
                    "%s-%s-%06d",
                    prefix,
                    date,
                    sequence
            );
        }

        return encounterNumber;
    }

    private EncounterResponse mapToResponse(Encounter encounter) {

        Patient patient = encounter.getPatient();

        return new EncounterResponse(
                encounter.getId(),
                encounter.getEncounterNumber(),
                patient.getId(),
                patient.getPatientMrNumber(),
                patient.getPatientName(),
                encounter.getType(),
                encounter.getStatus(),
                encounter.getCreatedAt(),
                encounter.getUpdatedAt()
        );
    }
}
