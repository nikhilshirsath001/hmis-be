package com.suma.hmis_service.feature.user.useprofiles;

import com.suma.hmis_service.exceptions.ResourceNotFoundException;
import com.suma.hmis_service.feature.user.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class HealthcareProfessionalService {

    private static final Logger log =
            LoggerFactory.getLogger(HealthcareProfessionalService.class);

    private final HealthcareProfessionalRepository professionalRepository;

    public HealthcareProfessionalService(
            HealthcareProfessionalRepository professionalRepository) {
        this.professionalRepository = professionalRepository;
    }

    public HealthcareProfessionalResponse getProfessional(Long professionalId) {
        log.info("Fetching healthcare professional professionalId={}", professionalId);

        HealthcareProfessional professional =
                professionalRepository.findById(professionalId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Healthcare professional not found: " + professionalId
                                )
                        );

        return mapToResponse(professional);
    }

    public HealthcareProfessionalResponse getProfessionalByUserId(Long userId) {
        log.info("Fetching healthcare professional userId={}", userId);

        HealthcareProfessional professional =
                professionalRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Healthcare professional not found for user: " + userId
                                )
                        );

        return mapToResponse(professional);
    }

    public List<HealthcareProfessionalResponse> getProfessionals(Role role) {
        log.info("Fetching healthcare professionals role={}", role);

        List<HealthcareProfessional> professionals =
                role == null
                        ? professionalRepository.findByActiveTrue()
                        : professionalRepository.findByUserRoleAndActiveTrue(role);

        return professionals.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private HealthcareProfessionalResponse mapToResponse(
            HealthcareProfessional professional) {

        return new HealthcareProfessionalResponse(
                professional.getId(),
                professional.getUser().getId(),
                professional.getHealthcareProfessionId(),
                professional.getUser().getRole(),
                professional.getDesignation(),
                professional.getContactNumber(),
                professional.getQualification(),
                professional.getSpecialization(),
                professional.isActive(),
                professional.getCreatedAt()
        );
    }
}

