package com.suma.hmis_service.feature.preauth;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PreAuthResponse {

    private String message;
    private PreAuthPayload payload;
}
