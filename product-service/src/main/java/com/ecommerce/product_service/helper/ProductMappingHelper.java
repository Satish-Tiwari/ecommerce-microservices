package com.ecommerce.product_service.helper;

import com.ecommerce.product_service.dto.*;
import com.ecommerce.product_service.entity.*;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public interface ProductMappingHelper {

    static ProductDto map(final Product product) {
        ProductDto.ProductDtoBuilder builder = ProductDto.builder()
                .id(product.getId())
                .sku(product.getSku())
                .slug(product.getSlug())
                .externalId(product.getExternalId())
                .productType(product.getProductType() != null ? product.getProductType().name() : null)
                .brand(product.getBrand())
                .manufacturer(product.getManufacturer())
                .name(product.getName())
                .shortDescription(product.getShortDescription())
                .description(product.getDescription())
                .price(product.getPrice())
                .salePrice(product.getSalePrice())
                .costPrice(product.getCostPrice())
                .compareAtPrice(product.getCompareAtPrice())
                .currencyCode(product.getCurrencyCode())
                .taxInclusive(product.getTaxInclusive())
                .stockQuantity(product.getStockQuantity())
                .reservedQuantity(product.getReservedQuantity())
                .stockStatus(product.getStockStatus() != null ? product.getStockStatus().name() : null)
                .backorderAllowed(product.getBackorderAllowed())
                .weightGrams(product.getWeightGrams())
                .requiresShipping(product.getRequiresShipping())
                .status(product.getStatus() != null ? product.getStatus().name() : null)
                .publishedAt(product.getPublishedAt())
                .featured(product.getFeatured())
                .averageRating(product.getAverageRating())
                .reviewCount(product.getReviewCount())
                .effectivePrice(product.getEffectivePrice())
                .availableStock(product.getAvailableStock())
                .onSale(product.isOnSale())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .createdBy(product.getCreatedBy())
                .updatedBy(product.getUpdatedBy())
                .version(product.getVersion());

        // Map Category
        if (product.getCategory() != null) {
            builder.categoryId(product.getCategory().getId());
            builder.categoryDto(CategoryMappingHelper.map(product.getCategory()));
        }

        // Map Images
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            builder.imageDtos(product.getImages().stream()
                    .map(ProductMappingHelper::mapImage)
                    .collect(Collectors.toList()));
        }

        return builder.build();
    }

    static ProductImageDto mapImage(final ProductImage image) {
        ProductImageDto.ProductImageDtoBuilder builder = ProductImageDto.builder()
                .id(image.getId());
        
        if (image.getMedia() != null) {
            builder.imageUrl(image.getMedia().getFilePath())
                    .imageName(image.getMedia().getFileName())
                    .contentType(image.getMedia().getFileType());
        }
        
        return builder.build();
    }

    static Product map(final ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .sku(productDto.getSku())
                .slug(productDto.getSlug())
                .externalId(productDto.getExternalId())
                .productType(productDto.getProductType() != null
                        ? Product.ProductType.valueOf(productDto.getProductType())
                        : null)
                .brand(productDto.getBrand())
                .manufacturer(productDto.getManufacturer())
                .name(productDto.getName())
                .shortDescription(productDto.getShortDescription())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .salePrice(productDto.getSalePrice())
                .costPrice(productDto.getCostPrice())
                .compareAtPrice(productDto.getCompareAtPrice())
                .currencyCode(productDto.getCurrencyCode())
                .taxInclusive(productDto.getTaxInclusive())
                .stockQuantity(productDto.getStockQuantity())
                .reservedQuantity(productDto.getReservedQuantity())
                .stockStatus(productDto.getStockStatus() != null
                        ? Product.StockStatus.valueOf(productDto.getStockStatus())
                        : null)
                .backorderAllowed(productDto.getBackorderAllowed())
                .weightGrams(productDto.getWeightGrams())
                .requiresShipping(productDto.getRequiresShipping())
                .status(productDto.getStatus() != null
                        ? Product.ProductStatus.valueOf(productDto.getStatus())
                        : null)
                .publishedAt(productDto.getPublishedAt())
                .featured(productDto.getFeatured())
                .createdBy(productDto.getCreatedBy())
                .updatedBy(productDto.getUpdatedBy())
                .build();
    }
}
