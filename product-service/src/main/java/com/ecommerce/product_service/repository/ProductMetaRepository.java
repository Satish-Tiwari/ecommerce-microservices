package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.entity.ProductMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductMetaRepository extends JpaRepository<ProductMeta, String> {

    /** All meta entries for a product */
    List<ProductMeta> findByProductId(String productId);

    /** Lookup a specific key for a product */
    Optional<ProductMeta> findByProductIdAndMetaKey(String productId, String metaKey);

    /** Check if a key exists for a product */
    boolean existsByProductIdAndMetaKey(String productId, String metaKey);

    /** Delete a specific key for a product */
    void deleteByProductIdAndMetaKey(String productId, String metaKey);

    /** Delete all meta for a product */
    void deleteByProductId(String productId);
}
