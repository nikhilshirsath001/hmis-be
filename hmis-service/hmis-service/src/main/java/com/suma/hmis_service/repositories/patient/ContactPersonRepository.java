package com.suma.hmis_service.repositories.patient;

import com.suma.hmis_service.entities.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactPersonRepository extends JpaRepository<ContactPerson,Long> {
    ContactPerson findByAbhaId(String abhaId);
}
