package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.entity.Product.ProductStatus;
import com.ecommerce.product_service.entity.Product.ProductType;
import com.ecommerce.product_service.entity.Product.StockStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    // ─── Identity Lookups ────────────────────────────────────────────────────
    Optional<Product> findBySku(String sku);

    Optional<Product> findBySlug(String slug);

    Optional<Product> findByExternalId(String externalId);

    boolean existsBySku(String sku);

    boolean existsBySlug(String slug);

    // ─── Search ──────────────────────────────────────────────────────────────
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // ─── Filtering ───────────────────────────────────────────────────────────
    Page<Product> findByCategoryId(String categoryId, Pageable pageable);

    Page<Product> findByBrandIgnoreCase(String brand, Pageable pageable);

    Page<Product> findByProductType(ProductType productType, Pageable pageable);

    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    Page<Product> findByStockStatus(StockStatus stockStatus, Pageable pageable);

    Page<Product> findByFeaturedTrue(Pageable pageable);

    // ─── Price range ─────────────────────────────────────────────────────────
    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    // ─── On-Sale: salePrice > 0 and status ACTIVE ────────────────────────────
    @Query("SELECT p FROM Product p WHERE p.salePrice IS NOT NULL AND p.salePrice > 0 AND p.status = 'ACTIVE'")
    Page<Product> findOnSaleProducts(Pageable pageable);

    // ─── Inventory queries ───────────────────────────────────────────────────
    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= :threshold AND p.stockQuantity > 0")
    Page<Product> findLowStockProducts(@Param("threshold") int threshold, Pageable pageable);

    Page<Product> findByStockQuantityLessThanEqual(int quantity, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.stockQuantity = 0 OR p.stockStatus = 'OUT_OF_STOCK'")
    Page<Product> findOutOfStockProducts(Pageable pageable);

    // ─── Counts ──────────────────────────────────────────────────────────────
    long countByStatus(ProductStatus status);

    long countByProductType(ProductType productType);

    long countByStockStatus(StockStatus stockStatus);

    long countByCategoryId(String categoryId);
}
