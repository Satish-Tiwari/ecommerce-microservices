package com.ecommerce.common_lib.exception;

import com.ecommerce.common_lib.utils.MessagesUtils;

public class BadRequestException extends RuntimeException {

    private final String message;

    public BadRequestException(String message) {
        this.message = MessagesUtils.getMessage(message);
    }

    public BadRequestException(String errorCode, Object... var2) {
        this.message = MessagesUtils.getMessage(errorCode, var2);
    }

    @Override
    public String getMessage() {
        return message;
    }

}
