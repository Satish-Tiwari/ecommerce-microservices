package com.ecommerce.product_service.helper;

import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.dto.CategoryDto;

import java.util.Optional;

public interface CategoryMappingHelper {
    /**
     * Maps a Category entity to a CategoryDto.
     * Includes a shallow parent (no recursive nesting).
     */
    static CategoryDto map(final Category category) {
        final var parentCategory = Optional.ofNullable(category.getParentCategory())
                .orElse(null);

        CategoryDto.CategoryDtoBuilder builder = CategoryDto.builder()
                .id(category.getId())
                .categoryTitle(category.getCategoryTitle());

        if (category.getMedia() != null) {
            builder.imageUrl(category.getMedia().getFilePath())
                    .imageName(category.getMedia().getOriginalFileName())
                    .contentType(category.getMedia().getFileType());
        }

        if (parentCategory != null) {
            CategoryDto.CategoryDtoBuilder parentBuilder = CategoryDto.builder()
                    .id(parentCategory.getId())
                    .categoryTitle(parentCategory.getCategoryTitle());

            if (parentCategory.getMedia() != null) {
                parentBuilder.imageUrl(parentCategory.getMedia().getFilePath());
            }

            builder.parentCategoryDto(parentBuilder.build());
            builder.parentCategoryId(parentCategory.getId());
        }

        return builder.build();
    }

    /**
     * Maps a CategoryDto to a Category entity.
     * Does NOT resolve parentCategory — the service layer handles that.
     */
    static Category map(final CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .categoryTitle(categoryDto.getCategoryTitle())
                .build();
    }

    /**
     * Maps a Category entity to CategoryDto including all sub-categories
     * recursively.
     */
    static CategoryDto mapWithChildren(final Category category) {
        CategoryDto dto = map(category);
        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
            dto.setSubCategoryDtos(category.getSubCategories().stream()
                    .map(CategoryMappingHelper::mapWithChildren)
                    .toList());
        }
        return dto;
    }
}
