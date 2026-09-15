package com.suma.hmis_service.feature.user.useprofiles;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record HealthcareProfessionalRequest(

        @NotBlank(message = "Healthcare profession ID is required")
        @Size(max = 50, message = "Healthcare profession ID must not exceed 50 characters")
        String healthcareProfessionId,

        @NotBlank(message = "Designation is required")
        @Size(max = 100, message = "Designation must not exceed 100 characters")
        String designation,

        @NotBlank(message = "Contact number is required")
        @Pattern(
                regexp = "^[0-9]{10}$",
                message = "Contact number must contain exactly 10 digits"
        )
        String contactNumber,

        @NotBlank(message = "Qualification is required")
        @Size(max = 200, message = "Qualification must not exceed 200 characters")
        String qualification,

        @Size(max = 100, message = "Specialization must not exceed 100 characters")
        String specialization
) {
}

