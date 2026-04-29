package com.ecommerce.favourite_service.helper;

import com.ecommerce.favourite_service.dto.FavouriteDto;
import com.ecommerce.favourite_service.dto.ProductDto;
import com.ecommerce.favourite_service.dto.UserDto;
import com.ecommerce.favourite_service.entity.Favourite;

public class FavouriteMappingHelper {

    public static FavouriteDto map(final Favourite favourite) {
        return FavouriteDto.builder()
                .userId(favourite.getUserId())
                .productId(favourite.getProductId())
                .likeDate(favourite.getLikeDate())
                .userDto(
                        UserDto.builder()
                                .userId(favourite.getUserId())
                                .build())
                .productDto(
                        ProductDto.builder()
                                .productId(favourite.getProductId())
                                .build())
                .build();
    }

    public static Favourite map(final FavouriteDto favouriteDto) {
        return Favourite.builder()
                .userId(favouriteDto.getUserId())
                .productId(favouriteDto.getProductId())
                .likeDate(favouriteDto.getLikeDate())
                .build();
    }

}


