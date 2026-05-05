package com.ecommerce.product_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

/**
 * Core Product Entity
 *
 * All dynamic / type-specific data (SEO, dimensions, digital download info,
 * subscription details, custom attributes, etc.) lives in {@link ProductMeta}.
 * This table stays lean and fast to query.
 */
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_product_sku", columnList = "sku", unique = true),
        @Index(name = "idx_product_slug", columnList = "slug", unique = true),
        @Index(name = "idx_product_status", columnList = "status"),
        @Index(name = "idx_product_type", columnList = "product_type"),
        @Index(name = "idx_product_brand", columnList = "brand"),
        @Index(name = "idx_product_category", columnList = "category_id"),
        @Index(name = "idx_product_created", columnList = "created_at"),
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "metaData", "variants", "images", "documents", "reviews" })
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {
    // ─── Identity ────────────────────────────────────────────────────────────
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, length = 36)
    @EqualsAndHashCode.Include
    private String id;

    /** Human-readable unique key e.g. "NIKE-AM90-WHT-10" */
    @Column(name = "sku", nullable = false, unique = true, length = 100)
    private String sku;

    /** URL-friendly identifier e.g. "nike-air-max-90-white" */
    @Column(name = "slug", nullable = false, unique = true, length = 255)
    private String slug;

    /** External / ERP reference (UPC, EAN, GTIN, ASIN, ISBN …) */
    @Column(name = "external_id", length = 100)
    private String externalId;

    // ─── Classification ──────────────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false, length = 30)
    private ProductType productType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    // ─── Core Content ────────────────────────────────────────────────────────
    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Column(name = "short_description", length = 1000)
    private String shortDescription;

    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // ─── Pricing ─────────────────────────────────────────────────────────────
    @Column(name = "price", nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @Column(name = "sale_price", precision = 19, scale = 4)
    private BigDecimal salePrice;

    @Column(name = "cost_price", precision = 19, scale = 4)
    private BigDecimal costPrice;

    @Column(name = "compare_at_price", precision = 19, scale = 4)
    private BigDecimal compareAtPrice;

    @Column(name = "currency_code", length = 3)
    @Builder.Default
    private String currencyCode = "USD";

    @Column(name = "tax_inclusive")
    @Builder.Default
    private Boolean taxInclusive = false;

    // ─── Inventory ───────────────────────────────────────────────────────────
    @Column(name = "stock_quantity")
    @Builder.Default
    private Integer stockQuantity = 0;

    @Column(name = "reserved_quantity")
    @Builder.Default
    private Integer reservedQuantity = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "stock_status", length = 20)
    @Builder.Default
    private StockStatus stockStatus = StockStatus.OUT_OF_STOCK;

    @Column(name = "backorder_allowed")
    @Builder.Default
    private Boolean backorderAllowed = false;

    // ─── Shipping ────────────────────────────────────────────────────────────
    @Column(name = "weight_grams", precision = 10, scale = 2)
    private BigDecimal weightGrams;

    @Column(name = "requires_shipping")
    @Builder.Default
    private Boolean requiresShipping = true;

    // ─── Visibility & Lifecycle ──────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ProductStatus status = ProductStatus.DRAFT;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "featured")
    @Builder.Default
    private Boolean featured = false;

    // ─── Ratings ─────────────────────────────────────────────────────────────
    @Column(name = "average_rating", precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "review_count")
    @Builder.Default
    private Integer reviewCount = 0;

    // ─── Relations ───────────────────────────────────────────────────────────

    /** All dynamic/type-specific attributes stored as key-value pairs. */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductMeta> metaData = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductVariant> variants = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderColumn(name = "sort_order")
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductDocument> documents = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductReview> reviews = new ArrayList<>();

    // ─── Audit ───────────────────────────────────────────────────────────────
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_by", updatable = false, length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Version
    @Column(name = "version")
    private Long version;

    // ─── Enums ───────────────────────────────────────────────────────────────
    public enum ProductType {
        PHYSICAL, DIGITAL, SUBSCRIPTION, BUNDLE, GIFT_CARD, SERVICE, VIRTUAL
    }

    public enum ProductStatus {
        DRAFT, PENDING_REVIEW, ACTIVE, INACTIVE, ARCHIVED, DISCONTINUED
    }

    public enum StockStatus {
        IN_STOCK, LOW_STOCK, OUT_OF_STOCK, ON_BACKORDER, ON_PREORDER
    }

    // ─── Computed helpers ────────────────────────────────────────────────────
    @Transient
    public boolean isOnSale() {
        return salePrice != null
                && salePrice.compareTo(BigDecimal.ZERO) > 0
                && status == ProductStatus.ACTIVE;
    }

    @Transient
    public BigDecimal getEffectivePrice() {
        return isOnSale() ? salePrice : price;
    }

    @Transient
    public int getAvailableStock() {
        return Math.max(0,
                (stockQuantity == null ? 0 : stockQuantity) -
                        (reservedQuantity == null ? 0 : reservedQuantity));
    }

    // ─── Convenience mutators ────────────────────────────────────────────────
    public void addMeta(ProductMeta meta) {
        meta.setProduct(this);
        this.metaData.add(meta);
    }

    /** Shorthand: add a meta entry directly from key + value strings. */
    public void addMeta(String key, String value) {
        addMeta(ProductMeta.builder().metaKey(key).metaValue(value).build());
    }

    /** Find the first meta value for a given key. */
    @Transient
    public Optional<String> getMeta(String key) {
        return metaData.stream()
                .filter(m -> m.getMetaKey().equals(key))
                .map(ProductMeta::getMetaValue)
                .findFirst();
    }

    public void addVariant(ProductVariant v) {
        v.setProduct(this);
        variants.add(v);
    }

    public void addImage(ProductImage img) {
        img.setProduct(this);
        images.add(img);
    }

    public void addDocument(ProductDocument d) {
        d.setProduct(this);
        documents.add(d);
    }
}