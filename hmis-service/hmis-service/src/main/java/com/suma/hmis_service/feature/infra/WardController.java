package com.suma.hmis_service.feature.infra;

import com.suma.hmis_service.feature.infra.bed.BedAssignRequest;
import com.suma.hmis_service.feature.infra.bed.BedRequest;
import com.suma.hmis_service.feature.infra.bed.BedResponse;
import com.suma.hmis_service.feature.infra.room.RoomRequest;
import com.suma.hmis_service.feature.infra.room.RoomResponse;
import com.suma.hmis_service.feature.infra.ward.*;
import com.suma.hmis_service.feature.infra.wardtype.WardTypeRequest;
import com.suma.hmis_service.feature.infra.wardtype.WardTypeResponse;
import com.suma.hmis_service.models.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wards")
@RequiredArgsConstructor
public class WardController {

    private final WardService wardService;

    @PostMapping("/types")
    public ResponseEntity<ApiResponse> createWardType(
            @Valid @RequestBody WardTypeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(1,"Ward created successful.",wardService.createWardType(request)));
    }

    @GetMapping("/types")
    public ResponseEntity<ApiResponse> getWardTypes() {
        return ResponseEntity.ok(new ApiResponse(1,"Got word by type.",wardService.getWardTypes()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createWard(
            @Valid @RequestBody WardRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(1,"Ward created successful.",wardService.createWard(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getWards(
            @RequestParam(required = false) Long wardTypeId) {
        return ResponseEntity.ok(new ApiResponse(1,"Got all wards.",wardService.getWards(wardTypeId)));
    }

    @PostMapping("/rooms")
    public ResponseEntity<ApiResponse> createRoom(
            @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(1,"Room created successful.",wardService.createRoom(request)));
    }

    @GetMapping("/{wardId}/rooms")
    public ResponseEntity<ApiResponse> getRooms(
            @PathVariable Long wardId) {
        return ResponseEntity.ok(new ApiResponse(1,"Got the room.",wardService.getRooms(wardId)));
    }

    @PostMapping("/rooms/beds")
    public ResponseEntity<ApiResponse> createBed(
            @Valid @RequestBody BedRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(1,"Bed created successful.",wardService.createBed(request)));
    }

    @GetMapping("/rooms/{roomId}/beds")
    public ResponseEntity<ApiResponse> getBeds(
            @PathVariable Long roomId) {
        return ResponseEntity.ok(new ApiResponse(1,"Got the rooms",wardService.getBeds(roomId)));
    }

    @PostMapping("/beds/assign")
    public ResponseEntity<ApiResponse> assignBed(
            @Valid @RequestBody BedAssignRequest request) {

        return ResponseEntity.ok(new ApiResponse(1,"Bed assign successful.",wardService.assignBed(request)));
    }
}

