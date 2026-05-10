package com.ecommerce.product_service.service.impl;

import com.ecommerce.product_service.dto.CategoryDto;
import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.entity.Media;
import com.ecommerce.product_service.exception.wrapper.CategoryNotFoundException;
import com.ecommerce.product_service.helper.CategoryMappingHelper;
import com.ecommerce.product_service.repository.CategoryRepository;
import com.ecommerce.product_service.service.CategoryService;
import com.ecommerce.product_service.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MediaService mediaService;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto, MultipartFile[] files) {
        log.info("CategoryService :: Creating category [{}] with [{}] images", categoryDto.getCategoryTitle(), files != null ? files.length : 0);
        Category category = CategoryMappingHelper.map(categoryDto);
        
        if (categoryDto.getParentCategoryId() != null && !categoryDto.getParentCategoryId().isBlank()) {
            Category parent = categoryRepository.findById(categoryDto.getParentCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Parent not found: " + categoryDto.getParentCategoryId()));
            category.setParentCategory(parent);
        }
        category.setId(null);

        // Handle Image (Take the first one if multiple are provided, as per industry standard for Category cover image)
        if (files != null && files.length > 0) {
            Media media = mediaService.saveFile(files[0]);
            category.setMedia(media);
        }

        return CategoryMappingHelper.map(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(String categoryId) {
        return categoryRepository.findById(categoryId)
                .map(CategoryMappingHelper::map)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + categoryId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return categoryRepository.findAll(pageable).getContent().stream()
                .map(CategoryMappingHelper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDto patchCategory(String categoryId, Map<String, Object> fields) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + categoryId));

        fields.forEach((key, value) -> {
            switch (key) {
                case "categoryTitle" -> category.setCategoryTitle((String) value);
                case "parentCategoryId" -> {
                    if (value == null) {
                        category.setParentCategory(null);
                    } else {
                        category.setParentCategory(categoryRepository.findById((String) value).orElse(null));
                    }
                }
            }
        });
        return CategoryMappingHelper.map(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + categoryId));

        String mediaId = category.getMedia() != null ? category.getMedia().getId() : null;
        
        categoryRepository.delete(category);
        log.info("CategoryService :: Category [{}] deleted from DB", categoryId);
        
        if (mediaId != null) {
            mediaService.deleteMedia(mediaId);
        }
    }

    @Override
    @Transactional
    public CategoryDto uploadImage(String categoryId, MultipartFile file) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + categoryId));

        // Delete old media if exists
        if (category.getMedia() != null) {
            mediaService.deleteMedia(category.getMedia().getId());
        }

        Media media = mediaService.saveFile(file);
        category.setMedia(media);
        return CategoryMappingHelper.map(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryDto deleteImage(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + categoryId));

        if (category.getMedia() == null) return CategoryMappingHelper.map(category);
        
        String mediaId = category.getMedia().getId();
        category.setMedia(null);
        categoryRepository.save(category);
        
        mediaService.deleteMedia(mediaId);
        return CategoryMappingHelper.map(category);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCategoryCount() {
        return categoryRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> searchCategories(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryRepository.findByCategoryTitleContainingIgnoreCase(keyword, pageable).getContent().stream()
                .map(CategoryMappingHelper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoryTree() {
        return categoryRepository.findByParentCategoryIsNull().stream()
                .map(CategoryMappingHelper::mapWithChildren)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getRootCategories() {
        return categoryRepository.findByParentCategoryIsNull().stream()
                .map(CategoryMappingHelper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getSubCategories(String parentId) {
        return categoryRepository.findByParentCategoryId(parentId).stream()
                .map(CategoryMappingHelper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getParentCategory(String categoryId) {
        return categoryRepository.findById(categoryId)
                .map(Category::getParentCategory)
                .map(CategoryMappingHelper::map)
                .orElse(null);
    }

    @Override
    @Transactional
    public CategoryDto moveCategory(String categoryId, String newParentId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + categoryId));
        
        if (newParentId == null || newParentId.isBlank()) {
            category.setParentCategory(null);
        } else {
            Category newParent = categoryRepository.findById(newParentId)
                    .orElseThrow(() -> new CategoryNotFoundException("New parent not found: " + newParentId));
            category.setParentCategory(newParent);
        }
        return CategoryMappingHelper.map(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public long getProductCountByCategory(String categoryId) {
        return categoryRepository.countProductsByCategoryId(categoryId);
    }

    @Override
    @Transactional
    public void bulkDeleteCategories(List<String> categoryIds) {
        categoryIds.forEach(this::deleteCategory);
    }
}
