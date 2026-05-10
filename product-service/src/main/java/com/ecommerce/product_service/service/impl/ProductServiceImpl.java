package com.ecommerce.product_service.service.impl;

import com.ecommerce.product_service.dto.ProductDto;
import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.entity.Product.ProductStatus;
import com.ecommerce.product_service.entity.Product.ProductType;
import com.ecommerce.product_service.entity.Product.StockStatus;
import com.ecommerce.product_service.exception.wrapper.ProductNotFoundException;
import com.ecommerce.product_service.helper.ProductMappingHelper;
import com.ecommerce.product_service.repository.CategoryRepository;
import com.ecommerce.product_service.repository.ProductRepository;
import com.ecommerce.product_service.service.MediaService;
import com.ecommerce.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.ecommerce.product_service.entity.ProductImage;
import com.ecommerce.product_service.entity.Media;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MediaService mediaService;

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> findAll(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAll(pageable).getContent().stream()
                .map(ProductMappingHelper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto findById(String productId) {
        return productRepository.findById(productId)
                .map(ProductMappingHelper::map)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto findBySku(String sku) {
        return productRepository.findBySku(sku)
                .map(ProductMappingHelper::map)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with SKU: " + sku));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto findBySlug(String slug) {
        return productRepository.findBySlug(slug)
                .map(ProductMappingHelper::map)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with slug: " + slug));
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto, MultipartFile[] files) {
        log.info("ProductService :: Creating product [{}] with [{}] images", productDto.getName(), files != null ? files.length : 0);
        Product product = ProductMappingHelper.map(productDto);
        
        if (productDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        
        product.setId(null); // Ensure new product
        product.setImages(new ArrayList<>());

        // Handle Image Uploads
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                Media media = mediaService.saveFile(file);
                
                ProductImage productImage = ProductImage.builder()
                        .media(media)
                        .sortOrder(i)
                        .product(product)
                        .build();
                
                product.getImages().add(productImage);
            }
        }
        
        return ProductMappingHelper.map(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDto updateProduct(String productId, ProductDto productDto) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        BeanUtils.copyProperties(productDto, existingProduct, "id", "createdAt", "version", "images", "metaData", "variants", "reviews");
        
        if (productDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingProduct.setCategory(category);
        }

        return ProductMappingHelper.map(productRepository.save(existingProduct));
    }

    @Override
    @Transactional
    public ProductDto patchProduct(String productId, Map<String, Object> fields) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        fields.forEach((key, value) -> {
            switch (key) {
                case "name" -> existingProduct.setName((String) value);
                case "description" -> existingProduct.setDescription((String) value);
                case "price" -> existingProduct.setPrice(new BigDecimal(value.toString()));
                case "salePrice" -> existingProduct.setSalePrice(new BigDecimal(value.toString()));
                case "status" -> existingProduct.setStatus(ProductStatus.valueOf((String) value));
                case "featured" -> existingProduct.setFeatured((Boolean) value);
                case "stockQuantity" -> existingProduct.setStockQuantity((Integer) value);
            }
        });

        return ProductMappingHelper.map(productRepository.save(existingProduct));
    }

    @Override
    @Transactional
    public void deleteById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        // 1. Delete associated media via MediaService
        product.getImages().forEach(img -> {
            if (img.getMedia() != null) {
                mediaService.deleteFile(img.getMedia().getFilePath());
            }
        });

        // 2. Delete product
        productRepository.delete(product);
        log.info("ProductService :: Product [{}] and its media deleted", productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> searchProducts(String keyword, int page, int size) {
        return productRepository.searchByKeyword(keyword, PageRequest.of(page, size)).getContent().stream()
                .map(ProductMappingHelper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> filterProducts(String categoryId, String brand, String productType, String status, 
                                          BigDecimal minPrice, BigDecimal maxPrice, String stockStatus, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Product> products;
        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId, pageable).getContent();
        } else if (brand != null) {
            products = productRepository.findByBrandIgnoreCase(brand, pageable).getContent();
        } else if (status != null) {
            products = productRepository.findByStatus(ProductStatus.valueOf(status), pageable).getContent();
        } else {
            products = productRepository.findAll(pageable).getContent();
        }
        
        return products.stream()
                .map(ProductMappingHelper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByCategory(String categoryId, int page, int size) {
        return productRepository.findByCategoryId(categoryId, PageRequest.of(page, size)).getContent().stream()
                .map(ProductMappingHelper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByBrand(String brand, int page, int size) {
        return productRepository.findByBrandIgnoreCase(brand, PageRequest.of(page, size)).getContent().stream()
                .map(ProductMappingHelper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getFeaturedProducts(int page, int size) {
        return productRepository.findByFeaturedTrue(PageRequest.of(page, size)).getContent().stream()
                .map(ProductMappingHelper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getOnSaleProducts(int page, int size) {
        return productRepository.findOnSaleProducts(PageRequest.of(page, size)).getContent().stream()
                .map(ProductMappingHelper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getNewArrivals(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending())).getContent().stream()
                .map(ProductMappingHelper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getTopRatedProducts(int size) {
        return productRepository.findAll(PageRequest.of(0, size, Sort.by("averageRating").descending())).getContent().stream()
                .map(ProductMappingHelper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDto updateStatus(String productId, String status) {
        ProductDto dto = findById(productId);
        dto.setStatus(status);
        return updateProduct(productId, dto);
    }

    @Override
    @Transactional
    public ProductDto publishProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        product.setStatus(ProductStatus.ACTIVE);
        product.setPublishedAt(Instant.now());
        return ProductMappingHelper.map(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDto unpublishProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        product.setStatus(ProductStatus.DRAFT);
        return ProductMappingHelper.map(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDto toggleFeatured(String productId, boolean featured) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        product.setFeatured(featured);
        return ProductMappingHelper.map(productRepository.save(product));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getInventory(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        Map<String, Object> inventory = new HashMap<>();
        inventory.put("productId", productId);
        inventory.put("stockQuantity", product.getStockQuantity());
        inventory.put("reservedQuantity", product.getReservedQuantity());
        inventory.put("availableStock", product.getAvailableStock());
        inventory.put("stockStatus", product.getStockStatus());
        return inventory;
    }

    @Override
    @Transactional
    public ProductDto updateStock(String productId, Map<String, Object> stockUpdate) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (stockUpdate.containsKey("quantity")) {
            product.setStockQuantity((Integer) stockUpdate.get("quantity"));
        }
        if (stockUpdate.containsKey("status")) {
            product.setStockStatus(StockStatus.valueOf((String) stockUpdate.get("status")));
        }
        return ProductMappingHelper.map(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDto reserveStock(String productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (product.getAvailableStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        product.setReservedQuantity(product.getReservedQuantity() + quantity);
        return ProductMappingHelper.map(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDto releaseStock(String productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        product.setReservedQuantity(Math.max(0, product.getReservedQuantity() - quantity));
        return ProductMappingHelper.map(productRepository.save(product));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getLowStockProducts(int threshold, int page, int size) {
        return productRepository.findLowStockProducts(threshold, PageRequest.of(page, size)).getContent().stream()
                .map(ProductMappingHelper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getOutOfStockProducts(int page, int size) {
        return productRepository.findOutOfStockProducts(PageRequest.of(page, size)).getContent().stream()
                .map(ProductMappingHelper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDto updatePricing(String productId, Map<String, Object> pricingUpdate) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (pricingUpdate.containsKey("price")) product.setPrice(new BigDecimal(pricingUpdate.get("price").toString()));
        if (pricingUpdate.containsKey("salePrice")) product.setSalePrice(new BigDecimal(pricingUpdate.get("salePrice").toString()));
        return ProductMappingHelper.map(productRepository.save(product));
    }

    @Override
    @Transactional
    public void bulkDelete(List<String> productIds) {
        productIds.forEach(this::deleteById);
    }

    @Override
    @Transactional
    public void bulkUpdateStatus(List<String> productIds, String status) {
        productIds.forEach(id -> updateStatus(id, status));
    }

    @Override
    @Transactional(readOnly = true)
    public long getProductCount(String status) {
        return status == null ? productRepository.count() : productRepository.countByStatus(ProductStatus.valueOf(status));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getProductCountByType() {
        Map<String, Long> counts = new HashMap<>();
        for (ProductType type : ProductType.values()) {
            counts.put(type.name(), productRepository.countByProductType(type));
        }
        return counts;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getProductCountByStockStatus() {
        Map<String, Long> counts = new HashMap<>();
        for (StockStatus status : StockStatus.values()) {
            counts.put(status.name(), productRepository.countByStockStatus(status));
        }
        return counts;
    }
}
