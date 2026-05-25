package com.ecommerce.product_service.helper;

import com.ecommerce.product_service.dto.*;
import com.ecommerce.product_service.entity.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public interface ProductMappingHelper {

        static ProductDto map(final Product product) {

                ProductDto.ProductDtoBuilder builder = ProductDto.builder()
                                .id(product.getId())
                                .sku(product.getSku())
                                .slug(product.getSlug())
                                .externalId(product.getExternalId())

                                // ENUMS
                                .productType(product.getProductType() != null
                                                ? product.getProductType().name()
                                                : null)

                                .stockStatus(product.getStockStatus() != null
                                                ? product.getStockStatus().name()
                                                : Product.StockStatus.OUT_OF_STOCK.name())

                                .status(product.getStatus() != null
                                                ? product.getStatus().name()
                                                : Product.ProductStatus.DRAFT.name())

                                // BASIC INFO
                                .brand(product.getBrand())
                                .manufacturer(product.getManufacturer())
                                .name(product.getName())
                                .shortDescription(product.getShortDescription())
                                .description(product.getDescription())

                                // PRICING
                                .price(product.getPrice())
                                .salePrice(product.getSalePrice())
                                .costPrice(product.getCostPrice())
                                .compareAtPrice(product.getCompareAtPrice())
                                .currencyCode(product.getCurrencyCode())
                                .taxInclusive(product.getTaxInclusive())

                                // INVENTORY
                                .stockQuantity(product.getStockQuantity())
                                .reservedQuantity(product.getReservedQuantity())
                                .backorderAllowed(product.getBackorderAllowed())

                                // SHIPPING
                                .weightGrams(product.getWeightGrams())
                                .requiresShipping(product.getRequiresShipping())

                                // VISIBILITY
                                .publishedAt(product.getPublishedAt())
                                .featured(product.getFeatured())

                                // RATINGS
                                .averageRating(product.getAverageRating())
                                .reviewCount(product.getReviewCount())

                                // COMPUTED
                                .effectivePrice(product.getEffectivePrice())
                                .availableStock(product.getAvailableStock())
                                .onSale(product.isOnSale())

                                // AUDIT
                                .createdAt(product.getCreatedAt())
                                .updatedAt(product.getUpdatedAt())
                                .createdBy(product.getCreatedBy())
                                .updatedBy(product.getUpdatedBy())
                                .version(product.getVersion());

                // CATEGORY
                if (product.getCategory() != null) {
                        builder.categoryId(product.getCategory().getId());
                        builder.categoryDto(CategoryMappingHelper.map(product.getCategory()));
                }

                // IMAGES
                if (product.getImages() != null && !product.getImages().isEmpty()) {
                        builder.imageDtos(
                                        product.getImages()
                                                        .stream()
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
                                        .imageName(image.getMedia().getOriginalFileName())
                                        .contentType(image.getMedia().getFileType());
                }

                return builder.build();
        }

        static Product map(final ProductDto productDto) {

                return Product.builder()

                                // IDS
                                .id(productDto.getId())
                                .sku(productDto.getSku())
                                .slug(productDto.getSlug())
                                .externalId(productDto.getExternalId())

                                // ENUMS
                                .productType(productDto.getProductType() != null
                                                ? Product.ProductType.valueOf(productDto.getProductType())
                                                : Product.ProductType.PHYSICAL)

                                .stockStatus(productDto.getStockStatus() != null
                                                ? Product.StockStatus.valueOf(productDto.getStockStatus())
                                                : Product.StockStatus.OUT_OF_STOCK)

                                .status(productDto.getStatus() != null
                                                ? Product.ProductStatus.valueOf(productDto.getStatus())
                                                : Product.ProductStatus.DRAFT)

                                // BASIC INFO
                                .brand(productDto.getBrand())
                                .manufacturer(productDto.getManufacturer())
                                .name(productDto.getName())
                                .shortDescription(productDto.getShortDescription())
                                .description(productDto.getDescription())

                                // PRICING
                                .price(productDto.getPrice())
                                .salePrice(productDto.getSalePrice())
                                .costPrice(productDto.getCostPrice())
                                .compareAtPrice(productDto.getCompareAtPrice())
                                .currencyCode(productDto.getCurrencyCode() != null
                                                ? productDto.getCurrencyCode()
                                                : "USD")

                                .taxInclusive(productDto.getTaxInclusive() != null
                                                ? productDto.getTaxInclusive()
                                                : false)

                                // INVENTORY
                                .stockQuantity(productDto.getStockQuantity() != null
                                                ? productDto.getStockQuantity()
                                                : 0)

                                .reservedQuantity(productDto.getReservedQuantity() != null
                                                ? productDto.getReservedQuantity()
                                                : 0)

                                .backorderAllowed(productDto.getBackorderAllowed() != null
                                                ? productDto.getBackorderAllowed()
                                                : false)

                                // SHIPPING
                                .weightGrams(productDto.getWeightGrams())

                                .requiresShipping(productDto.getRequiresShipping() != null
                                                ? productDto.getRequiresShipping()
                                                : true)

                                // VISIBILITY
                                .publishedAt(productDto.getPublishedAt())

                                .featured(productDto.getFeatured() != null
                                                ? productDto.getFeatured()
                                                : false)

                                // AUDIT
                                .createdBy(productDto.getCreatedBy())
                                .updatedBy(productDto.getUpdatedBy())

                                // RELATIONS
                                .images(new ArrayList<>())

                                .build();
        }
}