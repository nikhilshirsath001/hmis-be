package com.suma.hmis_service.feature.user;

import com.suma.hmis_service.exceptions.ResourceNotFoundException;
import com.suma.hmis_service.feature.user.useprofiles.HealthcareProfessional;
import com.suma.hmis_service.feature.user.useprofiles.HealthcareProfessionalRepository;
import com.suma.hmis_service.feature.user.useprofiles.HealthcareProfessionalResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final HealthcareProfessionalRepository professionalRepository;
    private final ModelMapper modelMapper;


    public UserResponse createUser(CreateUserRequest request) {
        log.info(
                "Creating user username={} role={}",
                request.username(),
                request.role()
        );

        validateUserUniqueness(request);
        validateProfessionalDetails(request);
//        User mappedUser = modelMapper.map(request, User.class);
        User user = new User();
        user.setName(request.name().trim());
        user.setUsername(request.username().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setRole(request.role());
        user.setActive(true);

        User savedUser = userRepository.save(user);

        if (request.professionalDetails() != null) {
            createProfessional(savedUser, request);
        }

        log.info(
                "User created successfully userId={} role={}",
                savedUser.getId(),
                savedUser.getRole()
        );

        return mapToResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Long userId) {
        log.info("Fetching user userId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + userId
                        )
                );
//        modelMapper.map(user,UserResponse.class);
        return mapToResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers(Role role) {
        log.info("Fetching users role={}", role);

        List<User> users = role == null
                ? userRepository.findAll()
                : userRepository.findByRoleAndActiveTrue(role);

        return users.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void validateUserUniqueness(CreateUserRequest request) {
        String username = request.username().trim();
        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    private void validateProfessionalDetails(CreateUserRequest request) {
        if (request.professionalDetails() == null) {
            return;
        }

        if (!isProfessionalRole(request.role())) {
            throw new IllegalArgumentException(
                    "Professional details are not allowed for role: "
                            + request.role()
            );
        }

        String professionId =
                request.professionalDetails()
                        .healthcareProfessionId()
                        .trim();

        if (professionalRepository.existsByHealthcareProfessionId(professionId)) {
            throw new IllegalArgumentException(
                    "Healthcare profession ID already exists"
            );
        }
    }

    private void createProfessional(
            User user,
            CreateUserRequest request) {

        var details = request.professionalDetails();

        HealthcareProfessional professional =
                new HealthcareProfessional();

        professional.setUser(user);
        professional.setHealthcareProfessionId(
                details.healthcareProfessionId().trim()
        );
        professional.setDesignation(
                details.designation().trim()
        );
        professional.setContactNumber(
                details.contactNumber().trim()
        );
        professional.setQualification(
                details.qualification().trim()
        );
        professional.setSpecialization(
                normalize(details.specialization())
        );
        professional.setActive(true);

        professionalRepository.save(professional);

        log.info(
                "Healthcare professional created userId={} professionId={}",
                user.getId(),
                details.healthcareProfessionId()
        );
    }

    private String normalize(String value) {
        return value == null || value.isBlank()
                ? null
                : value.trim();
    }

    private boolean isProfessionalRole(Role role) {
        return role == Role.DOCTOR
                || role == Role.NURSE
                || role == Role.PHARMACIST
                || role == Role.LAB_TECHNICIAN;
    }

    private UserResponse mapToResponse(User user) {
        HealthcareProfessionalResponse professionalResponse = null;

        if (isProfessionalRole(user.getRole())) {
            professionalResponse =
                    professionalRepository.findByUserId(user.getId())
                            .map(this::mapProfessionalResponse)
                            .orElse(null);
        }

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                professionalResponse,
                user.getCreatedAt()
        );
    }

    private HealthcareProfessionalResponse mapProfessionalResponse(
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
