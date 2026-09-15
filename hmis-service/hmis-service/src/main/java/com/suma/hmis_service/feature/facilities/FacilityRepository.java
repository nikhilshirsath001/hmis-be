package com.suma.hmis_service.feature.facilities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

    List<Facility> findByActiveTrue();

    List<Facility> findByTypeAndActiveTrue(FacilityType type);

    Optional<Facility> findByCode(String code);

    boolean existsByCode(String code);
}

