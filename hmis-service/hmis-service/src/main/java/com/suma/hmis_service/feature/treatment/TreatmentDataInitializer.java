package com.suma.hmis_service.feature.treatment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class TreatmentDataInitializer implements CommandLineRunner {

    private final TreatmentRepository treatmentRepository;

    @Override
    public void run(String... args) {

        if (treatmentRepository.count() > 0) {
            return;
        }

        createTreatment(
                "Hypertension Management",
                "Treatment plan for controlling high blood pressure",
                "Hypertension",
                "Amlodipine",
                "5 mg once daily",
                LocalDate.now(),
                null,
                "ACTIVE",
                "PKG-GEN-001",
                "Hypertension Care Package",
                "OPD",
                LocalDate.now(),
                1
        );

        createTreatment(
                "Type 2 Diabetes Management",
                "Blood glucose management for type 2 diabetes",
                "Type 2 Diabetes Mellitus",
                "Metformin",
                "500 mg twice daily",
                LocalDate.now(),
                null,
                "ACTIVE",
                "PKG-GEN-002",
                "Diabetes Management Package",
                "OPD",
                LocalDate.now(),
                1
        );

        createTreatment(
                "Coronary Artery Disease Treatment",
                "Medical management of coronary artery disease",
                "Coronary Artery Disease",
                "Atorvastatin",
                "40 mg once daily",
                LocalDate.now(),
                null,
                "ACTIVE",
                "PKG-CARD-001",
                "Cardiac Care Package",
                "IPD",
                LocalDate.now().plusDays(1),
                5
        );

        createTreatment(
                "Appendicitis Treatment",
                "Treatment and post-operative management of acute appendicitis",
                "Acute Appendicitis",
                "Amoxicillin-Clavulanate",
                "625 mg three times daily",
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                "COMPLETED",
                "PKG-SURG-001",
                "Appendectomy Package",
                "IPD",
                LocalDate.now(),
                4
        );

        createTreatment(
                "Pneumonia Treatment",
                "Antibiotic treatment for bacterial pneumonia",
                "Pneumonia",
                "Azithromycin",
                "500 mg once daily",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                "ACTIVE",
                "PKG-PULM-001",
                "Pneumonia Treatment Package",
                "IPD",
                LocalDate.now(),
                5
        );

        createTreatment(
                "Femur Fracture Management",
                "Treatment and rehabilitation following femur fracture",
                "Fracture of Femur",
                "Paracetamol",
                "650 mg every 6 hours",
                LocalDate.now(),
                LocalDate.now().plusDays(14),
                "ACTIVE",
                "PKG-ORTHO-001",
                "Femur Fracture Treatment Package",
                "IPD",
                LocalDate.now().plusDays(1),
                14
        );

        createTreatment(
                "Gallstone Management",
                "Management of symptomatic gallstones",
                "Cholelithiasis",
                "Ursodeoxycholic Acid",
                "300 mg twice daily",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                "ACTIVE",
                "PKG-GI-001",
                "Gallstone Management Package",
                "OPD",
                LocalDate.now(),
                1
        );

        createTreatment(
                "Chronic Kidney Disease Management",
                "Supportive treatment for chronic kidney disease",
                "Chronic Kidney Disease",
                "Losartan",
                "50 mg once daily",
                LocalDate.now(),
                null,
                "ACTIVE",
                "PKG-NEPH-001",
                "Kidney Care Package",
                "OPD",
                LocalDate.now(),
                1
        );

        createTreatment(
                "Cataract Treatment",
                "Post-operative treatment following cataract surgery",
                "Cataract",
                "Moxifloxacin Eye Drops",
                "1 drop four times daily",
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                "COMPLETED",
                "PKG-OPHTH-001",
                "Cataract Surgery Package",
                "IPD",
                LocalDate.now(),
                2
        );

        createTreatment(
                "Gastroenteritis Treatment",
                "Supportive treatment for acute gastroenteritis",
                "Acute Gastroenteritis",
                "Oral Rehydration Solution",
                "200 ml after each loose stool",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                "ACTIVE",
                "PKG-GI-002",
                "Gastroenteritis Care Package",
                "OPD",
                LocalDate.now(),
                1
        );

        log.info("Dummy treatment data initialized");
    }

    private void createTreatment(
            String name,
            String description,
            String diagnosis,
            String medication,
            String dosage,
            LocalDate startDate,
            LocalDate endDate,
            String status,
            String packageCode,
            String packageName,
            String admissionType,
            LocalDate proposedAdmissionDate,
            Integer estimatedLengthOfStay) {

        Treatment treatment = Treatment.builder()
                .name(name)
                .description(description)
                .diagnosis(diagnosis)
                .medication(medication)
                .dosage(dosage)
                .startDate(startDate)
                .endDate(endDate)
                .status(status)
                .packageCode(packageCode)
                .packageName(packageName)
                .admissionType(admissionType)
                .proposedAdmissionDate(proposedAdmissionDate)
                .estimatedLengthOfStay(estimatedLengthOfStay)
                .build();

        treatmentRepository.save(treatment);
    }
}
