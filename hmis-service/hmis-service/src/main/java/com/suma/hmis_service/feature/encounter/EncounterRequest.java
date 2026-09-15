package com.suma.hmis_service.feature.encounter;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EncounterRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Encounter type is required")
    private EncounterType type;
}
