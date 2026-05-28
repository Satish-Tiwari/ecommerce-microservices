package com.ecommerce.product_service.dto;

import java.io.Serial;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductExcelRowDto {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String sku;
    private String slug;
    private String externalId;

}
