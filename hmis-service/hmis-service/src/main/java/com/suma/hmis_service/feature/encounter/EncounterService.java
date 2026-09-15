package com.suma.hmis_service.feature.encounter;

import java.util.List;

public interface EncounterService {

    EncounterResponse createEncounter(EncounterRequest request);

    EncounterResponse getEncounter(Long encounterId);

    EncounterResponse getEncounterByNumber(String encounterNumber);

    List<EncounterResponse> getPatientEncounters(Long patientId);

    EncounterResponse completeEncounter(Long encounterId);

    EncounterResponse cancelEncounter(Long encounterId);
}
