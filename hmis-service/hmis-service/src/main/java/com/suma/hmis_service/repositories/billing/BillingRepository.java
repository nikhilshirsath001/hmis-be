package com.suma.hmis_service.repositories.billing;

import com.suma.hmis_service.entities.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing,Long> {
    Optional<Billing> findByClaimId(String claimId);

    Optional<Billing> findByPatientId(String patientId);
}
