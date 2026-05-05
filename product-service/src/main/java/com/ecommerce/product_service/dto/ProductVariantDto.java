package com.ecommerce.product_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductVariantDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String sku;
    private String name;
    private BigDecimal price;
    private Integer stockQuantity;
}
