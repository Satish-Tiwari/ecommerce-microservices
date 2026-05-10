package com.ecommerce.product_service.exception.wrapper;

import java.io.Serial;

public class MediaNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public MediaNotFoundException(String message) {
        super(message);
    }
}