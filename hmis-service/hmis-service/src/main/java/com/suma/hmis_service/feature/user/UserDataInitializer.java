package com.suma.hmis_service.feature.user;

import com.suma.hmis_service.feature.user.useprofiles.HealthcareProfessional;
import com.suma.hmis_service.feature.user.useprofiles.HealthcareProfessionalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserDataInitializer implements CommandLineRunner {

    private static final Logger log =
            LoggerFactory.getLogger(UserDataInitializer.class);

    private final UserRepository userRepository;
    private final HealthcareProfessionalRepository professionalRepository;

    public UserDataInitializer(
            UserRepository userRepository,
            HealthcareProfessionalRepository professionalRepository) {
        this.userRepository = userRepository;
        this.professionalRepository = professionalRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        createProfessional(
                "Dr. Amit Sharma",
                "amit.sharma",
                "amit.sharma@healthcare.com",
                Role.DOCTOR,
                "HP10001",
                "Cardiologist",
                "9876543210",
                "MBBS, MD Cardiology",
                "Cardiology"
        );

        createProfessional(
                "Dr. Priya Patel",
                "priya.patel",
                "priya.patel@healthcare.com",
                Role.DOCTOR,
                "HP10002",
                "General Physician",
                "9876543211",
                "MBBS, MD Medicine",
                "General Medicine"
        );

        createProfessional(
                "Dr. Rahul Mehta",
                "rahul.mehta",
                "rahul.mehta@healthcare.com",
                Role.DOCTOR,
                "HP10003",
                "Orthopaedic Surgeon",
                "9876543212",
                "MBBS, MS Orthopaedics",
                "Orthopaedics"
        );

        createProfessional(
                "Priya Singh",
                "priya.singh",
                "priya.singh@healthcare.com",
                Role.NURSE,
                "HP20001",
                "Registered Nurse",
                "9876543213",
                "B.Sc Nursing",
                "Critical Care"
        );

        createProfessional(
                "Anita Verma",
                "anita.verma",
                "anita.verma@healthcare.com",
                Role.NURSE,
                "HP20002",
                "Registered Nurse",
                "9876543214",
                "B.Sc Nursing",
                "General Ward"
        );
    }

    private void createProfessional(
            String name,
            String username,
            String email,
            Role role,
            String healthcareProfessionId,
            String designation,
            String contactNumber,
            String qualification,
            String specialization) {

        if (userRepository.existsByUsername(username)) {
            return;
        }

        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(role);
        user.setActive(true);

        User savedUser = userRepository.save(user);

        HealthcareProfessional professional =
                new HealthcareProfessional();

        professional.setUser(savedUser);
        professional.setHealthcareProfessionId(
                healthcareProfessionId
        );
        professional.setDesignation(designation);
        professional.setContactNumber(contactNumber);
        professional.setQualification(qualification);
        professional.setSpecialization(specialization);
        professional.setActive(true);

        professionalRepository.save(professional);

        log.info(
                "Healthcare professional initialized userId={} role={} professionId={}",
                savedUser.getId(),
                role,
                healthcareProfessionId
        );
    }
}

