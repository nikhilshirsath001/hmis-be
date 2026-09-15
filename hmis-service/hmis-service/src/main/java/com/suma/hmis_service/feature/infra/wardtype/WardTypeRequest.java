package com.suma.hmis_service.feature.infra.wardtype;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WardTypeRequest {

    @NotBlank
    @Size(max = 50)
    private String code;

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String description;
}


