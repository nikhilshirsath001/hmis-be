package com.suma.hmis_service.feature.eligibility;

import com.suma.hmis_service.entities.Patient;
import com.suma.hmis_service.exceptions.ResourceNotFoundException;
import com.suma.hmis_service.feature.diagnoses.Diagnosis;
import com.suma.hmis_service.feature.diagnoses.DiagnosisRepository;
import com.suma.hmis_service.feature.facilities.Facility;
import com.suma.hmis_service.feature.facilities.FacilityRepository;
import com.suma.hmis_service.feature.facilities.FacilityType;
import com.suma.hmis_service.feature.infra.bed.Bed;
import com.suma.hmis_service.feature.infra.bed.BedRepository;
import com.suma.hmis_service.feature.infra.bed.BedStatus;
import com.suma.hmis_service.feature.infra.ward.WardRepository;
import com.suma.hmis_service.feature.insurance.InsurancePolicy;
import com.suma.hmis_service.feature.insurance.InsurancePolicyRepository;
import com.suma.hmis_service.feature.user.*;
import com.suma.hmis_service.feature.user.useprofiles.HealthcareProfessional;
import com.suma.hmis_service.feature.user.useprofiles.HealthcareProfessionalRepository;
import com.suma.hmis_service.repositories.DocumentRepository;
import com.suma.hmis_service.repositories.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EligibilityService {

    private final InsurancePolicyRepository insurancePolicyRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;
    private final HealthcareProfessionalRepository healthcareProfessionalRepository;
    private final PatientRepository patientRepository;
    private final DocumentRepository documentRepository;

    public EligibilityResponse buildEligibilityPayload(
            EligibilityRequest request) {

        log.info(
                "Building eligibility payload patientId={} claimId={}",
                request.getPatientId(),
                request.getClaimId()
        );

        InsurancePolicy insurance =
                resolveInsurance(
                        request.getPatientId(),
                        request.getInsurancePolicyId()
                );

        User doctor = resolveDoctor(request.getDoctorId());

        List<Diagnosis> diagnoses =
                resolveDiagnoses(request.getDiagnosisIds());

        List<Facility> facilities =
                resolveFacilities(request.getServices());

        OneEligibilityPayload payload =
                buildPayload(
                        request,
                        insurance,
                        doctor,
                        diagnoses,
                        facilities
                );

        log.info(
                "Eligibility payload built successfully patientId={} claimId={}",
                request.getPatientId(),
                request.getClaimId()
        );

        return new EligibilityResponse(
                "Eligibility payload built successfully",
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

    private User resolveDoctor(Long doctorId) {

        User doctor =
                userRepository.findById(doctorId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found: " + doctorId
                                )
                        );

        if (doctor.getRole() == null
                || !"DOCTOR".equalsIgnoreCase(
                doctor.getRole().name())) {

            throw new IllegalArgumentException(
                    "User is not a doctor: " + doctorId
            );
        }

        return doctor;
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
            List<EligibilityServiceRequest> requests) {

        List<Long> serviceIds =
                requests.stream()
                        .map(EligibilityServiceRequest::getServiceId)
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

    private OneEligibilityPayload buildPayload(
            EligibilityRequest request,
            InsurancePolicy insurance,
            User doctor,
            List<Diagnosis> diagnoses,
            List<Facility> services) {

        OneEligibilityPayload payload =
                new OneEligibilityPayload();

        payload.setPatient(buildPatient(request));
        payload.setInsurance(buildInsurance(insurance));
        payload.setBasicDetails(buildBasicDetails(request));
        payload.setDoctorDetails(buildDoctorDetails(doctor));
        payload.setEligibilityPurposes(
                buildEligibilityPurposes(
                        request.getEligibilityPurposes()
                )
        );
        payload.setDiagnosis(buildDiagnoses(diagnoses));
        payload.setBilling(
                buildBilling(
                        services,
                        request.getServices()
                )
        );

        return payload;
    }

    private OneEligibilityPayload.Patient buildPatient(
            EligibilityRequest request) {

        OneEligibilityPayload.Patient patient =
                new OneEligibilityPayload.Patient();

        patient.setPatientId(request.getPatientId());

        return patient;
    }

    private OneEligibilityPayload.Insurance buildInsurance(
            InsurancePolicy insurance) {

        OneEligibilityPayload.Insurance payload =
                new OneEligibilityPayload.Insurance();

        payload.setInsurancePolicyId(insurance.getId());
        payload.setPolicyNumber(insurance.getPolicyNumber());
        payload.setProviderName(insurance.getProviderName());
        payload.setPlanName(insurance.getPlanName());
        payload.setMemberId(insurance.getMemberId());
        payload.setGroupNumber(insurance.getGroupNumber());
        payload.setCoverageAmount(
                insurance.getCoverageAmount().toString()
        );
        payload.setClaimedAmount(
                insurance.getClaimedAmount().toString()
        );
        payload.setRemainingAmount(
                insurance.getRemainingAmount().toString()
        );
        payload.setStartDate(
                insurance.getStartDate().toString()
        );
        payload.setEndDate(
                insurance.getEndDate().toString()
        );

        return payload;
    }

    private OneEligibilityPayload.BasicDetails buildBasicDetails(
            EligibilityRequest request) {

        OneEligibilityPayload.BasicDetails details =
                new OneEligibilityPayload.BasicDetails();

        details.setClaimId(request.getClaimId());
        details.setWardName(request.getWardName());
        details.setBedNumber(request.getBedNumber());
        details.setIpdNumber(request.getIpdNumber());

        return details;
    }

    private OneEligibilityPayload.DoctorDetails buildDoctorDetails(
            User doctor) {

        OneEligibilityPayload.DoctorDetails details =
                new OneEligibilityPayload.DoctorDetails();
        HealthcareProfessional healthcareProfessional = healthcareProfessionalRepository.findById(doctor.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Professional profile not found!"));
        details.setHealthcareProfessionId(
                healthcareProfessional.getHealthcareProfessionId()
        );
        details.setRole(doctor.getRole().name());
        details.setContactNumber(
                healthcareProfessional.getContactNumber()
        );
        details.setQualification(
                healthcareProfessional.getQualification()
        );
        return details;
    }

    private OneEligibilityPayload.EligibilityPurposes
    buildEligibilityPurposes(List<String> purposes) {

        OneEligibilityPayload.EligibilityPurposes payload =
                new OneEligibilityPayload.EligibilityPurposes();

        payload.setEligibilityPurposes(purposes);

        payload.setAuthRequirement(
                purposes.contains("AUTH_REQUIREMENT")
        );

        payload.setBenefits(
                purposes.contains("BENEFITS")
        );

        payload.setDiscovery(
                purposes.contains("DISCOVERY")
        );

        payload.setValidation(
                purposes.contains("VALIDATION")
        );

        return payload;
    }

    private List<OneEligibilityPayload.Diagnosis> buildDiagnoses(
            List<Diagnosis> diagnoses) {

        return diagnoses.stream()
                .map(diagnosis -> {
                    OneEligibilityPayload.Diagnosis payload =
                            new OneEligibilityPayload.Diagnosis();

                    payload.setId(diagnosis.getId());
                    payload.setCode(diagnosis.getCode());
                    payload.setName(diagnosis.getName());
                    payload.setDescription(
                            diagnosis.getDescription()
                    );

                    return payload;
                })
                .toList();
    }

    private OneEligibilityPayload.Billing buildBilling(
            List<Facility> services,
            List<EligibilityServiceRequest> requests) {

        OneEligibilityPayload.Billing billing =
                new OneEligibilityPayload.Billing();

        billing.setConsultation(new ArrayList<>());
        billing.setInvestigation(new ArrayList<>());
        billing.setOperationTheatre(new ArrayList<>());
        billing.setOther(new ArrayList<>());

        for (Facility facility : services) {

            EligibilityServiceRequest request =
                    requests.stream()
                            .filter(item ->
                                    item.getServiceId()
                                            .equals(facility.getId()))
                            .findFirst()
                            .orElseThrow();

            OneEligibilityPayload.BillingService item =
                    new OneEligibilityPayload.BillingService();

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
            OneEligibilityPayload.Billing billing,
            FacilityType type,
            OneEligibilityPayload.BillingService service) {

        switch (type) {
            case CONSULTATION -> billing.getConsultation().add(service);

            case INVESTIGATION -> billing.getInvestigation().add(service);

            case OPERATION_THEATRE -> billing.getOperationTheatre().add(service);

            case OTHER -> billing.getOther().add(service);
        }
    }

    private EligibilityResponsePayload.BeneficiaryDetails resolvePatient_(String abdmId) {
        Patient patient = patientRepository.findByAbhaId(abdmId).orElseThrow(() -> new ResourceNotFoundException("Patient not found!"));
        EligibilityResponsePayload.BeneficiaryDetails beneficiaryDetails =
                new EligibilityResponsePayload.BeneficiaryDetails();

        beneficiaryDetails.setAbdmId(patient.getAbhaId());
        beneficiaryDetails.setPatientId(patient.getId());
        beneficiaryDetails.setName(patient.getPatientName());
        beneficiaryDetails.setMrNumber(patient.getPatientMrNumber());
        beneficiaryDetails.setWalletBalance("0");
        beneficiaryDetails.setGender(patient.getGender().name());
        return beneficiaryDetails;
    }

    private List<EligibilityResponsePayload.DiagnosisDetails> resolveDiagnosis_(String abdmId) {
        Random rand = new Random();
        Long randomNumber = rand.nextLong(10);
        Diagnosis diagnosis = diagnosisRepository.findById(randomNumber).orElse(null);
        List<EligibilityResponsePayload.DiagnosisDetails> diagnosisDetails = new ArrayList<>();

        if (diagnosis == null)
            return diagnosisDetails;

        EligibilityResponsePayload.DiagnosisDetails details = new
                EligibilityResponsePayload.DiagnosisDetails();
        details.setName(diagnosis.getName());
        details.setCode(diagnosis.getCode());
        details.setDescription(diagnosis.getDescription());
        diagnosisDetails.add(details);

        return diagnosisDetails;
    }

    public EligibilityResponsePayload calculateAndGetEligibilityStatus(String abhaId) {
        //get the policy details
//        get the facilities
//        policyAmount - facilityamount find generate abs() and return
//        get the policy details by patient id
//        load dr, providerPayload random
//        load 2 diagnoses random
        //billing

        EligibilityResponsePayload response = new EligibilityResponsePayload();
        EligibilityResponsePayload.BeneficiaryDetails beneficiaryDetails = resolvePatient_(abhaId);
        List<EligibilityResponsePayload.DiagnosisDetails> diagnosisDetails = resolveDiagnosis_(abhaId);
        response.setPatient(beneficiaryDetails);
        response.setWard(resolveWard_(abhaId));
        response.setDiagnosis(diagnosisDetails);
        response.setDoctor(resolveDocuments_(beneficiaryDetails.getPatientId()));

        response.setBilling(resolveBilling_());
        return response;
    }

    private List<EligibilityResponsePayload.BillingDetails> resolveBilling_() {
        List<EligibilityResponsePayload.BillingDetails> list =
                new ArrayList<>();
//        facilityRepository.find
        return list;
    }

    private List<EligibilityResponsePayload.DoctorDetails> resolveDocuments_(Long patientId) {

        List<EligibilityResponsePayload.DoctorDetails> list = new ArrayList<>();
        User doctor = userRepository.findByRoleAndActiveTrue(Role.DOCTOR)
                .stream()
                .findAny().orElse(null);
        if (doctor != null) {
            HealthcareProfessional user = healthcareProfessionalRepository.findByUserId(doctor.getId()).orElse(null);
            if (user != null) {
                EligibilityResponsePayload.DoctorDetails details = new EligibilityResponsePayload.DoctorDetails();

                details.setContactNumber(user.getContactNumber());
                details.setName(doctor.getName());
                details.setRole(doctor.getRole().name());
                details.setQualification(user.getQualification());
                details.setHealthcareProfessionalId(user.getHealthcareProfessionId());


                list.add(details);
            }
        }
        return list;
    }

    private final BedRepository bedRepository;

    private EligibilityResponsePayload.WardDetails resolveWard_(String abdmId) {
        Bed bed = bedRepository.findByRoomIdAndStatusAndActiveTrue(1L, BedStatus.AVAILABLE)
                .stream()
                .findFirst()
                .orElse(null);
        EligibilityResponsePayload.WardDetails wardDetails = new EligibilityResponsePayload.WardDetails();
        wardDetails.setId(String.valueOf(bed.getId()));
        wardDetails.setBadNumber(bed.getCode());

        wardDetails.setEligibilityPurpose(
                List.of(
                        "Auth-Requirements",
                        "Benefits",
                        "Discovery",
                        "Validation"
                )
        );
        wardDetails.setIpdNumber("IpdNumber");
        wardDetails.setName(bed.getName());

        return wardDetails;
    }


}

