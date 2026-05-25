package com.ecommerce.product_service.api;

import com.ecommerce.product_service.dto.ProductDto;
import com.ecommerce.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // ═══════════════════════════════════════════════════════════════════════════
    // PRODUCT CRUD
    // ═══════════════════════════════════════════════════════════════════════════

    /** Create a new product with optional images */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> createProduct(
            @Valid @RequestPart("productDTO") ProductDto productDto,
            @RequestPart(value = "images", required = false) MultipartFile[] files) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productDto, files));
    }

    /** Get a single product by ID */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String productId) {
        return ResponseEntity.ok(productService.findById(productId));
    }

    /** Get a product by its unique SKU */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductDto> getProductBySku(@PathVariable String sku) {
        log.info("GET /api/products/sku/{} :: Fetching product", sku);
        return ResponseEntity.ok(productService.findBySku(sku));
    }

    /** Get a product by its URL-friendly slug */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProductDto> getProductBySlug(@PathVariable String slug) {
        log.info("GET /api/products/slug/{} :: Fetching product", slug);
        return ResponseEntity.ok(productService.findBySlug(slug));
    }

    /** Full update of a product */
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String productId,
            @RequestBody ProductDto productDto) {
        log.info("PUT /api/products/{} :: Updating product", productId);
        return ResponseEntity.ok(productService.updateProduct(productId, productDto));
    }

    /** Partial update of a product */
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductDto> patchProduct(@PathVariable String productId,
            @RequestBody Map<String, Object> fields) {
        log.info("PATCH /api/products/{} :: Patching fields {}", productId, fields.keySet());
        return ResponseEntity.ok(productService.patchProduct(productId, fields));
    }

    /** Delete a product */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
        log.info("DELETE /api/products/{} :: Deleting product", productId);
        productService.deleteById(productId);
        return ResponseEntity.noContent().build();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PRODUCT LISTING & SEARCH
    // ═══════════════════════════════════════════════════════════════════════════

    /** List all products with pagination & sorting */
    @GetMapping
    public List<ProductDto> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        log.info("GET /api/products :: page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction);
        return productService.findAll(page, size, sortBy, direction);
    }

    /** Search products by keyword (name, description, brand) */
    @GetMapping("/search")
    public List<ProductDto> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/products/search :: keyword={}, page={}, size={}", keyword, page, size);
        return productService.searchProducts(keyword, page, size);
    }

    /** Filter products by multiple criteria */
    @GetMapping("/filter")
    public List<ProductDto> filterProducts(
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String stockStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/products/filter :: categoryId={}, brand={}, status={}", categoryId, brand, status);
        return productService.filterProducts(categoryId, brand, productType, status, minPrice, maxPrice, stockStatus,
                page, size);
    }

    /** Get all products under a category */
    @GetMapping("/category/{categoryId}")
    public List<ProductDto> getProductsByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/products/category/{} :: page={}, size={}", categoryId, page, size);
        return productService.getProductsByCategory(categoryId, page, size);
    }

    /** Get all products by brand */
    @GetMapping("/brand/{brand}")
    public List<ProductDto> getProductsByBrand(
            @PathVariable String brand,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/products/brand/{} :: page={}, size={}", brand, page, size);
        return productService.getProductsByBrand(brand, page, size);
    }

    /** Get all featured products */
    @GetMapping("/featured")
    public List<ProductDto> getFeaturedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/products/featured :: page={}, size={}", page, size);
        return productService.getFeaturedProducts(page, size);
    }

    /** Get all products currently on sale */
    @GetMapping("/on-sale")
    public List<ProductDto> getOnSaleProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/products/on-sale :: page={}, size={}", page, size);
        return productService.getOnSaleProducts(page, size);
    }

    /** Get recently added products */
    @GetMapping("/new-arrivals")
    public List<ProductDto> getNewArrivals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/products/new-arrivals :: page={}, size={}", page, size);
        return productService.getNewArrivals(page, size);
    }

    /** Get top-rated products */
    @GetMapping("/top-rated")
    public List<ProductDto> getTopRatedProducts(
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/products/top-rated :: size={}", size);
        return productService.getTopRatedProducts(size);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PRODUCT STATUS & LIFECYCLE
    // ═══════════════════════════════════════════════════════════════════════════

    /** Update product status (DRAFT → ACTIVE → ARCHIVED etc.) */
    @PatchMapping("/{productId}/status")
    public ResponseEntity<ProductDto> updateProductStatus(@PathVariable String productId,
            @RequestParam String status) {
        log.info("PATCH /api/products/{}/status :: status={}", productId, status);
        return ResponseEntity.ok(productService.updateStatus(productId, status));
    }

    /** Publish a product (set status ACTIVE + publishedAt timestamp) */
    @PostMapping("/{productId}/publish")
    public ResponseEntity<ProductDto> publishProduct(@PathVariable String productId) {
        log.info("POST /api/products/{}/publish", productId);
        return ResponseEntity.ok(productService.publishProduct(productId));
    }

    /** Unpublish / take a product offline */
    @PostMapping("/{productId}/unpublish")
    public ResponseEntity<ProductDto> unpublishProduct(@PathVariable String productId) {
        log.info("POST /api/products/{}/unpublish", productId);
        return ResponseEntity.ok(productService.unpublishProduct(productId));
    }

    /** Toggle the featured flag on a product */
    @PatchMapping("/{productId}/featured")
    public ResponseEntity<ProductDto> toggleFeatured(@PathVariable String productId,
            @RequestParam boolean featured) {
        log.info("PATCH /api/products/{}/featured :: featured={}", productId, featured);
        return ResponseEntity.ok(productService.toggleFeatured(productId, featured));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // INVENTORY MANAGEMENT
    // ═══════════════════════════════════════════════════════════════════════════

    /** Get current stock info for a product */
    @GetMapping("/{productId}/inventory")
    public ResponseEntity<Map<String, Object>> getInventory(@PathVariable String productId) {
        log.info("GET /api/products/{}/inventory", productId);
        return ResponseEntity.ok(productService.getInventory(productId));
    }

    /** Update stock quantity */
    @PatchMapping("/{productId}/inventory")
    public ResponseEntity<ProductDto> updateStock(@PathVariable String productId,
            @RequestBody Map<String, Object> stockUpdate) {
        log.info("PATCH /api/products/{}/inventory :: update={}", productId, stockUpdate);
        return ResponseEntity.ok(productService.updateStock(productId, stockUpdate));
    }

    /** Reserve stock (e.g. during checkout) */
    @PostMapping("/{productId}/inventory/reserve")
    public ResponseEntity<ProductDto> reserveStock(@PathVariable String productId,
            @RequestParam int quantity) {
        log.info("POST /api/products/{}/inventory/reserve :: quantity={}", productId, quantity);
        return ResponseEntity.ok(productService.reserveStock(productId, quantity));
    }

    /** Release previously reserved stock */
    @PostMapping("/{productId}/inventory/release")
    public ResponseEntity<ProductDto> releaseStock(@PathVariable String productId,
            @RequestParam int quantity) {
        log.info("POST /api/products/{}/inventory/release :: quantity={}", productId, quantity);
        return ResponseEntity.ok(productService.releaseStock(productId, quantity));
    }

    /** Get all low-stock products */
    @GetMapping("/inventory/low-stock")
    public List<ProductDto> getLowStockProducts(
            @RequestParam(defaultValue = "10") int threshold,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/products/inventory/low-stock :: threshold={}, page={}, size={}", threshold, page, size);
        return productService.getLowStockProducts(threshold, page, size);
    }

    /** Get all out-of-stock products */
    @GetMapping("/inventory/out-of-stock")
    public List<ProductDto> getOutOfStockProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/products/inventory/out-of-stock :: page={}, size={}", page, size);
        return productService.getOutOfStockProducts(page, size);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PRICING
    // ═══════════════════════════════════════════════════════════════════════════

    /** Update product pricing (price, salePrice, costPrice, compareAtPrice) */
    @PatchMapping("/{productId}/pricing")
    public ResponseEntity<ProductDto> updatePricing(@PathVariable String productId,
            @RequestBody Map<String, Object> pricingUpdate) {
        log.info("PATCH /api/products/{}/pricing :: update={}", productId, pricingUpdate);
        return ResponseEntity.ok(productService.updatePricing(productId, pricingUpdate));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // BULK OPERATIONS
    // ═══════════════════════════════════════════════════════════════════════════

    /** Bulk delete multiple products */
    @DeleteMapping("/bulk")
    public ResponseEntity<Void> bulkDeleteProducts(@RequestBody List<String> productIds) {
        log.info("DELETE /api/products/bulk :: ids={}", productIds);
        productService.bulkDelete(productIds);
        return ResponseEntity.noContent().build();
    }

    /** Bulk update status for multiple products */
    @PatchMapping("/bulk/status")
    public ResponseEntity<Void> bulkUpdateStatus(@RequestBody Map<String, Object> request) {
        @SuppressWarnings("unchecked")
        List<String> productIds = (List<String>) request.get("productIds");
        String status = (String) request.get("status");
        log.info("PATCH /api/products/bulk/status :: status={}, ids={}", status, productIds);
        productService.bulkUpdateStatus(productIds, status);
        return ResponseEntity.noContent().build();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // ANALYTICS / COUNTS
    // ═══════════════════════════════════════════════════════════════════════════

    /** Get total product count (optionally filtered by status) */
    @GetMapping("/count")
    public ResponseEntity<Long> getProductCount(@RequestParam(required = false) String status) {
        log.info("GET /api/products/count :: status={}", status);
        return ResponseEntity.ok(productService.getProductCount(status));
    }

    /** Get count of products grouped by product type */
    @GetMapping("/count/by-type")
    public ResponseEntity<Map<String, Long>> getProductCountByType() {
        log.info("GET /api/products/count/by-type");
        return ResponseEntity.ok(productService.getProductCountByType());
    }

    /** Get count of products grouped by stock status */
    @GetMapping("/count/by-stock-status")
    public ResponseEntity<Map<String, Long>> getProductCountByStockStatus() {
        log.info("GET /api/products/count/by-stock-status");
        return ResponseEntity.ok(productService.getProductCountByStockStatus());
    }
}
