package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.entity.ProductDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDocumentRepository extends JpaRepository<ProductDocument, String> {

    /** All documents for a product */
    List<ProductDocument> findByProductId(String productId);

    /** Delete all documents for a product */
    void deleteByProductId(String productId);
}
