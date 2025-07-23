package uz.idimzo.beck.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.idimzo.beck.exception.FileStorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_FORMATS = {
            "image/webp", "image/png", "image/jpeg",
            "video/mp4", "video/webm", "video/quicktime"
    };

    public String storeFile(MultipartFile file) {
        validateFile(file);
        
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = getFileExtension(originalFileName);
            String fileName = UUID.randomUUID() + "." + fileExtension;

            Path targetLocation = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file. Please try again!", ex);
        }
    }

    public void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("File cannot be empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !isAllowedContentType(contentType)) {
            throw new FileStorageException("Invalid file format");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileStorageException("File size exceeds maximum limit (10MB)");
        }
    }

    private boolean isAllowedContentType(String contentType) {
        for (String format : ALLOWED_FORMATS) {
            if (format.equals(contentType)) {
                return true;
            }
        }
        return false;
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}