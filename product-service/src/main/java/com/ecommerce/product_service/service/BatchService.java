package com.ecommerce.product_service.service;

import org.springframework.web.multipart.MultipartFile;

public interface BatchService {
    String processSingleThreadBatch(MultipartFile excelFile);

    String processMultiThreadBatch(MultipartFile excelFile, int threads);
}
