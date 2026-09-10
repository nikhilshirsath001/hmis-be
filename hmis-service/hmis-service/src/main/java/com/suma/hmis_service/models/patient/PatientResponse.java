package com.suma.hmis_service.models.patient;

import com.suma.hmis_service.entities.EGender;
import com.suma.hmis_service.entities.Patient;
import com.suma.hmis_service.entities.PatientAttachment;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponse {

    private String abhaId;

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
    private List<PatientAttachment> attachments = new ArrayList<>();

    public static PatientResponse toPatientResponseUsingPatient(Patient obj) {
        return PatientResponse.builder()
                .abhaId(obj.getAbhaId())
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
