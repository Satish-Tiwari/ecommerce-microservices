package com.ecommerce.user_service.service;

import com.ecommerce.user_service.model.dto.request.ChangePasswordRequest;
import com.ecommerce.user_service.model.dto.request.Login;
import com.ecommerce.user_service.model.dto.request.SignUp;
import com.ecommerce.user_service.model.dto.request.UserDto;
import com.ecommerce.user_service.model.dto.response.JwtResponseMessage;
import com.ecommerce.user_service.model.entity.User;
import org.springframework.data.domain.Page;

import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> register(SignUp signUp);

    Mono<JwtResponseMessage> login(Login signInForm);

    Mono<String> logout();

    Mono<User> update(Long id, SignUp update);

    Mono<String> changePassword(ChangePasswordRequest request);

    Mono<String> delete(Long id);

    Mono<User> findById(Long userId);

    Mono<User> findByUsername(String userName);

    Mono<com.ecommerce.user_service.model.dto.response.InformationMessage> getProfile();

    Mono<Page<UserDto>> findAllUsers(int page, int size, String sortBy, String sortOrder);
}
