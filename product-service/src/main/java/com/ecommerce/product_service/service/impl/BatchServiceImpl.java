package com.ecommerce.product_service.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.product_service.service.BatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BatchServiceImpl implements BatchService {

    @Override
    @Transactional
    public String processSingleThreadBatch(MultipartFile file) {
        return "Single thread batch processing completed";
    }

    @Override
    @Transactional
    public String processMultiThreadBatch(MultipartFile file, int threads) {
        return "Multi thread batch processing completed";
    }

}
