package com.suma.hmis_service.services.patient;

import com.suma.hmis_service.entities.ContactPerson;
import com.suma.hmis_service.entities.Patient;
import com.suma.hmis_service.models.ApiResponse;
import com.suma.hmis_service.models.patient.*;
import com.suma.hmis_service.repositories.patient.ContactPersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ContactPersonServiceImpl implements ContactPersonService{

    private  final  ContactPersonRepository contactPersonRepository;

    ContactPersonServiceImpl(ContactPersonRepository contactPersonRepository){
        this.contactPersonRepository = contactPersonRepository;
    }

    @Override
    public ApiResponse createContactPerson(CreateContactPersonDto request){

        ContactPerson contactPerson = ContactPerson.toContactPersonFromContactPersonDto(request);

        ContactPerson savedContactPerson = contactPersonRepository.save(contactPerson);

        return new ApiResponse(1, "", ContactPersonResponse.toContactPersonResponseUsingContactPerson(savedContactPerson));

    }

    @Override
    public ApiResponse getContanctPersonByAbhaId(String abhaId) {
        ContactPersonResponse contactPersonResponse = null;
        try {
            ContactPerson contactPerson = contactPersonRepository.findByAbhaId(abhaId);
            contactPersonResponse = ContactPersonResponse.toContactPersonResponseUsingContactPerson(contactPerson);
            return new ApiResponse(1, "", contactPersonResponse);
        } catch (Exception e) {
            log.error("Error occurred in get Contact Person by abhaId: {}, error: {}", abhaId, e.getMessage());
            return new ApiResponse(2, "", contactPersonResponse
            );
        }
    }


}
