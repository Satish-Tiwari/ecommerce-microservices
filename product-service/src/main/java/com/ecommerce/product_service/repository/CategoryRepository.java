package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    // ─── Search ──────────────────────────────────────────────────────────────
    Page<Category> findByCategoryTitleContainingIgnoreCase(String keyword, Pageable pageable);

    /** All root categories (no parent) */
    List<Category> findByParentCategoryIsNull();

    /** Direct children of a given parent */
    List<Category> findByParentCategoryId(String parentId);

    /** Check if a category has children */
    boolean existsByParentCategoryId(String parentId);

    // ─── Lookups ─────────────────────────────────────────────────────────────
    Optional<Category> findByCategoryTitle(String categoryTitle);

    // ─── Counts ──────────────────────────────────────────────────────────────
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId")
    long countProductsByCategoryId(String categoryId);
}
