package com.suma.hmis_service.feature.infra.wardtype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WardTypeRepository extends JpaRepository<WardType, Long> {

    boolean existsByCodeIgnoreCase(String code);

    boolean existsByNameIgnoreCase(String name);

    Optional<WardType> findByCodeIgnoreCase(String code);
}

