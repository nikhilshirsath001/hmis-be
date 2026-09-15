package com.suma.hmis_service.feature.preauth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreAuthPayloadRepository extends JpaRepository<PreAuthPayloadEntity, Long> {
//    Optional<PreAuthPayloadEntity> findByClaimId(String claimId);
}