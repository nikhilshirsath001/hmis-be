package com.suma.hmis_service.services.patient;

import com.suma.hmis_service.models.ApiResponse;
import com.suma.hmis_service.models.patient.ContactPersonResponse;
import com.suma.hmis_service.models.patient.CreateContactPersonDto;
import com.suma.hmis_service.models.patient.CreatePatientDto;

public interface ContactPersonService {
    public  ApiResponse getContanctPersonByAbhaId(String abhaId);

    public ApiResponse createContactPerson(CreateContactPersonDto createContactPersonDto);
}
