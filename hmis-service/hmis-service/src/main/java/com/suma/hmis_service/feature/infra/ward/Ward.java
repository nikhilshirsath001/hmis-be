package com.suma.hmis_service.feature.infra.ward;

import com.suma.hmis_service.feature.infra.wardtype.WardType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "wards",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_ward_code",
                        columnNames = "code"
                ),
                @UniqueConstraint(
                        name = "uk_ward_name",
                        columnNames = "name"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ward_type_id", nullable = false)
    private WardType wardType;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}