package com.suma.hmis_service.feature.user.useprofiles;


import com.suma.hmis_service.feature.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthcareProfessionalRepository extends JpaRepository<HealthcareProfessional, Long> {

    Optional<HealthcareProfessional> findByUserId(Long userId);

    Optional<HealthcareProfessional> findByHealthcareProfessionId(String healthcareProfessionId);

    List<HealthcareProfessional> findByActiveTrue();

    List<HealthcareProfessional> findByUserRoleAndActiveTrue(Role role);

    boolean existsByUserId(Long userId);

    boolean existsByHealthcareProfessionId(String healthcareProfessionId);
}
