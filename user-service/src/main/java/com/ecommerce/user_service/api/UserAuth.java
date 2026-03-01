package com.ecommerce.user_service.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.user_service.model.dto.request.Login;
import com.ecommerce.user_service.model.dto.request.SignUp;
import com.ecommerce.user_service.model.dto.response.InformationMessage;
import com.ecommerce.user_service.model.dto.response.JwtResponseMessage;
import com.ecommerce.user_service.model.dto.response.ResponseMessage;
import com.ecommerce.user_service.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "User Authentication API", description = "APIs for user registration, login, and authentication")
public class UserAuth {
        private final UserService userService;

        @Operation(summary = "Register new user", description = "Registers a new user with the provided details.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User created successfully"),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error")
        })
        @PostMapping({ "/signup", "/register" })
        public Mono<ResponseMessage> register(@Valid @RequestBody SignUp signUp) {
                return userService.register(signUp)
                                .map(user -> new ResponseMessage("User registered successfully"))
                                .onErrorResume(error -> Mono.just(new ResponseMessage("User registration failed")));
        }

        @Operation(summary = "User login", description = "Logs in a user with the provided credentials.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Login successful"),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error")
        })
        @PostMapping({ "/signin", "/login" })
        public Mono<ResponseEntity<JwtResponseMessage>> login(@Valid @RequestBody Login signInForm) {
                return userService.login(signInForm)
                                .map(ResponseEntity::ok)
                                .onErrorResume(error -> {
                                        JwtResponseMessage errorjwtResponseMessage = new JwtResponseMessage(
                                                        null,
                                                        null,
                                                        new InformationMessage());
                                        return Mono.just(new ResponseEntity<>(errorjwtResponseMessage,
                                                        HttpStatus.INTERNAL_SERVER_ERROR));
                                });
        }

        @Operation(summary = "User logout", description = "Logs out the authenticated user.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Logged out successfully"),
                        @ApiResponse(responseCode = "400", description = "Bad Request")
        })
        @PostMapping("/logout")
        @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
        public Mono<ResponseEntity<String>> logout() {
                log.info("Logout endpoint called");
                return userService.logout()
                                .then(Mono.just(new ResponseEntity<>("Logged out successfully.", HttpStatus.OK)))
                                .onErrorResume(error -> {
                                        log.error("Logout failed", error);
                                        return Mono.just(
                                                        new ResponseEntity<>("Logout failed.", HttpStatus.BAD_REQUEST));
                                });
        }
}
