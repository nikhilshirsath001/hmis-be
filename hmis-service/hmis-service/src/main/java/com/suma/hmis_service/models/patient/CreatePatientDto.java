package com.suma.hmis_service.models.patient;

import com.suma.hmis_service.entities.EGender;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePatientDto {

    private String patientName;

    private String mobileNumber;

    private EGender gender;

    private boolean isNewBorn;

    private String patientMrNumber;

    private LocalDate dateOfBirth;


    private String address;

    private String town;

    private String district;

    private String state;

    private Long pinCode;

}
