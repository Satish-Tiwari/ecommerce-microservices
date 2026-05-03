package com.ecommerce.user_service.exception;

import com.ecommerce.user_service.exception.payload.ExceptionMessage;
import com.ecommerce.user_service.exception.wrapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApiExceptionHandler {

        @ExceptionHandler(value = {
                        MethodArgumentNotValidException.class,
                        HttpMessageNotReadableException.class
        })
        public ResponseEntity<ExceptionMessage> handleValidationException(final Exception e) {
                log.info("ApiExceptionHandler controller, handle validation exception\n");
                final var badRequest = HttpStatus.BAD_REQUEST;

                String message = "Validation failed";
                if (e instanceof BindException be) {
                        message = Objects.requireNonNull(be.getBindingResult().getFieldError()).getDefaultMessage();
                }

                return new ResponseEntity<>(
                                ExceptionMessage.builder()
                                                .msg("*" + message + "!**")
                                                .httpStatus(badRequest)
                                                .timestamp(ZonedDateTime.now(ZoneId.systemDefault()))
                                                .build(),
                                badRequest);
        }

        @ExceptionHandler(value = {
                        UserNotFoundException.class,
                        RoleNotFoundException.class,
                        EmailOrUsernameNotFoundException.class,
                        PhoneNumberNotFoundException.class
        })
        public ResponseEntity<ExceptionMessage> handleNotFoundException(final RuntimeException e) {
                log.info("ApiExceptionHandler controller, handle Not Found exception: {}\n", e.getMessage());
                final var notFound = HttpStatus.NOT_FOUND;

                return new ResponseEntity<>(
                                ExceptionMessage.builder()
                                                .msg(e.getMessage())
                                                .httpStatus(notFound)
                                                .timestamp(ZonedDateTime.now(ZoneId.systemDefault()))
                                                .build(),
                                notFound);
        }

        @ExceptionHandler(UserAlreadyExistsException.class)
        public ResponseEntity<ExceptionMessage> handleAlreadyExistsException(UserAlreadyExistsException e) {
                log.info("ApiExceptionHandler controller, handle Already Exists exception: {}\n", e.getMessage());
                final var conflict = HttpStatus.CONFLICT;

                return new ResponseEntity<>(
                                ExceptionMessage.builder()
                                                .msg(e.getMessage())
                                                .httpStatus(conflict)
                                                .timestamp(ZonedDateTime.now(ZoneId.systemDefault()))
                                                .build(),
                                conflict);
        }

        @ExceptionHandler(PasswordNotFoundException.class)
        public ResponseEntity<ExceptionMessage> handlePasswordException(PasswordNotFoundException e) {
                log.info("ApiExceptionHandler controller, handle Password exception\n");
                final var badRequest = HttpStatus.BAD_REQUEST;

                return new ResponseEntity<>(
                                ExceptionMessage.builder()
                                                .msg(e.getMessage())
                                                .httpStatus(badRequest)
                                                .timestamp(ZonedDateTime.now(ZoneId.systemDefault()))
                                                .build(),
                                badRequest);
        }

        @ExceptionHandler({ AccessDeniedException.class, BadCredentialsException.class })
        public ResponseEntity<ExceptionMessage> handleAccessDeniedException(Exception ex) {
                log.error("Access denied: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(ExceptionMessage.builder()
                                                .msg("Access denied: " + ex.getMessage())
                                                .httpStatus(HttpStatus.FORBIDDEN)
                                                .timestamp(ZonedDateTime.now(ZoneId.systemDefault()))
                                                .build());
        }

        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ExceptionMessage> handleAuthenticationException(AuthenticationException ex) {
                log.error("Authentication failed: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(ExceptionMessage.builder()
                                                .msg("Authentication failed: " + ex.getMessage())
                                                .httpStatus(HttpStatus.UNAUTHORIZED)
                                                .timestamp(ZonedDateTime.now(ZoneId.systemDefault()))
                                                .build());
        }

        @ExceptionHandler(UserNotAuthenticatedException.class)
        public ResponseEntity<ExceptionMessage> handleUserNotAuthenticatedException(UserNotAuthenticatedException ex) {
                log.error("User not authenticated: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(ExceptionMessage.builder()
                                                .msg("User not authenticated: " + ex.getMessage())
                                                .httpStatus(HttpStatus.UNAUTHORIZED)
                                                .timestamp(ZonedDateTime.now(ZoneId.systemDefault()))
                                                .build());
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ExceptionMessage> handleGenericException(Exception ex) {
                log.error("Unexpected error: {}", ex.getMessage(), ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ExceptionMessage.builder()
                                                .msg("An unexpected error occurred: " + ex.getMessage())
                                                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                                                .timestamp(ZonedDateTime.now(ZoneId.systemDefault()))
                                                .build());
        }

}
