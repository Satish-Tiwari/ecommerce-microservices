package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, String> {

    /** All variants for a product */
    List<ProductVariant> findByProductId(String productId);

    /** Lookup variant by its unique SKU */
    Optional<ProductVariant> findBySku(String sku);

    boolean existsBySku(String sku);

    /** Delete all variants for a product */
    void deleteByProductId(String productId);
}
