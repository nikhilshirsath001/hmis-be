package com.suma.hmis_service.feature.user;

import com.suma.hmis_service.feature.user.useprofiles.HealthcareProfessionalResponse;
import com.suma.hmis_service.feature.user.useprofiles.HealthcareProfessionalService;
import com.suma.hmis_service.models.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final HealthcareProfessionalService professionalService;


    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse(1,
                        "User created successfully",
                        response
                )
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable Long userId) {
        UserResponse response = userService.getUser(userId);
        return ResponseEntity.ok(
                new ApiResponse(1,
                        "User retrieved successfully",
                        response
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getUsers(
            @RequestParam(required = false) Role role) {

        List<UserResponse> response = userService.getUsers(role);
        return ResponseEntity.ok(
                new ApiResponse(1,
                        "Users retrieved successfully",
                        response
                )
        );
    }

    @GetMapping("/professionals")
    public ResponseEntity<ApiResponse> getProfessionals(
            @RequestParam(required = false) Role role) {

        List<HealthcareProfessionalResponse> response =
                professionalService.getProfessionals(role);

        return ResponseEntity.ok(
                new ApiResponse(1,
                        "Healthcare professionals retrieved successfully",
                        response
                )
        );
    }

    @GetMapping("/professionals/{professionalId}")
    public ResponseEntity<ApiResponse> getProfessional(@PathVariable Long professionalId) {
        HealthcareProfessionalResponse response = professionalService.getProfessional(professionalId);

        return ResponseEntity.ok(
                new ApiResponse(1,
                        "Healthcare professional retrieved successfully",
                        response
                )
        );
    }
}

