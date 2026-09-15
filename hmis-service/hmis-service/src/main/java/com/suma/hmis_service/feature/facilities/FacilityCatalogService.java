package com.suma.hmis_service.feature.facilities;

import com.suma.hmis_service.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FacilityCatalogService {

    private final FacilityRepository facilityRepository;


    @Transactional
    public Facility createFacility(Facility facility) {

        log.info(
                "Creating billing facility code={} type={}",
                facility.getCode(),
                facility.getType()
        );

        if (facilityRepository.existsByCode(facility.getCode())) {
            throw new IllegalArgumentException(
                    "Billing facility already exists: " + facility.getCode()
            );
        }

        facility.setActive(true);

        return facilityRepository.save(facility);
    }


    public List<Facility> getActiveFacilities() {
        log.info("Fetching active billing facilitys");
        return facilityRepository.findByActiveTrue();
    }

    public List<Facility> getFacilitiesByType(FacilityType type) {
        log.info("Fetching billing facilitys type={}", type);
        return facilityRepository.findByTypeAndActiveTrue(type);
    }

    public Facility getFacility(Long facilityId) {
        log.info("Fetching billing facility facilityId={}", facilityId);

        return facilityRepository.findById(facilityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Billing facility not found: " + facilityId
                        )
                );
    }
}


