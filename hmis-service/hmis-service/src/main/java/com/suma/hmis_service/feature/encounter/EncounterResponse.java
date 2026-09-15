package com.suma.hmis_service.feature.encounter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EncounterResponse {

    private Long id;
    private String encounterNumber;
    private Long patientId;
    private String patientMrNumber;
    private String patientName;
    private EncounterType type;
    private EncounterStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
