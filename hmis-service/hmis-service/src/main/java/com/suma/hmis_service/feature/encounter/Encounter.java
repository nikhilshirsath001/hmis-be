package com.suma.hmis_service.feature.encounter;

import com.suma.hmis_service.entities.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "encounters",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_encounter_number",
                        columnNames = "encounter_number"
                )
        },
        indexes = {
                @Index(
                        name = "idx_encounter_patient_id",
                        columnList = "patient_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Encounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "encounter_number",
            nullable = false,
            unique = true,
            length = 30
    )
    private String encounterNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "patient_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_encounter_patient"
            )
    )
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "encounter_type",
            nullable = false,
            length = 20
    )
    private EncounterType type;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private EncounterStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    private void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    private void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
