package com.suma.hmis_service.feature.infra.bed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BedRequest {

    @NotNull
    private Long roomId;

    @NotBlank
    @Size(max = 50)
    private String code;

    @NotBlank
    @Size(max = 100)
    private String name;

    private BedStatus status;

}
