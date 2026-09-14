package com.suma.hmis_service.entities;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="policy")
public class Policy {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long policyId;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(name = "insurance_company")
    private String insuranceCompany ;

    @Column(name = "policy_number")
    private String policyNumber;

    @Column(name="policy_type")
    private String policyType;

    @Column(name="policy_start_date")
    private Date policyStartDate;

    @Column(name="policy_end_date")
    private Date policyEndDate;

    @Column(name="member_id")
    private String memberId;

    @Column(name="relationship")
    private String relationship;

}
