package com.suma.hmis_service.feature.diagnoses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiagnosisDataInitializer implements CommandLineRunner {

    private final DiagnosisRepository diagnosisRepository;

    @Override
    public void run(String... args) {

        if (diagnosisRepository.count() > 0) {
            return;
        }

        createDiagnosis(
                "DIA001",
                "Hypertension",
                "High blood pressure"
        );

        createDiagnosis(
                "DIA002",
                "Type 2 Diabetes Mellitus",
                "Type 2 diabetes mellitus"
        );

        createDiagnosis(
                "DIA003",
                "Coronary Artery Disease",
                "Disease affecting the coronary arteries"
        );

        createDiagnosis(
                "DIA004",
                "Acute Appendicitis",
                "Acute inflammation of the appendix"
        );

        createDiagnosis(
                "DIA005",
                "Pneumonia",
                "Infection causing inflammation of the lungs"
        );

        createDiagnosis(
                "DIA006",
                "Fracture of Femur",
                "Fracture involving the femur"
        );

        createDiagnosis(
                "DIA007",
                "Cholelithiasis",
                "Gallstones within the gallbladder"
        );

        createDiagnosis(
                "DIA008",
                "Chronic Kidney Disease",
                "Progressive loss of kidney function"
        );

        createDiagnosis(
                "DIA009",
                "Cataract",
                "Clouding of the eye lens"
        );

        createDiagnosis(
                "DIA010",
                "Acute Gastroenteritis",
                "Acute inflammation of the gastrointestinal tract"
        );

        log.info("Dummy diagnosis data initialized");
    }

    private void createDiagnosis(
            String code,
            String name,
            String description) {

        Diagnosis diagnosis = new Diagnosis();

        diagnosis.setCode(code);
        diagnosis.setName(name);
        diagnosis.setDescription(description);
        diagnosis.setActive(true);

        diagnosisRepository.save(diagnosis);
    }
}

