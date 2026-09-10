package com.suma.hmis_service.controllers;

import com.suma.hmis_service.models.ApiResponse;
import com.suma.hmis_service.models.constants.ApiConstant;
import com.suma.hmis_service.entities.PatientAttachment;
import com.suma.hmis_service.services.document.DocumentService;
import com.suma.hmis_service.services.document.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ApiConstant.Controller.DOCUMENT)
@AllArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final FileStorageService fileStorageService;

    @PostMapping(value = "/{id}/attachments",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> uploadDocuments(
            @PathVariable Long id,
            MultipartHttpServletRequest request) {

        Map<String, MultipartFile> files = request.getFileMap();
        List<PatientAttachment> attachments = documentService.uploadDocuments(id, files);
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                "Document added successfully.",
                attachments
        ));
    }

    @GetMapping("/{id}/attachments")
    public ResponseEntity<ApiResponse> getDocuments(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                "Document list",
                documentService.getAllDocuments(id)
        ));
    }

    @GetMapping("/{patientId}/attachments/{attachmentId}")
    public ResponseEntity<ApiResponse> getDocument(
            @PathVariable String patientId, @PathVariable String attachmentId) {
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                "Document",
                documentService.getDocument(Long.valueOf(patientId), attachmentId)
        ));
    }

    @GetMapping("/{patientId}/attachments/{attachmentId}/download")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long patientId, @PathVariable String attachmentId) {

        PatientAttachment attachment = documentService.getAttachmentForDownload(patientId, attachmentId);
        Path path = fileStorageService.load(attachment.getFilePath());
        Resource resource = new FileSystemResource(path);
        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(attachment.getContentType());
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }
        return ResponseEntity.ok()
                .contentType(mediaType)
                .contentLength(attachment.getFileSize())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + attachment.getFileName() + "\"")
                .body(resource);
    }


    @PutMapping(
            value = "/{patientId}/attachments/{attachmentId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse> updateDocument(
            @PathVariable Long patientId,
            @PathVariable String attachmentId,
            @RequestPart("file") MultipartFile file) {

        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                "Document update successful.",
                documentService.updateDocument(patientId, attachmentId, file)
        ));
    }

    @DeleteMapping("/{patientId}/attachments")
    public ResponseEntity<Void> deleteDocumentAgainstUser(
            @PathVariable Long patientId) {

        documentService.deleteDocumentAgainstUser(patientId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{patientId}/attachments/{attachmentId}")
    public ResponseEntity<Void> deleteDocumentsAgainstUser(
            @PathVariable Long patientId,
            @PathVariable String attachmentId) {

        documentService.deleteDocumentsAgainstUser(patientId, attachmentId);
        return ResponseEntity.noContent().build();
    }

}
