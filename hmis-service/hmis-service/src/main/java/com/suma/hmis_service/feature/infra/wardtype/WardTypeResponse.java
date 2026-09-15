package com.suma.hmis_service.feature.infra.wardtype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WardTypeResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Boolean active;
}
