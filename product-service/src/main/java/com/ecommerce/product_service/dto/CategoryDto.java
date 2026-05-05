package com.ecommerce.product_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // ─── Identity ────────────────────────────────────────────────────────────
    private String id;
    private String categoryTitle;

    // ─── Image ───────────────────────────────────────────────────────────────
    private String imageUrl;
    private String imageName;
    private String contentType;

    // ─── Hierarchy ───────────────────────────────────────────────────────────
    @JsonProperty("parentCategory")
    private CategoryDto parentCategoryDto;

    private String parentCategoryId;   // convenience: just the parent's UUID for create/update

    @JsonProperty("subCategories")
    private List<CategoryDto> subCategoryDtos;

    // ─── Nested products (optional, only returned on detail endpoints) ────────
    @JsonProperty("products")
    private List<ProductDto> productDtos;
}
