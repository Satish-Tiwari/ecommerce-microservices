package com.ecommerce.product_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Focused DTO for inventory-related API endpoints.
 * Exposes only the stock fields from the Product entity.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventoryDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String productId;
    private String sku;
    private Integer stockQuantity;
    private Integer reservedQuantity;
    private Integer availableStock;
    private String stockStatus;       // IN_STOCK, LOW_STOCK, OUT_OF_STOCK, ON_BACKORDER, ON_PREORDER
    private Boolean backorderAllowed;
}
