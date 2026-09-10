package com.suma.hmis_service.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.suma.hmis_service.models.document.PatientDocumentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(
        name = "patient_attachments",
        indexes = {
                @Index(name = "idx_patient_attachment_patient",
                        columnList = "patient_id"),
                @Index(name = "idx_patient_attachment_type",
                        columnList = "document_type")
        }
)
public class PatientAttachment {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "stored_file_name", nullable = false)
    private String storedFileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "file_path", nullable = false, length = 1000)
    private String filePath;


    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 50)
    private PatientDocumentType documentType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonBackReference
    private Patient patient;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
