package com.suma.hmis_service.services.document;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import static com.suma.hmis_service.models.constants.ApiConstant.Document.*;

@Slf4j
@Service
public class FileStorageService {

    private final Path rootLocation;
    private final Tika tika = new Tika();


    public FileStorageService() {
        this.rootLocation = Paths.get(ROOT_FOLDER).toAbsolutePath().normalize();
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new IllegalStateException("Could not initialize file storage", e);
        }
    }

    public StoredFile store(Long patientId, String documentType, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        String originalFileName =
                StringUtils.cleanPath(file.getOriginalFilename() == null ?
                        "document" :
                        file.getOriginalFilename());
        String extension = getExtension(originalFileName);
        String storedFileName = UUID.randomUUID() + extension;
        Path patientDirectory = rootLocation.resolve(String.valueOf(patientId)).resolve(documentType).normalize();
        try {

            Files.createDirectories(patientDirectory);
            Path target = patientDirectory.resolve(storedFileName).normalize();
            if (!target.startsWith(patientDirectory)) {
                throw new SecurityException("Invalid file path");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            }
            String relativePath = rootLocation.relativize(target).toString().replace("\\", "/");
            return new StoredFile(originalFileName, storedFileName,
                    relativePath, file.getContentType(), file.getSize());
        } catch (IOException e) {
            throw new RuntimeException("Failed to store patient document", e);
        }
    }

    public String store( MultipartFile file, String oldFilePath) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Cannot store empty file");
        }
        try {
            String originalFileName =
                    StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String extension = "";
            int dotIndex = originalFileName.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = originalFileName.substring(dotIndex);
            }
            String generatedFileName = UUID.randomUUID() + extension;
            String substring = oldFilePath.substring(0, oldFilePath.lastIndexOf("\\"));
            Path patientDirectory = rootLocation.resolve(substring);
            Files.createDirectories(patientDirectory);
            Path targetPath = patientDirectory.resolve(generatedFileName).normalize();
            if (!targetPath.startsWith(patientDirectory)) {
                throw new RuntimeException("Invalid file path");
            }
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return rootLocation.relativize(targetPath).toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public Path load(String relativePath) {
        Path file = rootLocation.resolve(relativePath).normalize();
        if (!file.startsWith(rootLocation)) {
            throw new SecurityException("Invalid file path");
        }
        return file;
    }

    public void delete(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return;
        }
        try {
            Path file = rootLocation.resolve(relativePath).normalize();
            if (!file.startsWith(rootLocation)) {
                throw new SecurityException("Invalid file path");
            }
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    public void deleteEntierFolder(String id) {

        Path patientDirectory = rootLocation.resolve(id).normalize();
        if (!patientDirectory.startsWith(rootLocation)) {
            throw new IllegalArgumentException("Invalid patient directory");
        }

        if (!Files.exists(patientDirectory)) {
            log.info("Patient folder does not exist: {}", patientDirectory);
            return;
        }
        try (Stream<Path> paths = Files.walk(patientDirectory)) {
            paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                            log.info("Deleted: {}", path);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to delete: " + path, e);
                        }
                    });

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete patient folder: " + id, e);
        }
    }

    private String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return "";
        }
        return filename.substring(index).toLowerCase();
    }

    public record StoredFile(String originalFileName, String storedFileName,
                             String relativePath, String contentType,
                             long size) {
    }

    public void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Maximum file size is 20 MB");
        }
        try {
            String detectedType = tika.detect(file.getInputStream());
            if (!ALLOWED_TYPES.contains(detectedType)) {
                throw new IllegalArgumentException("Unsupported file type: " + detectedType);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not inspect uploaded file", e);
        }
    }
}
