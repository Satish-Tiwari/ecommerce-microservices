package com.ecommerce.product_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Focused DTO for pricing-related API endpoints.
 * Exposes only the price fields from the Product entity.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PricingDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String productId;
    private String sku;
    private BigDecimal price;
    private BigDecimal salePrice;
    private BigDecimal costPrice;
    private BigDecimal compareAtPrice;
    private String currencyCode;
    private Boolean taxInclusive;
    private BigDecimal effectivePrice;  // computed: salePrice or price
    private Boolean onSale;             // computed
}
