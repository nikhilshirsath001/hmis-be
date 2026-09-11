package com.suma.hmis_service.models.billing;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBillingRequest {

    private Long patientId;

}
