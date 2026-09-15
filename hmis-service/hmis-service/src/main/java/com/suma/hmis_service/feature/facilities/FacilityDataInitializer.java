package com.suma.hmis_service.feature.facilities;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.suma.hmis_service.feature.facilities.FacilityType.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class FacilityDataInitializer implements CommandLineRunner {

    private final FacilityRepository facilityRepository;

    @Override
    public void run(String... args) {

        if (facilityRepository.count() > 0) {
            return;
        }

        createFacility("CONS001", "Specialist Consultation", CONSULTATION, "500");

        createFacility("CONS002", "General Physician Consultation", CONSULTATION, "300");

        createFacility("CONS003", "Follow-up Consultation", CONSULTATION, "250");

        createFacility("LAB001", "Complete Blood Count", INVESTIGATION, "300");

        createFacility("LAB002", "Liver Function Test", INVESTIGATION, "600");

        createFacility("LAB003", "Kidney Function Test", INVESTIGATION, "500");

        createFacility("IMG001", "Chest X-Ray", INVESTIGATION, "800");

        createFacility("IMG002", "CT Scan", INVESTIGATION, "3500");

        createFacility("IMG003", "MRI Scan", INVESTIGATION, "6000");

        createFacility("OT001", "Minor Operation Theatre", OPERATION_THEATRE, "5000");

        createFacility("OT002", "Major Operation Theatre", OPERATION_THEATRE, "25000");

        createFacility("OT003", "Emergency Operation Theatre", OPERATION_THEATRE, "35000");

        createFacility("OTH001", "Room Charges", OTHER, "2000");

        createFacility("OTH002", "Nursing Charges", OTHER, "1000");

        createFacility("OTH003", "Medical Supplies", OTHER, "1500");

        log.info("Dummy billing facility data initialized");
    }

    private void createFacility(String code, String description, FacilityType type, String amount) {

        Facility facility = new Facility();

        facility.setCode(code);
        facility.setDescription(description);
        facility.setType(type);
        facility.setAmount(new BigDecimal(amount));
        facility.setActive(true);

        facilityRepository.save(facility);
    }
}

