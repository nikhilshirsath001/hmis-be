package com.suma.hmis_service.feature.eligibility;

import com.suma.hmis_service.feature.preauth.PreAuthPayloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EligibilityPayloadRepository extends JpaRepository<EligibilityPayloadEntity, Long> {
}