package com.ecommerce.product_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Product Meta — key/value store for all dynamic product attributes.
 *
 * Replaces every "nullable extension column" that would otherwise bloat the
 * products table. Any attribute that is product-type-specific, optional, or
 * likely to evolve goes here instead.
 *
 * ─── Common meta_key conventions ─────────────────────────────────────────────
 *
 * SEO
 * meta_title → "Best Nike Air Max 90 – Buy Online"
 * meta_description → "Shop the iconic Air Max 90 …"
 * meta_keywords → "nike, air max, sneakers"
 * canonical_url → "https://store.com/products/nike-am90"
 * og_image_url → CDN / GCS URL for Open Graph image
 *
 * Physical dimensions
 * length_mm → "300"
 * width_mm → "200"
 * height_mm → "150"
 * shipping_class → "oversized"
 * hs_code → "6402910000"
 * country_of_origin → "VN"
 *
 * Digital product
 * download_url → "https://cdn.example.com/files/guide.pdf"
 * google_drive_file_id → "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgVE2upms"
 * download_limit → "5"
 * download_expiry_days → "30"
 * file_size_bytes → "2048000"
 * file_format → "PDF"
 *
 * Subscription
 * subscription_period → "MONTHLY"
 * subscription_period_interval → "1"
 * subscription_trial_days → "14"
 *
 * Tax / compliance
 * tax_class → "reduced_rate"
 * age_restriction → "18"
 * hazmat_class → "flammable"
 *
 * Ratings & reviews
 * reviews_allowed → "true"
 * visibility → "PUBLIC"
 * password → (hashed) for password-protected listings
 *
 * Inventory extras
 * low_stock_threshold → "10"
 * warehouse_location → "RACK-A-42"
 * preorder_allowed → "true"
 * preorder_date → "2025-12-01T00:00:00Z"
 *
 * Cassandra / search
 * cassandra_partition_key → "electronics#audio"
 * search_boost → "1.5"
 *
 * Google Drive document refs (repeatable via multiple rows)
 * gdrive_spec_sheet_id → "<Drive file ID>"
 * gdrive_manual_id → "<Drive file ID>"
 * gdrive_certificate_id → "<Drive file ID>"
 *
 * Merchant custom fields (arbitrary extensibility)
 * custom_* → any merchant-defined key
 *
 * ─────────────────────────────────────────────────────────────────────────────
 */
@Entity
@Table(name = "product_meta", indexes = {
        @Index(name = "idx_pmeta_product_id", columnList = "product_id"),
        @Index(name = "idx_pmeta_key", columnList = "meta_key"),
        // Composite: fast lookup of one key for one product
        @Index(name = "idx_pmeta_product_key", columnList = "product_id, meta_key"),
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "product")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductMeta {

    // ─── Identity ────────────────────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, length = 36)
    @EqualsAndHashCode.Include
    private String id;

    // ─── Relationship ────────────────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_pmeta_product"))
    private Product product;

    // ─── Payload ─────────────────────────────────────────────────────────────

    /**
     * Attribute key — use snake_case, dot-namespaced for grouping.
     * Examples: "meta_title", "download_url", "gdrive_manual_id",
     * "custom_color_hex"
     */
    @Column(name = "meta_key", nullable = false, length = 255)
    private String metaKey;

    /**
     * Attribute value — always stored as text.
     * Numbers, booleans, dates, and JSON arrays must be serialized by the caller.
     * Use TEXT (unlimited) to future-proof large values (rich content, JSON blobs).
     */
    @Lob
    @Column(name = "meta_value", columnDefinition = "TEXT")
    private String metaValue;

    // ─── Audit ───────────────────────────────────────────────────────────────

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    // ─── Static factory helpers ──────────────────────────────────────────────

    public static ProductMeta of(Product product, String key, String value) {
        return ProductMeta.builder()
                .product(product)
                .metaKey(key)
                .metaValue(value)
                .build();
    }
}
