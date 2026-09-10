package com.suma.hmis_service.services.document;

import com.suma.hmis_service.entities.Patient;
import com.suma.hmis_service.entities.PatientAttachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface DocumentService {
    List<PatientAttachment> uploadDocuments(Long id, Map<String, MultipartFile> files);

    List<PatientAttachment> uploadDocuments(Patient patient, Map<String, MultipartFile> files);

    Object getAllDocuments(Long id);

    Object getDocument(Long aLong, String attachmentId);

    PatientAttachment getAttachmentForDownload(Long patientId, String attachmentId);

    Object updateDocument(Long patientId, String attachmentId, MultipartFile file);

    void deleteDocumentAgainstUser(Long patientId);

    void deleteDocumentsAgainstUser(Long patientId, String attachmentId);
}
