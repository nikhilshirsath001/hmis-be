package com.suma.hmis_service.feature.infra.ward;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WardResponse {
    private Long id;
    private Long wardTypeId;
    private String wardTypeCode;
    private String wardTypeName;
    private String code;
    private String name;
    private String description;
    private Boolean active;
}
