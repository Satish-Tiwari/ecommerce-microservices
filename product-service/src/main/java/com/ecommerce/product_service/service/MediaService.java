package com.ecommerce.product_service.service;

import com.ecommerce.product_service.entity.Media;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {

    /**
     * Uploads single or multiple files.
     *
     * Backend automatically generates
     * upload batch and storage structure.
     *
     * @param files multipart files
     * @return uploaded media list
     */
    List<Media> saveFiles(List<MultipartFile> files);

    /**
     * Deletes media metadata
     * and physical file.
     *
     * @param mediaId media identifier
     */
    void deleteMedia(String mediaId);

    /**
     * Deletes physical file only.
     *
     * @param relativePath stored relative path
     */
    void deletePhysicalFile(String relativePath);

    /**
     * Generates full public URL.
     *
     * @param relativePath relative file path
     * @return public URL
     */
    String getFullUrl(String relativePath);

    /**
     * Removes orphaned media/files.
     */
    void garbageCollect();
}