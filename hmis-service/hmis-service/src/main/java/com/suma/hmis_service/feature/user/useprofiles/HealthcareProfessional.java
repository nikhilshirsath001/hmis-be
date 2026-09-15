package com.suma.hmis_service.feature.user.useprofiles;

import com.suma.hmis_service.feature.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "healthcare_professionals", uniqueConstraints = {
        @UniqueConstraint(name = "uk_healthcare_professionals_user_id", columnNames = "user_id"),
        @UniqueConstraint(name = "uk_healthcare_professionals_profession_id", columnNames = "healthcare_profession_id")})
public class HealthcareProfessional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_healthcare_professional_user"))
    private User user;

    @Column(name = "healthcare_profession_id", nullable = false, length = 50)
    private String healthcareProfessionId;

    @Column(nullable = false, length = 100)
    private String designation;

    @Column(nullable = false, length = 20)
    private String contactNumber;

    @Column(nullable = false, length = 200)
    private String qualification;

    @Column(length = 100)
    private String specialization;

    @Column(nullable = false)
    private boolean active;

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
