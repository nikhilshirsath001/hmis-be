package com.suma.hmis_service.models.policy;
import lombok.*;
import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlicyRequest {

      private String insuranceCompany;

        private String policyNumber;

        private String policyType;

        private Date policyStartDate;

        private Date policyEndDate;

        private String memberId;

        private String relationship;
    }

