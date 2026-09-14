package com.suma.hmis_service.repositories;

import com.suma.hmis_service.entities.Patient;
import com.suma.hmis_service.entities.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<Policy,Long>{

     List <Policy> findByPatient(Patient patient);

}
