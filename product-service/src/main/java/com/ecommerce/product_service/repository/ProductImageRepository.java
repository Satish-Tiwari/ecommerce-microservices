package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, String> {
    /** All images for a product */
    List<ProductImage> findByProductId(String productId);

    /** All images for a product ordered by sort order */
    List<ProductImage> findByProductIdOrderBySortOrderAsc(String productId);

    /** Delete all images for a product */
    void deleteByProductId(String productId);
}
