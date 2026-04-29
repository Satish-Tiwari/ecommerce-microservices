package com.ecommerce.common_lib.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(final String message) {
        super(message);
    }
}
