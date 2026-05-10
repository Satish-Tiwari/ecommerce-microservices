package com.ecommerce.product_service.exception;

import com.ecommerce.product_service.exception.payload.ExceptionMessage;
import com.ecommerce.product_service.exception.wrapper.CategoryNotFoundException;
import com.ecommerce.product_service.exception.wrapper.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ProductServiceExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionMessage> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.warn("Validation failed for request: {} - Errors: {}", request.getRequestURI(), errors);

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Failed", "ERR_VALIDATION", request.getRequestURI(), errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionMessage> handleMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Malformed JSON request: {}", request.getRequestURI());
        return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request", "ERR_BAD_REQUEST", request.getRequestURI(), null);
    }

    @ExceptionHandler({CategoryNotFoundException.class, ProductNotFoundException.class})
    public ResponseEntity<ExceptionMessage> handleNotFoundException(RuntimeException ex, HttpServletRequest request) {
        log.warn("Resource not found: {} - {}", request.getRequestURI(), ex.getMessage());
        String errorCode = ex instanceof CategoryNotFoundException ? "ERR_CATEGORY_NOT_FOUND" : "ERR_PRODUCT_NOT_FOUND";
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), errorCode, request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionMessage> handleTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = String.format("Parameter '%s' should be of type '%s'", ex.getName(), ex.getRequiredType().getSimpleName());
        log.warn("Type mismatch: {}", message);
        return buildResponse(HttpStatus.BAD_REQUEST, message, "ERR_TYPE_MISMATCH", request.getRequestURI(), null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionMessage> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.warn("Method not supported: {} - {}", ex.getMethod(), request.getRequestURI());
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), "ERR_METHOD_NOT_ALLOWED", request.getRequestURI(), null);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ExceptionMessage> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        log.warn("Media type not supported: {} - {}", ex.getContentType(), request.getRequestURI());
        return buildResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage(), "ERR_UNSUPPORTED_MEDIA_TYPE", request.getRequestURI(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessage> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error occurred at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", "ERR_INTERNAL_SERVER_ERROR", request.getRequestURI(), null);
    }

    private ResponseEntity<ExceptionMessage> buildResponse(HttpStatus status, String message, String errorCode, String path, Map<String, String> errors) {
        ExceptionMessage payload = ExceptionMessage.builder()
                .timestamp(ZonedDateTime.now(ZoneId.systemDefault()))
                .status(status.value())
                .error(status.getReasonPhrase())
                .errorCode(errorCode)
                .message(message)
                .path(path)
                .errors(errors)
                .build();
        return new ResponseEntity<>(payload, status);
    }
}