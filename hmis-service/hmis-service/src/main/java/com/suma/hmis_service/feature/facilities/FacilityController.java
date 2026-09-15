package com.suma.hmis_service.feature.facilities;

import com.suma.hmis_service.models.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityCatalogService facilityCatalogService;


    @PostMapping
    public ResponseEntity<ApiResponse> createFacility(
            @RequestBody Facility facility) {
        return ResponseEntity.ok(
                new ApiResponse(
                        1,
                        "Billing facility created successfully",
                        facilityCatalogService.createFacility(facility)
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getFacilities(
            @RequestParam(required = false) FacilityType type) {

        List<Facility> facilities = type == null
                ? facilityCatalogService.getActiveFacilities()
                : facilityCatalogService.getFacilitiesByType(type);

        return ResponseEntity.ok(
                new ApiResponse(
                        1,
                        "Billing facilities retrieved successfully",
                        facilities
                )
        );
    }

    @GetMapping("/{facilityId}")
    public ResponseEntity<ApiResponse> getFacility(
            @PathVariable Long facilityId) {
        Facility facility = facilityCatalogService.getFacility(facilityId);
        return ResponseEntity.ok(
                new ApiResponse(
                        1,
                        "Billing facility retrieved successfully",
                        facility
                )
        );
    }
}

