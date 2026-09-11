package com.suma.hmis_service.services.document;

import com.suma.hmis_service.entities.Patient;
import com.suma.hmis_service.exceptions.BadRequestException;
import com.suma.hmis_service.exceptions.ResourceNotFoundException;
import com.suma.hmis_service.models.constants.ApiConstant;
import com.suma.hmis_service.entities.PatientAttachment;
import com.suma.hmis_service.models.document.PatientAttachmentResponse;
import com.suma.hmis_service.models.document.PatientDocumentType;
import com.suma.hmis_service.repositories.DocumentRepository;
import com.suma.hmis_service.repositories.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService{


    private final DocumentRepository documentRepository;
    private final PatientRepository patientRepository;
    private final FileStorageService fileStorageService;
    private final ModelMapper modelMapper;

    public List<PatientAttachment> uploadDocuments(Long patientId, Map<String, MultipartFile> files) {
        Patient patient = getPatient(patientId);
        return storeDocuments(patient,files);
    }

    public List<PatientAttachment> uploadDocuments(Patient patient, Map<String, MultipartFile> files) {
        return storeDocuments(patient,files);
    }

    private List<PatientAttachment> storeDocuments(Patient patient, Map<String, MultipartFile> files) {

        Set<String> requests = files.keySet().stream()
                .filter(Objects::nonNull)
                .filter(str -> !str.isBlank())
                .collect(Collectors.toSet());

        if (requests.isEmpty()) {
            throw new BadRequestException("No documents supplied");
        }
        if (requests.size() > 20) {
            throw new BadRequestException("Maximum 20 documents per request");
        }
        List<String> storedPaths = new ArrayList<>();

        try {
            List<PatientAttachment> attachments = new ArrayList<>();

            for (String request : requests) {
                MultipartFile file = files.get(request);

                if (file == null) {
                    throw new ResourceNotFoundException("File not found: " + request);
                }
                fileStorageService.validate(file);
                PatientDocumentType patientDocumentType = PatientDocumentType.fromValue(request);
                FileStorageService.StoredFile stored = fileStorageService.store(patient.getId(), patientDocumentType.name(), file);
                storedPaths.add(stored.relativePath());
                PatientAttachment attachment =
                        PatientAttachment.builder()
                                .fileName(stored.originalFileName())
                                .storedFileName(stored.storedFileName())
                                .contentType(stored.contentType())
                                .fileSize(stored.size())
                                .filePath(stored.relativePath())
                                .documentType(patientDocumentType).patient(patient).build();
                attachments.add(attachment);
            }
            return documentRepository.saveAll(attachments);
        } catch (RuntimeException e) {
            for (String path : storedPaths) {
                try {
                    fileStorageService.delete(path);
                } catch (Exception ignored) {
                }
            }
            throw e;
        }
    }
    private Patient getPatient(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Patient not found: " + patientId)
                );
    }

    public List<PatientAttachmentResponse> getAllDocuments(Long patientId) {
        getPatient(patientId);
        List<PatientAttachment> attachments = getAttachmentsByPatientId(patientId);
        return attachments.stream()
                .map(attachment -> new PatientAttachmentResponse(
                        attachment.getId(),
                        attachment.getFileName(),
                        attachment.getContentType(),
                        attachment.getFileSize(),
                        attachment.getDocumentType(),
                        createDocumentPath(patientId, attachment.getId())))
                .toList();
    }

    public PatientAttachmentResponse getDocument(Long patientId, String attachmentId) {
        PatientAttachment attachment = getPatientAttachmentOrElseThrow(patientId, attachmentId);
        return modelMapper.map(attachment,PatientAttachmentResponse.class);
    }

    public PatientAttachment getAttachmentForDownload(Long patientId, String attachmentId) {
        PatientAttachment attachment = getPatientAttachmentOrElseThrow(patientId, attachmentId);
        Path path = fileStorageService.load(attachment.getFilePath());
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            throw new ResourceNotFoundException("Physical file not found");
        }
        return attachment;
    }

    private PatientAttachment getPatientAttachmentOrElseThrow(Long patientId, String attachmentId) {
        return documentRepository.findByIdAndPatientId(attachmentId, patientId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Document not found")
                );
    }

    public Path getDocumentPath(Long patientId, String attachmentId) {
        PatientAttachment attachment = getPatientAttachmentOrElseThrow(patientId, attachmentId);
        Path path = fileStorageService.load(attachment.getFilePath());
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            throw new ResourceNotFoundException("Physical file not found");
        }
        return path;
    }

    private String createDocumentPath(Long patientId, String attachmentId) {
        return ApiConstant.Controller.DOCUMENT + "/" + patientId + "/attachments/" + attachmentId + "/download";
    }

    public void deleteDocumentAgainstUser(Long patientId) {
        List<PatientAttachment> attachments = getAttachmentsByPatientId(patientId);
        documentRepository.deleteAll(attachments);
        fileStorageService.deleteEntierFolder(String.valueOf(patientId));
    }

    private List<PatientAttachment> getAttachmentsByPatientId(Long patientId) {
        return documentRepository.findByPatientId(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Attachment not found: " + patientId));
    }

    public void deleteDocumentsAgainstUser(Long patientId, String attachmentId) {
        PatientAttachment attachment =
                documentRepository.findByPatientIdAndId(patientId, attachmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Attachment not found: " + attachmentId));
        String filePath = attachment.getFilePath();
        documentRepository.delete(attachment);
        if (filePath != null) {
            fileStorageService.delete(filePath);
        }
    }

    public PatientAttachmentResponse updateDocument(Long patientId, String attachmentId, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File cannot be empty");
        }
        PatientAttachment attachment =
                documentRepository.findByPatientIdAndId(patientId, attachmentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Attachment not found: " + attachmentId));

        String oldFilePath = attachment.getFilePath();
        String newFilePath = fileStorageService.store(file,oldFilePath);
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFilePath(newFilePath);
        attachment.setContentType(file.getContentType());
        attachment.setFileSize(file.getSize());

        PatientAttachment updated = documentRepository.save(attachment);

        if (oldFilePath != null &&
                !oldFilePath.equals(newFilePath)) {
            fileStorageService.delete(oldFilePath);
        }
        return modelMapper.map(updated, PatientAttachmentResponse.class);
    }

}
