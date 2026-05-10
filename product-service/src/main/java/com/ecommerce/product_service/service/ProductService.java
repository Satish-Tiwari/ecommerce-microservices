package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.ProductDto;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<ProductDto> findAll(int page, int size, String sortBy, String direction);
    ProductDto findById(String productId);
    ProductDto findBySku(String sku);
    ProductDto findBySlug(String slug);
    ProductDto createProduct(ProductDto productDto, org.springframework.web.multipart.MultipartFile[] files);
    ProductDto updateProduct(String productId, ProductDto productDto);
    ProductDto patchProduct(String productId, Map<String, Object> fields);
    void deleteById(String productId);

    // Search & Filter
    List<ProductDto> searchProducts(String keyword, int page, int size);
    List<ProductDto> filterProducts(String categoryId, String brand, String productType, String status, 
                                   java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice, 
                                   String stockStatus, int page, int size);
    List<ProductDto> getProductsByCategory(String categoryId, int page, int size);
    List<ProductDto> getProductsByBrand(String brand, int page, int size);
    List<ProductDto> getFeaturedProducts(int page, int size);
    List<ProductDto> getOnSaleProducts(int page, int size);
    List<ProductDto> getNewArrivals(int page, int size);
    List<ProductDto> getTopRatedProducts(int size);

    // Status & Lifecycle
    ProductDto updateStatus(String productId, String status);
    ProductDto publishProduct(String productId);
    ProductDto unpublishProduct(String productId);
    ProductDto toggleFeatured(String productId, boolean featured);

    // Inventory
    Map<String, Object> getInventory(String productId);
    ProductDto updateStock(String productId, Map<String, Object> stockUpdate);
    ProductDto reserveStock(String productId, int quantity);
    ProductDto releaseStock(String productId, int quantity);
    List<ProductDto> getLowStockProducts(int threshold, int page, int size);
    List<ProductDto> getOutOfStockProducts(int page, int size);

    // Pricing
    ProductDto updatePricing(String productId, Map<String, Object> pricingUpdate);

    // Bulk Operations
    void bulkDelete(java.util.List<String> productIds);
    void bulkUpdateStatus(java.util.List<String> productIds, String status);

    // Analytics
    long getProductCount(String status);
    Map<String, Long> getProductCountByType();
    Map<String, Long> getProductCountByStockStatus();
}
