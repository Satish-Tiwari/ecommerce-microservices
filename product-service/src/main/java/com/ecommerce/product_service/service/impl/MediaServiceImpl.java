package com.ecommerce.product_service.service.impl;

import com.ecommerce.product_service.entity.Media;
import com.ecommerce.product_service.repository.MediaRepository;
import com.ecommerce.product_service.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final String uploadDir = "uploads";

    @Value("${app.base-url:http://localhost:8086}")
    private String baseUrl;

    @Override
    @Transactional
    public Media saveFile(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();

            // 1. Create WordPress-style directory structure: uploads/yyyy/MM/dd/
            LocalDate now = LocalDate.now();
            String subPath = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path targetDir = Paths.get(uploadDir, subPath);

            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            // 2. Generate a unique filename
            String extension = "";
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + extension;
            Path targetFile = targetDir.resolve(fileName);
            String relativePath = "/uploads/" + subPath + "/" + fileName;

            // 3. Save to disk
            Files.write(targetFile, bytes);

            // 4. Save to DB
            Media media = Media.builder()
                    .fileName(originalFilename)
                    .fileType(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                    .filePath(relativePath)
                    .data(bytes)
                    .build();

            Media savedMedia = mediaRepository.save(media);
            log.info("MediaService :: File saved successfully at [{}]", savedMedia.getFilePath());
            return savedMedia;

        } catch (IOException e) {
            log.error("MediaService :: Error saving file: {}", e.getMessage());
            throw new RuntimeException("Could not save file: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteMedia(String mediaId) {
        mediaRepository.findById(mediaId).ifPresent(media -> {
            String path = media.getFilePath();
            deleteFile(path);
            mediaRepository.delete(media);
            log.info("MediaService :: Media record and file deleted: {}", path);
        });
    }

    @Override
    public void deleteFile(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return;
        }

        try {
            String cleanPath = relativePath.startsWith("/uploads/")
                    ? relativePath.substring(9)
                    : relativePath;

            Path fileToDelete = Paths.get(uploadDir).resolve(cleanPath);
            if (Files.exists(fileToDelete)) {
                Files.delete(fileToDelete);
                log.info("MediaService :: Physical file deleted: {}", relativePath);
            }
        } catch (IOException e) {
            log.error("MediaService :: Could not delete file [{}]: {}", relativePath, e.getMessage());
        }
    }

    @Override
    public String getFullUrl(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) return null;
        if (relativePath.startsWith("http")) return relativePath;
        return baseUrl + (relativePath.startsWith("/") ? "" : "/") + relativePath;
    }

    @Override
    @Scheduled(cron = "0 0 2 * * *") // Every day at 2 AM
    public void garbageCollect() {
        log.info("MediaService :: Running Garbage Collector...");
        List<Media> orphanedMedia = mediaRepository.findOrphanedMedia();
        orphanedMedia.forEach(media -> {
            log.warn("MediaService :: Deleting orphaned media: {}", media.getFilePath());
            deleteMedia(media.getId());
        });
        log.info("MediaService :: Garbage Collection completed.");
    }
}
