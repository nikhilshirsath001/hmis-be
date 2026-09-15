package com.suma.hmis_service.feature.preauth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "pre_auth_payload",
        indexes = @Index(name = "idx_pre_auth_claim_id", columnList = "claim_id")
)
@Getter
@Setter
public class PreAuthPayloadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id")
    private String patientId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", columnDefinition = "jsonb", nullable = false)
    private String payload;

    @Column(name = "created_at",
            columnDefinition = "TIMESTAMPTZ NOT NULL DEFAULT now()",
            insertable = false, updatable = false)
    private OffsetDateTime createdAt;
}