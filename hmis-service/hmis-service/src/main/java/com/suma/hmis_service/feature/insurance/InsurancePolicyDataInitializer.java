package com.suma.hmis_service.feature.insurance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class InsurancePolicyDataInitializer implements CommandLineRunner {

    private final InsurancePolicyRepository insurancePolicyRepository;

    @Override
    public void run(String... args) {

        if (insurancePolicyRepository.count() > 0) {
            return;
        }

        InsurancePolicy policy1 = createPolicy(
                1L,
                "POL10001",
                "ONE HCX PMJY",
                "PMJY Health Cover",
                "MEM10001",
                "GRP10001",
                "150000",
                "25000",
                "125000"
        );

        InsurancePolicy policy2 = createPolicy(
                1L,
                "POL10002",
                "Private Health Provider",
                "Family Health Plan",
                "MEM10002",
                "GRP10002",
                "300000",
                "50000",
                "250000"
        );

        InsurancePolicy policy3 = createPolicy(
                2L,
                "POL20001",
                "ONE HCX PMJY",
                "PMJY Health Cover",
                "MEM20001",
                "GRP20001",
                "200000",
                "40000",
                "160000"
        );

        InsurancePolicy policy4 = createPolicy(
                3L,
                "POL30001",
                "ONE HCX PMJY",
                "PMJY Senior Health Cover",
                "MEM30001",
                "GRP30001",
                "500000",
                "100000",
                "400000"
        );

        insurancePolicyRepository.save(policy1);
        insurancePolicyRepository.save(policy2);
        insurancePolicyRepository.save(policy3);
        insurancePolicyRepository.save(policy4);

        log.info("Insurance policy dummy data initialized");
    }

    private InsurancePolicy createPolicy(
            Long patientId,
            String policyNumber,
            String providerName,
            String planName,
            String memberId,
            String groupNumber,
            String coverageAmount,
            String claimedAmount,
            String remainingAmount) {

        InsurancePolicy policy = new InsurancePolicy();

        policy.setPatientId(patientId);
        policy.setPolicyNumber(policyNumber);
        policy.setProviderName(providerName);
        policy.setPlanName(planName);
        policy.setMemberId(memberId);
        policy.setGroupNumber(groupNumber);
        policy.setCoverageAmount(new BigDecimal(coverageAmount));
        policy.setClaimedAmount(new BigDecimal(claimedAmount));
        policy.setRemainingAmount(new BigDecimal(remainingAmount));
        policy.setStartDate(LocalDate.of(2026, 1, 1));
        policy.setEndDate(LocalDate.of(2026, 12, 31));
        policy.setActive(true);

        return policy;
    }
}

