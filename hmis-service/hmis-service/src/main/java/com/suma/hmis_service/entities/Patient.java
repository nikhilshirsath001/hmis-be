package com.suma.hmis_service.entities;

import com.suma.hmis_service.models.patient.CreatePatientDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "patient")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "abha_id")
    private String abhaId;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private EGender gender;

    @Column(name = "is_new_born")
    private boolean isNewBorn;

    @Column(name = "patient_mr_number")
    private String patientMrNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;



    @Column(name = "address")
    private String address;

    @Column(name = "town")
    private String town;

    @Column(name = "district")
    private String district;

    @Column(name = "state")
    private String state;

    @Column(name = "pin_code")
    private Long pinCode;


    public static Patient toPatientUsingCreatePatientDto(CreatePatientDto obj) {
        return Patient.builder()
                .patientName(obj.getPatientName())
                .mobileNumber(obj.getMobileNumber())
                .gender(obj.getGender())
                .isNewBorn(obj.isNewBorn())
                .patientMrNumber(obj.getPatientMrNumber())
                .dateOfBirth(obj.getDateOfBirth())
                .address(obj.getAddress())
                .town(obj.getTown())
                .district(obj.getDistrict())
                .state(obj.getState())
                .pinCode(obj.getPinCode())
                .build();
    }
}
