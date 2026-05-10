package com.ecommerce.product_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductImageDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String imageUrl;
    private String imageName;
    private String contentType;
    private Integer sortOrder;  // derived from @OrderColumn in Product entity
}
