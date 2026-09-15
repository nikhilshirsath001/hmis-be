package com.suma.hmis_service.feature.infra.bed;

import com.suma.hmis_service.feature.infra.ward.Ward;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BedResponse {
    private Long id;
    private Long roomId;
    private String roomCode;
    private String roomName;
    private Long wardId;
    private String wardCode;
    private String wardName;
//    private Ward ward;
    private String code;
    private String name;
    private BedStatus status;
    private Boolean active;
}
