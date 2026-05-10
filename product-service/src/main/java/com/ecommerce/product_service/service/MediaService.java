package com.ecommerce.product_service.service;

import com.ecommerce.product_service.entity.Media;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {

    /**
     * Saves a file to the filesystem and database.
     * WordPress-style folder strategy (/uploads/yyyy/MM/dd/).
     *
     * @param file The file to save
     * @return The Media entity
     */
    Media saveFile(MultipartFile file);

    /**
     * Deletes media from the filesystem and database.
     *
     * @param mediaId The ID of the media to delete
     */
    void deleteMedia(String mediaId);

    /**
     * Physically deletes a file from the filesystem.
     *
     * @param relativePath The relative path to the file
     */
    void deleteFile(String relativePath);

    /**
     * Generates a full URL for the given relative path.
     *
     * @param relativePath The relative path
     * @return The full URL string
     */
    String getFullUrl(String relativePath);

    /**
     * Strong garbage collector to find and delete orphaned media records and files.
     */
    void garbageCollect();
}
