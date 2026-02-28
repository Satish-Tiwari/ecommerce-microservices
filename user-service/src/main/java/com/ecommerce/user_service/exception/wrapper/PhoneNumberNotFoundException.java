package com.ecommerce.user_service.exception.wrapper;

public class PhoneNumberNotFoundException extends RuntimeException{
    public PhoneNumberNotFoundException() {
        super();
    }

    public PhoneNumberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhoneNumberNotFoundException(String message) {
        super(message);
    }

    public PhoneNumberNotFoundException(Throwable cause) {
        super(cause);
    }
}
