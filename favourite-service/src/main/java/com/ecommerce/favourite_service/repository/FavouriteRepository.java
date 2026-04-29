package com.ecommerce.favourite_service.repository;

import com.ecommerce.favourite_service.entity.Favourite;
import com.ecommerce.favourite_service.entity.id.FavouriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FavouriteRepository extends JpaRepository<Favourite, FavouriteId> {

}
