package com.ecommerce.product_service.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    // ═══════════════════════════════════════════════════════════════════════════
    // CATEGORY CRUD
    // ═══════════════════════════════════════════════════════════════════════════

    /** Create a new category */
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Map<String, Object> request) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    /** Get a single category by ID */
    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable String categoryId) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    /** Full update of a category */
    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable String categoryId,
                                            @RequestBody Map<String, Object> request) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    /** Partial update of a category */
    @PatchMapping("/{categoryId}")
    public ResponseEntity<?> patchCategory(@PathVariable String categoryId,
                                           @RequestBody Map<String, Object> fields) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    /** Delete a category */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable String categoryId) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CATEGORY LISTING & SEARCH
    // ═══════════════════════════════════════════════════════════════════════════

    /** List all categories with pagination & sorting */
    @GetMapping
    public ResponseEntity<?> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "categoryTitle") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    /** Search categories by name */
    @GetMapping("/search")
    public ResponseEntity<?> searchCategories(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CATEGORY HIERARCHY (Parent / Sub-Categories)
    // ═══════════════════════════════════════════════════════════════════════════

    /** Get full category tree (nested hierarchy) */
    @GetMapping("/tree")
    public ResponseEntity<?> getCategoryTree() {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    /** Get all root categories (those with no parent) */
    @GetMapping("/roots")
    public ResponseEntity<?> getRootCategories() {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    /** Get all sub-categories of a given parent category */
    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<?> getSubCategories(@PathVariable String categoryId) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    /** Get the parent category of a given category */
    @GetMapping("/{categoryId}/parent")
    public ResponseEntity<?> getParentCategory(@PathVariable String categoryId) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    /** Move a category under a different parent */
    @PatchMapping("/{categoryId}/parent")
    public ResponseEntity<?> moveCategory(@PathVariable String categoryId,
                                          @RequestParam(required = false) String newParentId) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CATEGORY IMAGE
    // ═══════════════════════════════════════════════════════════════════════════

    /** Upload / replace category image */
    @PostMapping("/{categoryId}/image")
    public ResponseEntity<?> uploadCategoryImage(@PathVariable String categoryId,
                                                 @RequestParam("file") MultipartFile file) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    /** Get category image */
    @GetMapping("/{categoryId}/image")
    public ResponseEntity<?> getCategoryImage(@PathVariable String categoryId) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    /** Delete category image */
    @DeleteMapping("/{categoryId}/image")
    public ResponseEntity<?> deleteCategoryImage(@PathVariable String categoryId) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CATEGORY ↔ PRODUCTS
    // ═══════════════════════════════════════════════════════════════════════════

    /** Get all products in a category */
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<?> getProductsByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    /** Get count of products in a category */
    @GetMapping("/{categoryId}/products/count")
    public ResponseEntity<?> getProductCountByCategory(@PathVariable String categoryId) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // BULK OPERATIONS
    // ═══════════════════════════════════════════════════════════════════════════

    /** Bulk delete multiple categories */
    @DeleteMapping("/bulk")
    public ResponseEntity<?> bulkDeleteCategories(@RequestBody List<String> categoryIds) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // ANALYTICS / COUNTS
    // ═══════════════════════════════════════════════════════════════════════════

    /** Get total category count */
    @GetMapping("/count")
    public ResponseEntity<?> getCategoryCount() {
        // TODO: implement
        return ResponseEntity.ok().build();
    }
}
