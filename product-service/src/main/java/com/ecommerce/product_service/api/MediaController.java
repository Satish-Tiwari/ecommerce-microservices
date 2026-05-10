package com.ecommerce.product_service.api;

import com.ecommerce.product_service.entity.Media;
import com.ecommerce.product_service.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media")
public class MediaController {

    private final MediaService mediaService;

    /**
     * Upload single or multiple media files.
     *
     * Supports:
     * - single image
     * - multiple images
     *
     * Example:
     * files=image1.png
     * files=image2.png
     */
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<List<Media>> uploadMedia(
            @RequestPart("files")
            List<MultipartFile> files
    ) {

        log.info(
                "POST /api/media :: Uploading {} file(s)",
                files.size()
        );

        List<Media> uploadedMedia =
                mediaService.saveFiles(files);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(uploadedMedia);
    }

    /**
     * Delete media by ID.
     */
    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> deleteMedia(
            @PathVariable String mediaId
    ) {

        log.info(
                "DELETE /api/media/{}",
                mediaId
        );

        mediaService.deleteMedia(mediaId);

        return ResponseEntity.noContent().build();
    }

    /**
     * Generate public URL from relative path.
     *
     * Example:
     * /uploads/2026/05/abcd1234/file.webp
     */
    @GetMapping("/public-url")
    public ResponseEntity<Map<String, String>> getPublicUrl(
            @RequestParam String path
    ) {

        log.info(
                "GET /api/media/public-url :: {}",
                path
        );

        return ResponseEntity.ok(
                Map.of(
                        "url",
                        mediaService.getFullUrl(path)
                )
        );
    }

    /**
     * Trigger garbage collection manually.
     *
     * Useful for:
     * - admin cleanup
     * - testing
     * - cron verification
     */
    @PostMapping("/garbage-collect")
    public ResponseEntity<Map<String, String>> garbageCollect() {

        log.warn(
                "POST /api/media/garbage-collect"
        );

        mediaService.garbageCollect();

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Media garbage collection completed successfully"
                )
        );
    }
}