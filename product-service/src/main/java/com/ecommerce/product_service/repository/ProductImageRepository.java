package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, String> {

    /** All images for a product (respects @OrderColumn sort_order) */
    List<ProductImage> findByProductIdOrderBySortOrderAsc(String productId);

    /** All images for a product (fallback without sort order) */
    List<ProductImage> findByProductId(String productId);

    /** Delete all images for a product */
    void deleteByProductId(String productId);
}
