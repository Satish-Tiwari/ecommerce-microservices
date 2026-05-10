package com.ecommerce.product_service.exception.wrapper;

import java.io.Serial;

public class MediaUploadException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public MediaUploadException(String message) {
        super(message);
    }

    public MediaUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}