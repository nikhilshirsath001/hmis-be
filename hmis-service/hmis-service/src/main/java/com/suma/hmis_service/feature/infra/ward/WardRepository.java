package com.suma.hmis_service.feature.infra.ward;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {

    boolean existsByCodeIgnoreCase(String code);

    boolean existsByNameIgnoreCase(String name);

    List<Ward> findByWardTypeIdAndActiveTrue(Long wardTypeId);

    List<Ward> findByActiveTrue();
}

