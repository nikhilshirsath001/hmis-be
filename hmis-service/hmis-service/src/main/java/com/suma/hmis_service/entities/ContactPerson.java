package com.suma.hmis_service.entities;

import com.suma.hmis_service.models.patient.CreateContactPersonDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contact_person")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private EGender gender;

    private String relation;

    private String mobileNo;

    private String address;

    private String abhaId;

    public static ContactPerson toContactPersonFromContactPersonDto(CreateContactPersonDto obj) {
        return ContactPerson.builder()
                .name(obj.getName())
                .gender(obj.getGender())
                .relation(obj.getRelation())
                .mobileNo(obj.getMobileNo())
                .address(obj.getAddress())
                .abhaId(obj.getAbhaId())
                .build();
    }


}
