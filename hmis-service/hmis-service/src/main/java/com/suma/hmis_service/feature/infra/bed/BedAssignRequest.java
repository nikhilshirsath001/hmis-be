package com.suma.hmis_service.feature.infra.bed;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BedAssignRequest {

    @NotNull
    private Long bedId;

    @NotNull
    private Long patientId;
}

