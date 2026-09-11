package com.suma.hmis_service.services.billing;

import com.suma.hmis_service.entities.*;
import com.suma.hmis_service.models.ApiResponse;
import com.suma.hmis_service.models.billing.BillingResponse;
import com.suma.hmis_service.models.billing.BillingDto;
import com.suma.hmis_service.models.billing.CreateBillingRequest;
import com.suma.hmis_service.repositories.billing.BillingRepository;
import com.suma.hmis_service.repositories.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService{
    private final BillingRepository billingRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse getBillingByClaimId(String claimId) {
        BillingResponse  billingResponse = null;
        try {
            Billing billing = billingRepository.findByClaimId(claimId).orElseThrow(() -> new RuntimeException());
            billingResponse =  modelMapper.map(billing, BillingResponse.class);
            billingResponse.setName(billing.getPatient().getPatientName());
            billingResponse.setMobileNumber(billing.getPatient().getMobileNumber());
            return  new ApiResponse(1, "", billingResponse);
        } catch (Exception e) {
            log.error("Error occurred in getBilling by claimId: {}, error: {}", claimId, e.getMessage());
            return new ApiResponse(2, "", billingResponse);
        }
    }

    @Override
    public ApiResponse createBilling(CreateBillingRequest createBillingRequest) {
        BillingResponse  billingResponse = null;
        try {
            Patient patient = patientRepository.findById(createBillingRequest.getPatientId()).orElseThrow(() -> new RuntimeException());

            //here we can give third party api call to fill dto and then we will save it

            BillingDto billingDto = BillingDto.builder()
                    .claimId(1234L)
                    .policyNumber("abcss223")
                    .insurer("test insurence")
                    .hospitalName("test hospital")
                    .amount(1234)
                    .status(EBillStatus.PENDING)
                    .acknowledgementStatus(EAcknowledgementStatus.PENDING)
                    .transactionNumber("123444")
                    .disposition("test")
                    .outcome("testss")
                    .date(LocalDate.now())
                    .admitDate(LocalDate.now())
                    .policyStartDate(LocalDate.now())
                    .policyEndDate(LocalDate.now())
                    .reconsiliationStartDate(LocalDate.now())
                    .reconsiliationEndDate(LocalDate.now())
                    .paymentStatus(EPaymentStatus.PENDING)
                    .build();

            Billing billing =  modelMapper.map(billingDto,Billing.class);
            billing.setPatient(patient);
            Billing savedBilling =  billingRepository.save(billing);

            billingResponse =  modelMapper.map(savedBilling, BillingResponse.class);
            billingResponse.setName(patient.getPatientName());
            billingResponse.setMobileNumber(patient.getMobileNumber());

            return  new ApiResponse(1, "", billingResponse);
        } catch (Exception e) {
            log.error("Error occurred in saveBilling  error: {}", e.getMessage());
            return new ApiResponse(2, "", billingResponse);
        }
    }

    @Override
    public ApiResponse getBillingByPatientId(String patientId) {
        BillingResponse  billingResponse = null;
        try {
            Billing billing = billingRepository.findByPatientId(patientId).orElseThrow(() -> new RuntimeException());
            billingResponse =  modelMapper.map(billing, BillingResponse.class);

            billingResponse.setName(billing.getPatient().getPatientName());
            billingResponse.setMobileNumber(billing.getPatient().getMobileNumber());
            return  new ApiResponse(1, "", billingResponse);
        } catch (Exception e) {
            log.error("Error occurred in getBilling by PatientId: {}, error: {}", patientId, e.getMessage());
            return new ApiResponse(2, "", billingResponse);
        }    }


}
