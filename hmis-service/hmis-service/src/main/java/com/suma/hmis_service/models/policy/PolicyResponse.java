package com.suma.hmis_service.models.policy;

import com.suma.hmis_service.entities.Policy;
import lombok.*;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class PolicyResponse {



        private Long policyId;

        private String insuranceCompany;

        private String policyNumber;

        private String policyType;

        private Date policyStartDate;

        private Date policyEndDate;

        private String memberId;

        private String relationship;


        public static PolicyResponse toPolicyResponseUsingPolicy(Policy obj) {

            return PolicyResponse.builder()
                    .policyId(obj.getPolicyId())
                    .insuranceCompany(obj.getInsuranceCompany())
                    .policyNumber(obj.getPolicyNumber())
                    .policyType(obj.getPolicyType())
                    .policyStartDate(obj.getPolicyStartDate())
                    .policyEndDate(obj.getPolicyEndDate())
                    .memberId(obj.getMemberId())
                    .relationship(obj.getRelationship())
                    .build();
        }
    }