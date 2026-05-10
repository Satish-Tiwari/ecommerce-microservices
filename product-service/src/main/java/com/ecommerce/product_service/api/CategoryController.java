package com.ecommerce.product_service.api;

import com.ecommerce.product_service.dto.CategoryDto;
import com.ecommerce.product_service.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    private final CategoryService categoryService;

    /** Create a new category with optional images */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestPart("dto") CategoryDto categoryDto,
            @RequestPart(value = "files", required = false) MultipartFile[] files) {

        System.out.println("category dto: " + categoryDto);
        System.out.println("files: " + files);

        CategoryDto created = categoryService.createCategory(categoryDto, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /** Get a single category by ID */
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId) {
        log.info("GET /api/categories/{} :: Fetching category", categoryId);
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    /** Partial update of a category */
    @PatchMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> patchCategory(@PathVariable String categoryId,
            @RequestBody Map<String, Object> fields) {
        log.info("PATCH /api/categories/{} :: Patching fields {}", categoryId, fields.keySet());
        return ResponseEntity.ok(categoryService.patchCategory(categoryId, fields));
    }

    /** Delete a category */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String categoryId) {
        log.info("DELETE /api/categories/{} :: Deleting category", categoryId);
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    /** List all categories with pagination & sorting */
    @GetMapping
    public List<CategoryDto> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "categoryTitle") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("GET /api/categories :: page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction);
        return categoryService.getAllCategories(page, size, sortBy, direction);
    }

    /** Search categories by name */
    @GetMapping("/search")
    public List<CategoryDto> searchCategories(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/categories/search :: keyword={}", keyword);
        return categoryService.searchCategories(keyword, page, size);
    }

    /** Get full category tree (nested hierarchy) */
    @GetMapping("/tree")
    public List<CategoryDto> getCategoryTree() {
        log.info("GET /api/categories/tree :: Fetching full tree");
        return categoryService.getCategoryTree();
    }

    /** Get all root categories (those with no parent) */
    @GetMapping("/roots")
    public List<CategoryDto> getRootCategories() {
        log.info("GET /api/categories/roots :: Fetching roots");
        return categoryService.getRootCategories();
    }

    /** Get all sub-categories of a given parent category */
    @GetMapping("/{categoryId}/subcategories")
    public List<CategoryDto> getSubCategories(@PathVariable String categoryId) {
        log.info("GET /api/categories/{}/subcategories", categoryId);
        return categoryService.getSubCategories(categoryId);
    }

    /** Get the parent category of a given category */
    @GetMapping("/{categoryId}/parent")
    public ResponseEntity<CategoryDto> getParentCategory(@PathVariable String categoryId) {
        log.info("GET /api/categories/{}/parent", categoryId);
        CategoryDto parent = categoryService.getParentCategory(categoryId);
        return parent != null ? ResponseEntity.ok(parent) : ResponseEntity.notFound().build();
    }

    /** Move a category under a different parent */
    @PatchMapping("/{categoryId}/parent")
    public ResponseEntity<CategoryDto> moveCategory(@PathVariable String categoryId,
            @RequestParam(required = false) String newParentId) {
        log.info("PATCH /api/categories/{}/parent?newParentId={}", categoryId, newParentId);
        return ResponseEntity.ok(categoryService.moveCategory(categoryId, newParentId));
    }

    /** Upload / replace category image */
    @PostMapping(value = "/{categoryId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryDto> uploadCategoryImage(@PathVariable String categoryId,
            @RequestPart("file") MultipartFile file) {
        log.info("POST /api/categories/{}/image", categoryId);
        return ResponseEntity.ok(categoryService.uploadImage(categoryId, file));
    }

    /** Delete category image */
    @DeleteMapping("/{categoryId}/image")
    public ResponseEntity<CategoryDto> deleteCategoryImage(@PathVariable String categoryId) {
        log.info("DELETE /api/categories/{}/image", categoryId);
        return ResponseEntity.ok(categoryService.deleteImage(categoryId));
    }

    /** Get count of products in a category */
    @GetMapping("/{categoryId}/products/count")
    public ResponseEntity<Long> getProductCountByCategory(@PathVariable String categoryId) {
        log.info("GET /api/categories/{}/products/count", categoryId);
        return ResponseEntity.ok(categoryService.getProductCountByCategory(categoryId));
    }

    /** Get total category count */
    @GetMapping("/count")
    public ResponseEntity<Long> getCategoryCount() {
        log.info("GET /api/categories/count");
        return ResponseEntity.ok(categoryService.getCategoryCount());
    }

    /** Bulk delete multiple categories */
    @DeleteMapping("/bulk")
    public ResponseEntity<Void> bulkDeleteCategories(@RequestBody List<String> categoryIds) {
        log.info("DELETE /api/categories/bulk :: {}", categoryIds);
        categoryService.bulkDeleteCategories(categoryIds);
        return ResponseEntity.noContent().build();
    }
}
