package com.ecommerce.product_service.exception.wrapper;

import java.io.Serial;

public class InvalidMediaException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidMediaException(String message) {
        super(message);
    }
}