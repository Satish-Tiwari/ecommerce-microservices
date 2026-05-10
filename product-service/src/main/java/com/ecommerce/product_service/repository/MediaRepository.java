package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, String> {
    
    @Query("SELECT m FROM Media m WHERE m NOT IN (SELECT c.media FROM Category c WHERE c.media IS NOT NULL) " +
           "AND m NOT IN (SELECT pi.media FROM ProductImage pi WHERE pi.media IS NOT NULL)")
    List<Media> findOrphanedMedia();
}
