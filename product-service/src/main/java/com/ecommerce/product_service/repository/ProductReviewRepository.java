package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.entity.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, String> {

    /** Paginated reviews for a product */
    Page<ProductReview> findByProductId(String productId, Pageable pageable);

    /** All reviews for a product (non-paginated) */
    List<ProductReview> findByProductId(String productId);

    /** Reviews by a specific user */
    List<ProductReview> findByUserId(String userId);

    /** Average rating for a product */
    @Query("SELECT AVG(r.rating) FROM ProductReview r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(String productId);

    /** Review count for a product */
    long countByProductId(String productId);

    /** Delete all reviews for a product */
    void deleteByProductId(String productId);
}
