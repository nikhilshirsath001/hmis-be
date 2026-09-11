package com.suma.hmis_service.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "billing")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

//    private LocalDate date;

//    private String mobileNumber;

    private LocalDate admitDate;

    private LocalDate policyStartDate;

    private LocalDate policyEndDate;

    private LocalDate reconsiliationStartDate;

    private LocalDate reconsiliationEndDate;

    private EPaymentStatus paymentStatus;



    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    private Patient patient;

}
