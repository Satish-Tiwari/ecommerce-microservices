package com.ecommerce.product_service.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.product_service.service.BatchService;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/batch")
public class BatchController {

    private final BatchService batchService;

    @PostMapping(value = "/single", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> processSingleThreadBatch(@RequestPart(value = "file") MultipartFile file) {
        log.info("POST /api/batch/single :: Processing file {}", file.getName());
        return ResponseEntity.ok(batchService.processSingleThreadBatch(file));
    }

    @PostMapping(value = "/multi", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> processMultiThreadBatch(@RequestPart(value = "file") MultipartFile file,
            @RequestPart int threads) {
        log.info("POST /api/batch/multi :: Processing file {}", file.getName());
        return ResponseEntity.ok(batchService.processMultiThreadBatch(file, threads));
    }

}
