package com.suma.hmis_service.feature.preauth;


import com.suma.hmis_service.entities.Patient;
import com.suma.hmis_service.entities.PatientAttachment;
import com.suma.hmis_service.exceptions.ResourceNotFoundException;
import com.suma.hmis_service.feature.diagnoses.Diagnosis;
import com.suma.hmis_service.feature.diagnoses.DiagnosisRepository;
import com.suma.hmis_service.feature.facilities.Facility;
import com.suma.hmis_service.feature.facilities.FacilityRepository;
import com.suma.hmis_service.feature.facilities.FacilityType;
import com.suma.hmis_service.feature.preauth.PreAuthEnums.*;
import com.suma.hmis_service.feature.user.Role;
import com.suma.hmis_service.feature.user.User;
import com.suma.hmis_service.feature.user.UserRepository;
import com.suma.hmis_service.feature.user.useprofiles.HealthcareProfessional;
import com.suma.hmis_service.feature.user.useprofiles.HealthcareProfessionalRepository;
import com.suma.hmis_service.repositories.DocumentRepository;
import com.suma.hmis_service.repositories.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PreAuthGeneratorService {

    private final PatientRepository patientRepository;
    private final DocumentRepository patientAttachmentRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;
    private final HealthcareProfessionalRepository healthcareProfessionalRepository;

    // ---------------- sample pools (edit freely) ----------------

    private static final List<String> SPECIALTIES = List.of(
            "General Medicine", "Cardiology", "Orthopaedics", "Neurology",
            "Paediatrics", "General Surgery", "Oncology", "Nephrology");

    private static final List<String> CLAIM_TYPES = List.of(
            "Pre-Authorization", "Enhancement", "Discharge");

    private static final List<String> CLAIM_SUB_TYPES = List.of(
            "Hospitalization", "Day Care", "Surgical");

    private static final List<String> CLAIM_USES = List.of(
            "PRE_AUTH", "CLAIM", "PRE_DETERMINATION");

    private static final List<String> PRIORITIES = List.of(
            "LOW", "NORMAL", "HIGH", "URGENT");

    private static final List<String> PROCEDURE_NAMES = List.of(
            "Acute Haemodialysis", "Unspecified Surgery Package", "LAMA/DAMA Procedure",
            "Coronary Angioplasty", "Cataract Extraction", "Appendicectomy",
            "Knee Replacement", "Burn Debridement");

    private static final List<String> STRATIFICATIONS = Arrays.asList(
            "Tier-1", "Tier-2", "Tier-3", null);

    private static final List<String> ALCOHOL_NOTES = Arrays.asList(
            "1-2 drinks per day", "Binge drinking on weekends",
            "Occasional social drinking", null);

    private static final List<String> GENERAL_HISTORY_NOTES = Arrays.asList(
            "Penicillin allergy", "Father - diabetes", "Prior surgery 2019",
            "Known hypertensive since 2015", null);

    private static final List<String> FALLBACK_DIAGNOSIS_NAMES = List.of(
            "Acute Amebic Dysentery",
            "Acute Gastroenteropathy due to Norwalk Agent",
            "Acute Gastroenteropathy due to Other Small Round Viruses",
            "Type 2 Diabetes Mellitus",
            "Essential Hypertension",
            "Acute Appendicitis");

    private static final List<String> FALLBACK_ICD_CODES = List.of(
            "A006", "A081", "A082", "E119", "I10", "K358");

    // ---------------- entry point ----------------

    public PreAuthPayload generate(Long patientId) {

        log.info("Generating pre-auth payload for patientId={}", patientId);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found: " + patientId));

        PreAuthPayload payload = new PreAuthPayload();

        payload.setPatient(buildPatient(patient));
        payload.setBasicDetails(randomBasicDetails());
        payload.setObservationHistory(randomObservationHistory());
        payload.setDoctors(randomDoctors());
        payload.setDiagnoses(randomDiagnoses());
        payload.setProcedures(randomProcedures());
        payload.setBilling(randomBilling());
        payload.setDocuments(loadAttachments(patientId));

        return payload;
    }

    // ---------------- patient (real) ----------------

    private PreAuthPayload.Patient buildPatient(Patient patient) {

        PreAuthPayload.Patient p = new PreAuthPayload.Patient();

        p.setName(patient.getPatientName());
        p.setAbhaId(patient.getAbhaId());
        p.setWalletBalance(randomMoney(0, 50000));
        p.setMobileNumber(randomMobile());
        p.setMrNumber(patient.getPatientMrNumber());
        p.setGender(patient.getGender().name());

        return p;
    }

    private List<PreAuthPayload.DocumentDetails> loadAttachments(Long patientId) {
        List<PatientAttachment> patientAttachments = patientAttachmentRepository.findByPatientId(patientId).orElse(new ArrayList<>());
        return patientAttachments
                .stream()
                .map(this::toDocumentDetails)
                .toList();
    }

    private PreAuthPayload.DocumentDetails toDocumentDetails(PatientAttachment a) {

        PreAuthPayload.DocumentDetails d = new PreAuthPayload.DocumentDetails();

        d.setDocumentId(a.getId());
        d.setFileName(a.getFileName());
        d.setContentType(a.getContentType());
        d.setFileSize(a.getFileSize());
        d.setDocumentType(a.getDocumentType().name());
        d.setUrl(a.getFilePath());

        return d;
    }

    // ---------------- basic details (random) ----------------

    private PreAuthPayload.BasicDetails randomBasicDetails() {

        PreAuthPayload.BasicDetails b = new PreAuthPayload.BasicDetails();

        LocalDateTime admission = LocalDateTime.now().minusDays(randInt(0, 10));
        LocalDateTime discharge = admission.plusDays(randInt(1, 14));

        b.setClaimId("CLM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        b.setInsuranceId((long) randInt(1, 999));
        b.setWardNumber("W-" + randInt(100, 999));
        b.setBedNumber("B-" + randInt(1, 50));
        b.setIpdNumber("IPD-" + System.currentTimeMillis());
        b.setAdmissionType(randomOf(AdmissionType.values()).name());
        b.setMedicoLegalCase(ThreadLocalRandom.current().nextBoolean());
        b.setSpecialtyDepartment(randomOf(SPECIALTIES));
        b.setAdmissionDateTime(admission.toString());
        b.setDischargeDateTime(discharge.toString());
        b.setClaimType(randomOf(CLAIM_TYPES));
        b.setClaimSubType(randomOf(CLAIM_SUB_TYPES));
        b.setClaimUse(randomOf(CLAIM_USES));
        b.setPriority(randomOf(PRIORITIES));

        return b;
    }

    // ---------------- observation history (random) ----------------

    private PreAuthPayload.PatientObservationHistory randomObservationHistory() {

        PreAuthPayload.PatientObservationHistory h =
                new PreAuthPayload.PatientObservationHistory();

        h.setAlcoholHistory(randomAlcoholHistory());
        h.setGeneralHistory(randomGeneralHistory());

        return h;
    }

    private List<PreAuthPayload.AlcoholHistory> randomAlcoholHistory() {

        if (ThreadLocalRandom.current().nextInt(100) < 30) {
            return List.of(); // 30% chance: no alcohol history at all
        }

        int count = randInt(1, 2);
        List<PreAuthPayload.AlcoholHistory> list = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            PreAuthPayload.AlcoholHistory item = new PreAuthPayload.AlcoholHistory();
            item.setCategory(randomOf(AlcoholHistoryCategory.values()).name());
            item.setAdditionalInformation(randomOf(ALCOHOL_NOTES));
            list.add(item);
        }

        return list;
    }

    private List<PreAuthPayload.GeneralHistory> randomGeneralHistory() {

        int count = randInt(1, 3);
        List<PreAuthPayload.GeneralHistory> list = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            PreAuthPayload.GeneralHistory item = new PreAuthPayload.GeneralHistory();
            item.setCategory(randomOf(GeneralHistoryCategory.values()).name());
            item.setAdditionalInformation(randomOf(GENERAL_HISTORY_NOTES));
            list.add(item);
        }

        return list;
    }

    // ---------------- doctors (real users, random pick) ----------------

    private List<PreAuthPayload.DoctorDetails> randomDoctors() {

        List<User> doctors =
                new ArrayList<>(userRepository.findByRoleAndActiveTrue(Role.DOCTOR));
        Collections.shuffle(doctors);

        int count = Math.min(doctors.size(), randInt(1, 2));
        List<PreAuthPayload.DoctorDetails> list = new ArrayList<>(count);

        for (User doctor : doctors.subList(0, count)) {

            HealthcareProfessional profile =
                    healthcareProfessionalRepository.findByUserId(doctor.getId())
                            .orElse(null);

            if (profile == null) {
                continue;
            }

            PreAuthPayload.DoctorDetails d = new PreAuthPayload.DoctorDetails();
            d.setName(doctor.getName());
            d.setHealthcareProfessionalId(profile.getHealthcareProfessionId());
            d.setRole(doctor.getRole().name());
            d.setContactNumber(profile.getContactNumber());
            d.setQualification(profile.getQualification());

            list.add(d);
        }

        return list;
    }

    // ---------------- diagnoses (DB first, fallback random) ----------------

    private List<PreAuthPayload.DiagnosisDetails> randomDiagnoses() {

        List<Diagnosis> fromDb = diagnosisRepository.findAll();

        List<String> names;
        List<String> codes;

        if (fromDb.isEmpty()) {
            names = new ArrayList<>(FALLBACK_DIAGNOSIS_NAMES);
            codes = new ArrayList<>(FALLBACK_ICD_CODES);
        } else {
            Collections.shuffle(fromDb);
            names = fromDb.stream().map(Diagnosis::getName).toList();
            codes = fromDb.stream().map(Diagnosis::getCode).toList();
        }

        int count = Math.min(names.size(), randInt(1, 3));
        List<PreAuthPayload.DiagnosisDetails> list = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            PreAuthPayload.DiagnosisDetails d = new PreAuthPayload.DiagnosisDetails();
            d.setType(randomOf(DiagnosisType.values()).name());
            d.setPresentOnAdmission(randomOf(PresentOnAdmission.values()).name());
            d.setName(names.get(i));
            d.setCode(codes.get(i));
            d.setClinicalStatus(randomOf(ClinicalStatus.values()).name());
            d.setSeverity(randomOf(Severity.values()).name());
            list.add(d);
        }

        return list;
    }

    // ---------------- procedures (fully random) ----------------

    private List<PreAuthPayload.ProcedureDetails> randomProcedures() {

        int count = randInt(1, 3);
        List<PreAuthPayload.ProcedureDetails> list = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            PreAuthPayload.ProcedureDetails p = new PreAuthPayload.ProcedureDetails();
            p.setCategory(randomOf(ProcedureCategory.values()).name());
            p.setProcedureName(randomOf(PROCEDURE_NAMES));
            p.setCustomProcedureName(null);
            p.setStratification(randomOf(STRATIFICATIONS));
            p.setDaysOrQuantity(randInt(1, 7));
            p.setAmount(randomMoney(1000, 100000));
            p.setStartDate(LocalDateTime.now().plusDays(i).toString());
            p.setEndDate(LocalDateTime.now().plusDays(i + 1).toString());
            list.add(p);
        }

        return list;
    }

    // ---------------- billing (real facilities, random pick + qty) ----------------

    private PreAuthPayload.Billing randomBilling() {

        PreAuthPayload.Billing billing = new PreAuthPayload.Billing();
        billing.setConsultation(new ArrayList<>());
        billing.setInvestigation(new ArrayList<>());
        billing.setOperationTheatre(new ArrayList<>());
        billing.setOther(new ArrayList<>());

        List<Facility> facilities = facilityRepository.findAll().stream()
                .filter(Facility::isActive)
                .collect(Collectors.toCollection(ArrayList::new));

        Collections.shuffle(facilities);

        for (Facility facility : facilities.subList(0, Math.min(facilities.size(), randInt(2, 5)))) {

            int quantity = randInt(1, 5);

            PreAuthPayload.BillingService item = new PreAuthPayload.BillingService();
            item.setCode(facility.getCode());
            item.setDescription(facility.getDescription());
            item.setQuantity(quantity);
            item.setAmount(
                    facility.getAmount()
                            .multiply(BigDecimal.valueOf(quantity))
                            .toString()
            );

            switch (facility.getType()) {
                case CONSULTATION -> billing.getConsultation().add(item);
                case INVESTIGATION -> billing.getInvestigation().add(item);
                case OPERATION_THEATRE -> billing.getOperationTheatre().add(item);
                case OTHER -> billing.getOther().add(item);
            }
        }

        return billing;
    }

    // ---------------- helpers ----------------

    private static <T> T randomOf(T[] values) {
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }

    private static <T> T randomOf(List<T> values) {
        return values.get(ThreadLocalRandom.current().nextInt(values.size()));
    }

    private static int randInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private static String randomMoney(int min, int max) {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(min, max))
                .setScale(2, java.math.RoundingMode.HALF_UP)
                .toString();
    }

    private static String randomMobile() {
        return "9" + ThreadLocalRandom.current().nextLong(100000000L, 999999999L);
    }
}
