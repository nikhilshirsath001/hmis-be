package com.suma.hmis_service.feature.user.useprofiles;

import com.suma.hmis_service.feature.user.Role;

import java.time.LocalDateTime;

public record HealthcareProfessionalResponse(
        Long id,
        Long userId,
        String healthcareProfessionId,
        Role role,
        String designation,
        String contactNumber,
        String qualification,
        String specialization,
        boolean active,
        LocalDateTime createdAt
) {
}
