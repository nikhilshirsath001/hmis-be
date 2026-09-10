package com.suma.hmis_service.models.document;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientAttachmentResponse {

    private String id;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private PatientDocumentType documentType;
    private String downloadUrl;
}

