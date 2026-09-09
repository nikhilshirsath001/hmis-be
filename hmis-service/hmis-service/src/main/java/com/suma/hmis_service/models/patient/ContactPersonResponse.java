package com.suma.hmis_service.models.patient;

import com.suma.hmis_service.entities.ContactPerson;
import com.suma.hmis_service.entities.EGender;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactPersonResponse {
    private String name;

    private EGender gender;

    private String relation;

    private String mobileNo;

    private String address;

    public static ContactPersonResponse toContactPersonResponseUsingContactPerson(ContactPerson obj) {
        return ContactPersonResponse.builder()
                .name(obj.getName())
                .gender(obj.getGender())
                .relation(obj.getRelation())
                .mobileNo(obj.getMobileNo())
                .address(obj.getAddress())
                .build();
    }

}
