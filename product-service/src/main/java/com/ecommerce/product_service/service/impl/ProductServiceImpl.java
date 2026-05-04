package com.ecommerce.product_service.service.impl;

import com.ecommerce.product_service.dto.ProductDto;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.exception.wrapper.ProductNotFoundException;
import com.ecommerce.product_service.helper.CategoryMappingHelper;
import com.ecommerce.product_service.helper.ProductMappingHelper;
import com.ecommerce.product_service.repository.ProductRepository;
import com.ecommerce.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Flux<List<ProductDto>> findAll() {
        log.info("ProductDto List, service, fetch all products");
        return Flux.defer(() -> {
            List<ProductDto> productDtos = productRepository.findAll()
                    .stream()
                    .map(ProductMappingHelper::map)
                    .distinct()
                    .toList();
            return Flux.just(productDtos);
        })
                .onErrorResume(throwable -> {
                    log.error("Error while fetching products: " + throwable.getMessage());
                    return Flux.empty();
                });
    }

    @Override
    public ProductDto findById(Integer productId) {
        log.info("ProductDto, service; fetch product by id");
        return productRepository.findById(productId)
                .map(ProductMappingHelper::map)
                .orElseThrow(
                        () -> new ProductNotFoundException(String.format("Product with id[%d] not found", productId)));
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        log.info("ProductDto, service; save product");
        try {
            return ProductMappingHelper.map(productRepository.save(ProductMappingHelper.map(productDto)));
        } catch (DataIntegrityViolationException e) {
            log.error("Error saving product: Data integrity violation", e);
            throw new ProductNotFoundException("Error saving product: Data integrity violation", e);
        } catch (Exception e) {
            log.error("Error saving product", e);
            throw new ProductNotFoundException("Error saving product", e);
        }
    }

    @Override
    public ProductDto update(ProductDto productDto) {
        log.info("ProductDto, service; update product");

        Product existingProduct = productRepository.findById(productDto.getProductId())
                .orElseThrow(
                        () -> new ProductNotFoundException("Product not found with id: " + productDto.getProductId()));

        BeanUtils.copyProperties(productDto, existingProduct, "productId", "categoryDto");

        if (productDto.getCategoryDto() != null) {
            existingProduct.setCategory(CategoryMappingHelper.map(productDto.getCategoryDto()));
        }

        Product updatedProduct = productRepository.save(existingProduct);

        return ProductMappingHelper.map(updatedProduct);
    }

    @Override
    public ProductDto update(Integer productId, ProductDto productDto) {
        log.info("ProductDto, service; update product with productId");

        // Check Product Exists in DB
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        // Update Product using BeanUtils.copyProperties
        BeanUtils.copyProperties(productDto, existingProduct, "productId", "category");

        // // Or Using Model Mapper Create a ModelMapper instance
        // ModelMapper modelMapper = new ModelMapper();
        //
        // // Map properties from productDto to existingProduct
        // modelMapper.map(productDto, existingProduct);

        // If categoryDto is not null, map it to Category
        if (productDto.getCategoryDto() != null) {
            existingProduct.setCategory(CategoryMappingHelper.map(productDto.getCategoryDto()));
        }

        // Save to Database
        Product updatedProduct = productRepository.save(existingProduct);

        return ProductMappingHelper.map(updatedProduct);
    }

    @Override
    public void deleteById(Integer productId) {
        log.info("Void, service; delete product by id");
        this.productRepository.delete(ProductMappingHelper.map(this.findById(productId)));
    }

}
