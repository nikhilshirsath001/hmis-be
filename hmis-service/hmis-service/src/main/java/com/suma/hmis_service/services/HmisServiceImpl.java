package com.suma.hmis_service.services;

import com.suma.hmis_service.clients.PatientClient;
import com.suma.hmis_service.entities.EGender;
import com.suma.hmis_service.entities.Patient;
import com.suma.hmis_service.models.ApiResponse;
import com.suma.hmis_service.models.patient.*;
import com.suma.hmis_service.repositories.PatientRepository;
import com.suma.hmis_service.services.patient.ContactPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.util.UUID;


@Slf4j
@Service
public class HmisServiceImpl implements HmisService{

    private final PatientRepository patientRepository;
    private final PatientClient patientClient;
    private final ContactPersonService contactPersonService;

    public HmisServiceImpl(PatientRepository patientRepository, PatientClient patientClient,
                           ContactPersonService contactPersonService){
        this.patientRepository= patientRepository;
        this.patientClient = patientClient;
        this.contactPersonService= contactPersonService;
    }


    public ApiResponse getPatientByAbhaId(String abhaId) {
        PatientResponse patientResponse = null;
        try {
            Patient patient = patientRepository.findByAbhaId(abhaId).orElseThrow(() -> new RuntimeException("Patient not found"));
                patientResponse = PatientResponse.toPatientResponseUsingPatient(patient);
               ApiResponse response = new ApiResponse(1, "", patientResponse);
            return response ;
        } catch (Exception e) {
            log.error("Error occurred in getPatients by abhaId: {}, error: {}", abhaId, e.getMessage());
            return new ApiResponse(2, "", patientResponse
            );
        }
    }

    public ApiResponse createPatients(CreatePatientRequest request){

        CreatePatientDto createPatientDto = this.getPatient();
        Patient patient = Patient.toPatientUsingCreatePatientDto(createPatientDto);
        patient.setAbhaId(request.getAbhaId());
        Patient savedPatient = patientRepository.save(patient);

        CreatePatientDto createPatientDto1 = this.getPatient();

        CreateContactPersonDto contactPersonDto = CreateContactPersonDto.builder()
                .name(createPatientDto1.getPatientName())
                .gender(createPatientDto1.getGender())
                .address(createPatientDto1.getAddress())
                .relation("Brother")
                .mobileNo(createPatientDto1.getMobileNumber())
                .abhaId(savedPatient.getAbhaId()).build();

        ApiResponse response =  contactPersonService.createContactPerson(contactPersonDto);

        return new ApiResponse(1, "", PatientResponse.toPatientResponseUsingPatient(savedPatient));

    }

    @Override
    public ApiResponse getContanctPersonByAbhaId(String abhaId) {
        return contactPersonService.getContanctPersonByAbhaId(abhaId);
    }


    public CreatePatientDto getPatient() {

        JsonNode user = patientClient
                .getRanddomUser()
                .path("results")
                .get(0);

        CreatePatientDto dto = new CreatePatientDto();

        dto.setPatientName(
                user.path("name").path("first").asText()
                        + " " +
                        user.path("name").path("last").asText()
        );

        dto.setGender(
                EGender.valueOf(user.path("gender").asText())
        );

        dto.setMobileNumber(
                user.path("cell").asText().replaceAll(" ","")
        );

        dto.setTown(
                user.path("location").path("city").asText()
        );

        dto.setDistrict(
                user.path("location").path("city").asText()
        );

        dto.setState(
                user.path("location").path("state").asText()
        );



        dto.setPinCode(
                user.path("location").path("postcode").asLong()
        );

        dto.setAddress(
                user.path("location").path("street").path("number").asText()
                        + " "
                        + user.path("location").path("street").path("name").asText()
        );

        dto.setDateOfBirth(
                LocalDate.parse(
                        user.path("dob").path("date").asText().substring(0, 10)
                )
        );

        dto.setPatientMrNumber(UUID.randomUUID().toString());

        return dto;
    }
}
