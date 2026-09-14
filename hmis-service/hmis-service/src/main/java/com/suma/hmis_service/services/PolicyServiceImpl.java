package com.suma.hmis_service.services;
import com.suma.hmis_service.clients.PolicyClient;
import com.suma.hmis_service.entities.Patient;
import com.suma.hmis_service.entities.Policy;
import com.suma.hmis_service.models.ApiResponse;
import com.suma.hmis_service.models.policy.PolicyResponse;
import com.suma.hmis_service.repositories.PolicyRepository;
import com.suma.hmis_service.repositories.patient.PatientRepository;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PolicyServiceImpl implements PolicyService {

    private final PolicyClient policyClient;
    private final PolicyRepository policyRepository;
    private final PatientRepository patientRepository;

    public PolicyServiceImpl(PolicyClient policyClient, PolicyRepository policyRepository, PatientRepository patientRepository) {

        this.policyClient = policyClient;
        this.policyRepository = policyRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public ApiResponse getExternalPolicies(Long patientId, String policyNumber) {

        Patient patient = patientRepository.findById(patientId).orElseThrow();

        if (policyNumber == null || policyNumber.isEmpty()) {
            return new ApiResponse(0, "policyNumber is required", null);
        }

        JsonNode response = policyClient.getCoverage(policyNumber);

        if (response.path("entry").isMissingNode() || response.path("entry").isEmpty()) {

            return new ApiResponse(
                    0,
                    "No coverage found for this patient's policy number",
                    null
            );
        }

        JsonNode coverage = response.path("entry").path(0).path("resource");

        String actualPolicyNumber = coverage.path("identifier").path(0).path("value").asText();

        patient.setPolicyNumber(actualPolicyNumber);

        patientRepository.save(patient);

        Policy policy = new Policy();

        policy.setPatient(patient);

        policy.setPolicyNumber(actualPolicyNumber);

        policy.setMemberId(coverage.path("subscriberId").asText());

        policy.setInsuranceCompany(
                coverage.path("payor").path(0).path("display").asText()
        );

        policy.setPolicyType(
                coverage.path("type").path("text").asText()
        );

        policy.setRelationship(
                coverage.path("relationship").path("text").asText()
        );

        policy.setPolicyStartDate(
                convertToDate(coverage.path("period").path("start").asText())
        );

        policy.setPolicyEndDate(
                convertToDate(coverage.path("period").path("end").asText()
                )
        );

        Policy savedPolicy = policyRepository.save(policy);

        return new ApiResponse(1, "", PolicyResponse.toPolicyResponseUsingPolicy(savedPolicy));}

       @Override
       public ApiResponse getPoliciesByPatient(Long patientId) {

        Patient patient = patientRepository.findById(patientId).orElseThrow();

        List<Policy> policies = policyRepository.findByPatient(patient);

        List<PolicyResponse> policyResponses = new ArrayList<>();

        for (Policy policy : policies) {

            policyResponses.add(PolicyResponse.toPolicyResponseUsingPolicy(policy));
        }

        return new ApiResponse(1, "", policyResponses);}

       private Date convertToDate(String date) {

        if (date == null || date.isEmpty()) {
            return null;
        }

        LocalDate localDate = LocalDate.parse(date);

        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}