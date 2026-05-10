package com.ecommerce.product_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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

    private String id;

    @NotBlank(message = "Category title must not be blank")
    private String categoryTitle;

    private String imageUrl;
    private String imageName;
    private String contentType;

    @JsonProperty("parentCategory")
    private CategoryDto parentCategoryDto;

    private String parentCategoryId;  
    @JsonProperty("subCategories")
    private List<CategoryDto> subCategoryDtos;

    @JsonProperty("products")
    private List<ProductDto> productDtos;
}
