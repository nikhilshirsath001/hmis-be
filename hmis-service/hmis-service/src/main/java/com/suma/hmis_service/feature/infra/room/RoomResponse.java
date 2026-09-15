package com.suma.hmis_service.feature.infra.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class RoomResponse {

    private Long id;
    private Long wardId;
    private String wardCode;
    private String wardName;
    private String code;
    private String name;
    private String description;
    private Boolean active;
}
