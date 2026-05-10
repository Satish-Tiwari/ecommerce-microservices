package com.ecommerce.product_service.service.impl;

import com.ecommerce.product_service.entity.Media;
import com.ecommerce.product_service.exception.wrapper.InvalidMediaException;
import com.ecommerce.product_service.exception.wrapper.MediaNotFoundException;
import com.ecommerce.product_service.exception.wrapper.MediaUploadException;
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
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private final MediaRepository mediaRepository;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    @Value("${app.base-url:http://localhost:8086}")
    private String baseUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Media> saveFiles(List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {

            throw new InvalidMediaException(
                    "No files uploaded"
            );
        }

        List<Media> uploadedMedia = new ArrayList<>();
        List<Path> uploadedPaths = new ArrayList<>();

        LocalDate now = LocalDate.now();

        String yearMonth =
                now.format(
                        DateTimeFormatter.ofPattern("yyyy/MM")
                );

        String batchId =
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8);

        String uploadFolder =
                yearMonth + "/" + batchId;

        Path targetDirectory =
                Paths.get(uploadDir, uploadFolder);

        try {

            Files.createDirectories(targetDirectory);

            for (MultipartFile file : files) {

                validateFile(file);

                String originalFilename =
                        sanitizeFileName(
                                file.getOriginalFilename()
                        );

                String extension =
                        extractExtension(originalFilename);

                String storedFileName =
                        UUID.randomUUID() + extension;

                Path targetFile =
                        targetDirectory.resolve(storedFileName);

                try (InputStream inputStream =
                             file.getInputStream()) {

                    Files.copy(
                            inputStream,
                            targetFile,
                            StandardCopyOption.REPLACE_EXISTING
                    );
                }

                uploadedPaths.add(targetFile);

                String relativePath =
                        "/uploads/" +
                        uploadFolder +
                        "/" +
                        storedFileName;

                Media media = Media.builder()
                        .originalFileName(originalFilename)
                        .storedFileName(storedFileName)
                        .fileType(file.getContentType())
                        .fileSize(file.getSize())
                        .batchId(batchId)
                        .filePath(relativePath)
                        .publicUrl(getFullUrl(relativePath))
                        .build();

                uploadedMedia.add(
                        mediaRepository.save(media)
                );
            }

            log.info(
                    "Uploaded {} files successfully [batchId={}]",
                    uploadedMedia.size(),
                    batchId
            );

            return uploadedMedia;

        } catch (Exception exception) {

            uploadedPaths.forEach(this::rollbackPhysicalFile);

            cleanupDirectory(targetDirectory);

            log.error(
                    "Failed to upload media files",
                    exception
            );

            throw new MediaUploadException(
                    "Failed to upload media files",
                    exception
            );
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMedia(String mediaId) {

        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() ->
                        new MediaNotFoundException(
                                "Media not found with ID: " + mediaId
                        )
                );

        try {

            mediaRepository.delete(media);

            deletePhysicalFile(media.getFilePath());

            cleanupBatchDirectory(media.getBatchId());

            log.info(
                    "Media deleted successfully [{}]",
                    media.getFilePath()
            );

        } catch (Exception exception) {

            log.error(
                    "Failed to delete media [{}]",
                    mediaId,
                    exception
            );

            throw new MediaUploadException(
                    "Failed to delete media",
                    exception
            );
        }
    }

    @Override
    public void deletePhysicalFile(String relativePath) {

        if (relativePath == null || relativePath.isBlank()) {
            return;
        }

        try {

            String cleanPath =
                    relativePath.replace("/uploads/", "");

            Path filePath =
                    Paths.get(uploadDir)
                            .resolve(cleanPath)
                            .normalize();

            Files.deleteIfExists(filePath);

            log.info(
                    "Physical file deleted [{}]",
                    relativePath
            );

        } catch (IOException exception) {

            log.error(
                    "Failed to delete file [{}]",
                    relativePath,
                    exception
            );
        }
    }

    @Override
    public String getFullUrl(String relativePath) {

        if (relativePath == null || relativePath.isBlank()) {
            return null;
        }

        if (relativePath.startsWith("http")) {
            return relativePath;
        }

        return baseUrl +
                (relativePath.startsWith("/") ? "" : "/") +
                relativePath;
    }

    @Override
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void garbageCollect() {

        log.info("Starting media garbage collection");

        List<Media> orphanedMedia =
                mediaRepository.findOrphanedMedia();

        for (Media media : orphanedMedia) {

            try {

                deletePhysicalFile(media.getFilePath());

                mediaRepository.delete(media);

                cleanupBatchDirectory(media.getBatchId());

                log.warn(
                        "Deleted orphaned media [{}]",
                        media.getId()
                );

            } catch (Exception exception) {

                log.error(
                        "Failed to cleanup orphaned media [{}]",
                        media.getId(),
                        exception
                );
            }
        }

        log.info("Media garbage collection completed");
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {

            throw new InvalidMediaException(
                    "Uploaded file is empty"
            );
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                !ALLOWED_CONTENT_TYPES.contains(contentType)) {

            throw new InvalidMediaException(
                    "Unsupported file type: " + contentType
            );
        }
    }

    private String sanitizeFileName(String fileName) {

        if (fileName == null || fileName.isBlank()) {
            return "unknown";
        }

        return Paths.get(fileName)
                .getFileName()
                .toString()
                .replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private String extractExtension(String fileName) {

        if (fileName == null || !fileName.contains(".")) {
            return "";
        }

        return fileName.substring(
                fileName.lastIndexOf(".")
        );
    }

    private void rollbackPhysicalFile(Path path) {

        if (path == null) {
            return;
        }

        try {

            Files.deleteIfExists(path);

            log.warn(
                    "Rolled back uploaded file [{}]",
                    path
            );

        } catch (IOException exception) {

            log.error(
                    "Failed to rollback file [{}]",
                    path,
                    exception
            );
        }
    }

    private void cleanupBatchDirectory(String batchId) {

        if (batchId == null || batchId.isBlank()) {
            return;
        }

        try {

            LocalDate now = LocalDate.now();

            String yearMonth =
                    now.format(
                            DateTimeFormatter.ofPattern("yyyy/MM")
                    );

            Path batchDirectory =
                    Paths.get(
                            uploadDir,
                            yearMonth,
                            batchId
                    );

            cleanupDirectory(batchDirectory);

        } catch (Exception exception) {

            log.error(
                    "Failed to cleanup batch directory [{}]",
                    batchId,
                    exception
            );
        }
    }

    private void cleanupDirectory(Path directory) {

        try {

            if (directory == null ||
                    !Files.exists(directory) ||
                    !Files.isDirectory(directory)) {

                return;
            }

            try (DirectoryStream<Path> stream =
                         Files.newDirectoryStream(directory)) {

                if (!stream.iterator().hasNext()) {

                    Files.deleteIfExists(directory);

                    log.info(
                            "Deleted empty directory [{}]",
                            directory
                    );
                }
            }

        } catch (Exception exception) {

            log.error(
                    "Failed to cleanup directory [{}]",
                    directory,
                    exception
            );
        }
    }
}