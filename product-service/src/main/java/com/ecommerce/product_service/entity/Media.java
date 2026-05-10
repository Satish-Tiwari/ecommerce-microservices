package com.ecommerce.product_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(
        name = "media",
        indexes = {
                @Index(name = "idx_media_created_at", columnList = "created_at"),
                @Index(name = "idx_media_stored_file_name", columnList = "stored_file_name"),
                @Index(name = "idx_media_batch_id", columnList = "batch_id")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Media implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    /**
     * Original filename uploaded by user.
     */
    @Column(
            name = "original_file_name",
            nullable = false,
            length = 255
    )
    private String originalFileName;

    /**
     * Internally generated unique filename.
     */
    @Column(
            name = "stored_file_name",
            nullable = false,
            unique = true,
            length = 255
    )
    private String storedFileName;

    /**
     * MIME type.
     * Example:
     * image/png
     */
    @Column(
            name = "file_type",
            nullable = false,
            length = 100
    )
    private String fileType;

    /**
     * File size in bytes.
     */
    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    /**
     * Relative storage path.
     * Example:
     * /uploads/2026/05/a1b2c3d4/image.webp
     */
    @Column(
            name = "file_path",
            nullable = false,
            length = 500
    )
    private String filePath;

    /**
     * Upload batch identifier.
     * Files uploaded together
     * share the same batchId.
     */
    @Column(
            name = "batch_id",
            nullable = false,
            length = 100
    )
    private String batchId;

    /**
     * Public accessible URL.
     */
    @Column(name = "public_url", length = 1000)
    private String publicUrl;

    /**
     * Soft delete support.
     */
    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private Instant createdAt;
}