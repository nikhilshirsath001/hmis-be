package com.suma.hmis_service.models.patient;
import com.suma.hmis_service.entities.EGender;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateContactPersonDto {
    private String name;

    private EGender gender;

    private String relation;

    private String mobileNo;

    private String address;

    private String abhaId;
}
