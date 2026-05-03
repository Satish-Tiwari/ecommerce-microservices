package com.ecommerce.user_service.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
        public Mono<ResponseEntity<ResponseMessage>> register(@Valid @RequestBody SignUp signUp) {
                return userService.register(signUp)
                                .map(user -> ResponseEntity.ok(new ResponseMessage("User registered successfully")));
        }

        @Operation(summary = "User login", description = "Logs in a user with the provided credentials.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Login successful"),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error")
        })
        @PostMapping({ "/signin", "/login" })
        public Mono<ResponseEntity<JwtResponseMessage>> login(@Valid @RequestBody Login signInForm) {
                return userService.login(signInForm)
                                .map(ResponseEntity::ok);
        }

        @Operation(summary = "User logout", description = "Logs out the authenticated user.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Logged out successfully"),
                        @ApiResponse(responseCode = "400", description = "Bad Request")
        })
        @PostMapping("/logout")
        @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
        public Mono<ResponseEntity<ResponseMessage>> logout() {
                return userService.logout()
                                .map(result -> {
                                        if (result.startsWith("ALREADY_LOGGED_OUT:")) {
                                                String username = result.replace("ALREADY_LOGGED_OUT:", "");
                                                return ResponseEntity.ok(new ResponseMessage(
                                                                username + ", you are already logged out."));
                                        }
                                        return ResponseEntity
                                                        .ok(new ResponseMessage(result + " logged out successfully."));
                                });
        }

        @Operation(summary = "Get user profile", description = "Retrieves the profile information of the authenticated user.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
                        @ApiResponse(responseCode = "401", description = "Unauthorized")
        })
        @GetMapping("/profile")
        @PreAuthorize("isAuthenticated()")
        public Mono<ResponseEntity<InformationMessage>> getProfile() {
                return userService.getProfile()
                                .map(ResponseEntity::ok);
        }
}
