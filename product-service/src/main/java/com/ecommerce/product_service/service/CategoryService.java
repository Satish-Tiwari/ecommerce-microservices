package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.CategoryDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto, MultipartFile[] files);

    CategoryDto getCategoryById(String categoryId);

    CategoryDto patchCategory(String categoryId, Map<String, Object> fields);

    List<CategoryDto> getAllCategories(int page, int size, String sortBy, String direction);

    void deleteCategory(String categoryId);

    long getCategoryCount();

    // ─── Search & Hierarchy ───────────────────────────────────

    List<CategoryDto> searchCategories(String keyword, int page, int size);

    List<CategoryDto> getCategoryTree();

    List<CategoryDto> getRootCategories();

    List<CategoryDto> getSubCategories(String parentId);

    CategoryDto getParentCategory(String categoryId);

    CategoryDto moveCategory(String categoryId, String newParentId);

    // ─── Image Operations ─────────────────────────────────────
    
    CategoryDto uploadImage(String categoryId, MultipartFile file);

    CategoryDto deleteImage(String categoryId);

    // ─── Analytics ────────────────────────────────────────────

    long getProductCountByCategory(String categoryId);

    void bulkDeleteCategories(List<String> categoryIds);
}
