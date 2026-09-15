package com.suma.hmis_service.feature.user;


import com.suma.hmis_service.feature.user.useprofiles.HealthcareProfessionalResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String username;
    private String email;
    private Role role;
    private boolean active;
    private HealthcareProfessionalResponse professionalDetails;
    private LocalDateTime createdAt;
}

