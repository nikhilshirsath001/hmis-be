package com.suma.hmis_service.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patient")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String name;
}
