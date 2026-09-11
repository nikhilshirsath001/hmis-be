package com.suma.hmis_service.models.billing;

import com.suma.hmis_service.entities.EAcknowledgementStatus;
import com.suma.hmis_service.entities.EBillStatus;
import com.suma.hmis_service.entities.EPaymentStatus;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillingDto {
    private Long claimId;

    private String policyNumber;

//    private String name;

    private String insurer;

    private String hospitalName;

    private double amount;

    private EBillStatus status;

    private EAcknowledgementStatus acknowledgementStatus;

    private String transactionNumber;

    private String disposition;

    private String outcome;

    private LocalDate date;

//    private String mobileNumber;

    private LocalDate admitDate;

    private LocalDate policyStartDate;

    private LocalDate policyEndDate;

    private LocalDate reconsiliationStartDate;

    private LocalDate reconsiliationEndDate;

    private EPaymentStatus paymentStatus;
}
