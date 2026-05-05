package com.ecommerce.product_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // ─── Identity ────────────────────────────────────────────────────────────
    private String id;
    private String sku;
    private String slug;
    private String externalId;

    // ─── Classification ──────────────────────────────────────────────────────
    private String productType;       // enum as string: PHYSICAL, DIGITAL, etc.
    private String categoryId;        // convenience: just the category UUID for create/update

    @JsonProperty("category")
    private CategoryDto categoryDto;  // expanded category on read

    private String brand;
    private String manufacturer;

    // ─── Core Content ────────────────────────────────────────────────────────
    private String name;
    private String shortDescription;
    private String description;

    // ─── Pricing ─────────────────────────────────────────────────────────────
    private BigDecimal price;
    private BigDecimal salePrice;
    private BigDecimal costPrice;
    private BigDecimal compareAtPrice;
    private String currencyCode;
    private Boolean taxInclusive;

    // ─── Inventory ───────────────────────────────────────────────────────────
    private Integer stockQuantity;
    private Integer reservedQuantity;
    private String stockStatus;       // enum as string: IN_STOCK, LOW_STOCK, etc.
    private Boolean backorderAllowed;

    // ─── Shipping ────────────────────────────────────────────────────────────
    private BigDecimal weightGrams;
    private Boolean requiresShipping;

    // ─── Visibility & Lifecycle ──────────────────────────────────────────────
    private String status;            // enum as string: DRAFT, ACTIVE, etc.
    private Instant publishedAt;
    private Boolean featured;

    // ─── Ratings ─────────────────────────────────────────────────────────────
    private BigDecimal averageRating;
    private Integer reviewCount;

    // ─── Computed (read-only) ────────────────────────────────────────────────
    private BigDecimal effectivePrice;
    private Integer availableStock;
    private Boolean onSale;

    // ─── Relations (nested DTOs, only populated on detail endpoints) ─────────
    @JsonProperty("meta")
    private List<ProductMetaDto> metaDtos;

    @JsonProperty("variants")
    private List<ProductVariantDto> variantDtos;

    @JsonProperty("images")
    private List<ProductImageDto> imageDtos;

    @JsonProperty("documents")
    private List<ProductDocumentDto> documentDtos;

    @JsonProperty("reviews")
    private List<ProductReviewDto> reviewDtos;

    // ─── Audit ───────────────────────────────────────────────────────────────
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private Long version;
}
