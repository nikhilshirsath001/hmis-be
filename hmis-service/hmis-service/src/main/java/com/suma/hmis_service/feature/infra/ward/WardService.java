package com.suma.hmis_service.feature.infra.ward;


import com.suma.hmis_service.feature.infra.bed.BedAssignRequest;
import com.suma.hmis_service.feature.infra.bed.BedRequest;
import com.suma.hmis_service.feature.infra.bed.BedResponse;
import com.suma.hmis_service.feature.infra.room.RoomRequest;
import com.suma.hmis_service.feature.infra.room.RoomResponse;
import com.suma.hmis_service.feature.infra.wardtype.WardTypeRequest;
import com.suma.hmis_service.feature.infra.wardtype.WardTypeResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface WardService {

    WardTypeResponse createWardType(WardTypeRequest request);

    List<WardTypeResponse> getWardTypes();

    WardResponse createWard(WardRequest request);

    List<WardResponse> getWards(Long wardTypeId);

    RoomResponse createRoom(RoomRequest request);

    List<RoomResponse> getRooms(Long wardId);

    BedResponse createBed(BedRequest request);

    List<BedResponse> getBeds(Long roomId);

    BedResponse assignBed(@Valid BedAssignRequest request);
}

