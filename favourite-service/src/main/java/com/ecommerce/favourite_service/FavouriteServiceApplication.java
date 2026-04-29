package com.ecommerce.favourite_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = "com.ecommerce")

public class FavouriteServiceApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT +0:00"));
		SpringApplication.run(FavouriteServiceApplication.class, args);
	}

}
