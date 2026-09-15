package com.suma.hmis_service.feature.preauth;


import com.suma.hmis_service.entities.Patient;
import com.suma.hmis_service.entities.PatientAttachment;
import com.suma.hmis_service.exceptions.ResourceNotFoundException;
import com.suma.hmis_service.feature.diagnoses.Diagnosis;
import com.suma.hmis_service.feature.diagnoses.DiagnosisRepository;
import com.suma.hmis_service.feature.facilities.Facility;
import com.suma.hmis_service.feature.facilities.FacilityRepository;
import com.suma.hmis_service.feature.facilities.FacilityType;
import com.suma.hmis_service.feature.insurance.InsurancePolicy;
import com.suma.hmis_service.feature.insurance.InsurancePolicyRepository;
import com.suma.hmis_service.feature.user.User;
import com.suma.hmis_service.feature.user.UserRepository;
import com.suma.hmis_service.feature.user.useprofiles.HealthcareProfessional;
import com.suma.hmis_service.feature.user.useprofiles.HealthcareProfessionalRepository;
import com.suma.hmis_service.repositories.DocumentRepository;
import com.suma.hmis_service.repositories.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PreAuthService {

    private final InsurancePolicyRepository insurancePolicyRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;
    private final HealthcareProfessionalRepository healthcareProfessionalRepository;
    private final PatientRepository patientRepository;
    private final DocumentRepository documentRepository;

    public PreAuthResponse buildPreAuthPayload(PreAuthRequest request) {

        log.info(
                "Building pre-auth payload patientId={} claimId={}",
                request.getPatientId(),
                request.getClaimId()
        );

        InsurancePolicy insurance =
                resolveInsurance(
                        request.getPatientId(),
                        request.getInsurancePolicyId()
                );

        Patient patient = resolvePatient(request.getPatientId());

        List<User> doctors = resolveDoctors(request.getDoctorIds());

        List<Diagnosis> diagnoses =
                resolveDiagnoses(
                        extractDiagnosisIds(request.getDiagnoses())
                );

        List<Facility> facilities =
                resolveFacilities(request.getServices());

        List<PatientAttachment> attachments =
                resolveDocuments(request.getPatientId(), request.getDocumentIds());

        PreAuthPayload payload =
                buildPayload(
                        request,
                        insurance,
                        patient,
                        doctors,
                        diagnoses,
                        facilities,
                        attachments
                );

        log.info(
                "Pre-auth payload built successfully patientId={} claimId={}",
                request.getPatientId(),
                request.getClaimId()
        );

        return new PreAuthResponse(
                "Pre-auth payload built successfully",
                payload
        );
    }

    private InsurancePolicy resolveInsurance(
            Long patientId,
            Long insurancePolicyId) {

        InsurancePolicy insurance =
                insurancePolicyRepository.findById(insurancePolicyId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Insurance policy not found: "
                                                + insurancePolicyId
                                )
                        );

        if (!insurance.getPatientId().equals(patientId)) {
            throw new IllegalArgumentException(
                    "Insurance policy does not belong to patient"
            );
        }

        if (!insurance.isActive()) {
            throw new IllegalArgumentException(
                    "Insurance policy is inactive: "
                            + insurancePolicyId
            );
        }

        return insurance;
    }

    private Patient resolvePatient(Long patientId) {

        return patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found: " + patientId
                        )
                );
    }

    private List<User> resolveDoctors(List<Long> doctorIds) {

        List<User> doctors =
                userRepository.findAllById(doctorIds);

        if (doctors.size() != doctorIds.size()) {
            throw new ResourceNotFoundException(
                    "One or more doctors were not found"
            );
        }

        for (User doctor : doctors) {
            if (doctor.getRole() == null
                    || !"DOCTOR".equalsIgnoreCase(
                    doctor.getRole().name())) {

                throw new IllegalArgumentException(
                        "User is not a doctor: " + doctor.getId()
                );
            }
        }

        return doctors;
    }

    private List<Long> extractDiagnosisIds(
            List<PreAuthRequest.DiagnosisRequest> requests) {

        return requests.stream()
                .map(PreAuthRequest.DiagnosisRequest::getDiagnosisId)
                .toList();
    }

    private List<Diagnosis> resolveDiagnoses(
            List<Long> diagnosisIds) {

        List<Diagnosis> diagnoses =
                diagnosisRepository.findAllById(diagnosisIds);

        if (diagnoses.size() != diagnosisIds.size()) {
            throw new ResourceNotFoundException(
                    "One or more diagnoses were not found"
            );
        }

        return diagnoses;
    }

    private List<Facility> resolveFacilities(
            List<PreAuthRequest.ServiceRequest> requests) {

        List<Long> serviceIds =
                requests.stream()
                        .map(PreAuthRequest.ServiceRequest::getServiceId)
                        .toList();

        List<Facility> services =
                facilityRepository.findAllById(serviceIds);

        if (services.size() != serviceIds.size()) {
            throw new ResourceNotFoundException(
                    "One or more billing services were not found"
            );
        }

        if (services.stream().anyMatch(service -> !service.isActive())) {
            throw new IllegalArgumentException(
                    "One or more billing services are inactive"
            );
        }

        return services;
    }

//    private List<PatientAttachment> resolveDocuments(
//            List<String> documentIds) {
//
//        List<PatientAttachment> documents =
//                documentRepository.findAllById(documentIds);
//
//        if (documents.size() != documentIds.size()) {
//            throw new ResourceNotFoundException(
//                    "One or more documents were not found"
//            );
//        }
//
//        return documents;
//    }
private List<PatientAttachment> resolveDocuments(
        Long patientId,
        List<String> documentIds) {

    List<PatientAttachment> attachments =
            documentRepository.findAllById(documentIds);

    if (attachments.size() != documentIds.size()) {
        throw new ResourceNotFoundException(
                "One or more documents were not found"
        );
    }

    for (PatientAttachment attachment : attachments) {
        if (!attachment.getPatient().getId().equals(patientId)) {
            throw new IllegalArgumentException(
                    "Document does not belong to patient: "
                            + attachment.getId()
            );
        }
    }

    return attachments;
}

    private PreAuthPayload buildPayload(
            PreAuthRequest request,
            InsurancePolicy insurance,
            Patient patient,
            List<User> doctors,
            List<Diagnosis> diagnoses,
            List<Facility> services,
            List<PatientAttachment> documents) {

        PreAuthPayload payload =
                new PreAuthPayload();

        payload.setPatient(buildPatient(patient));
        payload.setBasicDetails(buildBasicDetails(request, insurance));
        payload.setObservationHistory(buildObservationHistory(request));
        payload.setDoctors(buildDoctorDetails(doctors));
        payload.setDiagnoses(buildDiagnoses(diagnoses, request.getDiagnoses()));
        payload.setProcedures(buildProcedures(request.getProcedures()));
        payload.setBilling(buildBilling(services, request.getServices()));
        payload.setDocuments(buildDocumentDetails(documents));

        return payload;
    }

    private PreAuthPayload.Patient buildPatient(Patient patient) {

        PreAuthPayload.Patient payload =
                new PreAuthPayload.Patient();

        payload.setName(patient.getPatientName());
        payload.setAbhaId(patient.getAbhaId());
        payload.setWalletBalance("0");
        payload.setMobileNumber(patient.getMobileNumber());
        payload.setMrNumber(patient.getPatientMrNumber());
        payload.setGender(patient.getGender().name());

        return payload;
    }

    private PreAuthPayload.BasicDetails buildBasicDetails(
            PreAuthRequest request,
            InsurancePolicy insurance) {

        PreAuthPayload.BasicDetails details =
                new PreAuthPayload.BasicDetails();

        details.setInsuranceId(insurance.getId());
        details.setWardNumber(request.getWardNumber());
        details.setBedNumber(request.getBedNumber());
        details.setIpdNumber(request.getIpdNumber());
        details.setAdmissionType(request.getAdmissionType().name());
        details.setMedicoLegalCase(request.getMedicoLegalCase());
        details.setSpecialtyDepartment(request.getSpecialtyDepartment());
        details.setAdmissionDateTime(request.getAdmissionDateTime().toString());
        details.setDischargeDateTime(request.getDischargeDateTime().toString());
        details.setClaimType(request.getClaimType());
        details.setClaimSubType(request.getClaimSubType());
        details.setClaimUse(request.getClaimUse());
        details.setPriority(request.getPriority());

        return details;
    }

    private PreAuthPayload.PatientObservationHistory buildObservationHistory(
            PreAuthRequest request) {

        PreAuthPayload.PatientObservationHistory history =
                new PreAuthPayload.PatientObservationHistory();

        history.setAlcoholHistory(
                request.getAlcoholHistories() == null
                        ? List.of()
                        : request.getAlcoholHistories().stream()
                        .map(item -> {
                            PreAuthPayload.AlcoholHistory details =
                            new PreAuthPayload.AlcoholHistory();
                            details.setCategory(item.getCategory().name());
                            details.setAdditionalInformation(
                                    item.getAdditionalInformation()
                            );
                            return details;
                        })
                        .toList()
        );

        history.setGeneralHistory(
                request.getGeneralHistories() == null
                        ? List.of()
                        : request.getGeneralHistories().stream()
                        .map(item -> {
                            PreAuthPayload.GeneralHistory details =
                            new PreAuthPayload.GeneralHistory();
                            details.setCategory(item.getCategory().name());
                            details.setAdditionalInformation(
                                    item.getAdditionalInformation()
                            );
                            return details;
                        })
                        .toList()
        );

        return history;
    }

    private List<PreAuthPayload.DoctorDetails> buildDoctorDetails(
            List<User> doctors) {

        return doctors.stream()
                .map(doctor -> {
                    HealthcareProfessional profile =
                            healthcareProfessionalRepository
                                    .findByUserId(doctor.getId())
                                    .orElseThrow(() ->
                                            new ResourceNotFoundException(
                                                    "Professional profile not found for doctor: "
                                                            + doctor.getId()
                                            )
                                    );

                    PreAuthPayload.DoctorDetails details =
                            new PreAuthPayload.DoctorDetails();

                    details.setName(doctor.getName());
                    details.setHealthcareProfessionalId(
                            profile.getHealthcareProfessionId()
                    );
                    details.setRole(doctor.getRole().name());
                    details.setContactNumber(profile.getContactNumber());
                    details.setQualification(profile.getQualification());

                    return details;
                })
                .toList();
    }

    private List<PreAuthPayload.DiagnosisDetails> buildDiagnoses(
            List<Diagnosis> diagnoses,
            List<PreAuthRequest.DiagnosisRequest> requests) {

        return diagnoses.stream()
                .map(diagnosis -> {
                    PreAuthRequest.DiagnosisRequest diagnosisRequest =
                            requests.stream()
                                    .filter(item ->
                                            item.getDiagnosisId()
                                                    .equals(diagnosis.getId()))
                                    .findFirst()
                                    .orElseThrow();

                    PreAuthPayload.DiagnosisDetails details =
                            new PreAuthPayload.DiagnosisDetails();

                    details.setType(diagnosisRequest.getType().name());
                    details.setPresentOnAdmission(
                            diagnosisRequest.getPresentOnAdmission().name()
                    );
                    details.setName(diagnosis.getName());
                    details.setCode(diagnosis.getCode());
                    details.setClinicalStatus(
                            diagnosisRequest.getClinicalStatus().name()
                    );
                    details.setSeverity(
                            diagnosisRequest.getSeverity().name()
                    );

                    return details;
                })
                .toList();
    }

    private List<PreAuthPayload.ProcedureDetails> buildProcedures(
            List<PreAuthRequest.ProcedureRequest> procedures) {

        return procedures.stream()
                .map(procedure -> {
                    PreAuthPayload.ProcedureDetails details =
                            new PreAuthPayload.ProcedureDetails();

                    details.setCategory(procedure.getCategory().name());
                    details.setProcedureName(procedure.getProcedureName());
                    details.setCustomProcedureName(
                            procedure.getCustomProcedureName()
                    );
                    details.setStratification(procedure.getStratification());
                    details.setDaysOrQuantity(procedure.getDaysOrQuantity());
                    details.setAmount(procedure.getAmount().toString());
                    details.setStartDate(
                            procedure.getStartDate() == null
                                    ? null
                                    : procedure.getStartDate().toString()
                    );
                    details.setEndDate(
                            procedure.getEndDate() == null
                                    ? null
                                    : procedure.getEndDate().toString()
                    );

                    return details;
                })
                .toList();
    }

    private PreAuthPayload.Billing buildBilling(
            List<Facility> services,
            List<PreAuthRequest.ServiceRequest> requests) {

        PreAuthPayload.Billing billing =
                new PreAuthPayload.Billing();

        billing.setConsultation(new ArrayList<>());
        billing.setInvestigation(new ArrayList<>());
        billing.setOperationTheatre(new ArrayList<>());
        billing.setOther(new ArrayList<>());

        for (Facility facility : services) {

            PreAuthRequest.ServiceRequest request =
                    requests.stream()
                            .filter(item ->
                                    item.getServiceId()
                                            .equals(facility.getId()))
                            .findFirst()
                            .orElseThrow();

            PreAuthPayload.BillingService item =
                    new PreAuthPayload.BillingService();

            item.setCode(facility.getCode());
            item.setDescription(facility.getDescription());
            item.setAmount(
                    facility.getAmount()
                            .multiply(
                                    BigDecimal.valueOf(
                                            request.getQuantity()
                                    )
                            )
                            .toString()
            );
            item.setQuantity(request.getQuantity());

            addBillingService(
                    billing,
                    facility.getType(),
                    item
            );
        }

        return billing;
    }

    private void addBillingService(
            PreAuthPayload.Billing billing,
            FacilityType type,
            PreAuthPayload.BillingService service) {

        switch (type) {
            case CONSULTATION -> billing.getConsultation().add(service);

            case INVESTIGATION -> billing.getInvestigation().add(service);

            case OPERATION_THEATRE -> billing.getOperationTheatre().add(service);

            case OTHER -> billing.getOther().add(service);
        }
    }

    private List<PreAuthPayload.DocumentDetails> buildDocumentDetails(
            List<PatientAttachment> attachments) {

        return attachments.stream()
                .map(attachment -> {
                    PreAuthPayload.DocumentDetails details =
                            new PreAuthPayload.DocumentDetails();

                    details.setDocumentId(attachment.getId());
                    details.setFileName(attachment.getFileName());
                    details.setContentType(attachment.getContentType());
                    details.setFileSize(attachment.getFileSize());
                    details.setDocumentType(
                            attachment.getDocumentType().name()
                    );
                    details.setUrl(attachment.getFilePath());

                    return details;
                })
                .toList();
    }
}
