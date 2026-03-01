package com.ecommerce.user_service.service.impl;

import java.util.stream.Collectors;

import com.ecommerce.user_service.security.userprinciple.UserPrinciple;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.user_service.exception.wrapper.EmailOrUsernameNotFoundException;
import com.ecommerce.user_service.exception.wrapper.PasswordNotFoundException;
import com.ecommerce.user_service.exception.wrapper.PhoneNumberNotFoundException;
import com.ecommerce.user_service.exception.wrapper.UserNotFoundException;
import com.ecommerce.user_service.model.dto.request.ChangePasswordRequest;
import com.ecommerce.user_service.model.dto.request.Login;
import com.ecommerce.user_service.model.dto.request.SignUp;
import com.ecommerce.user_service.model.dto.request.UserDto;
import com.ecommerce.user_service.model.dto.response.InformationMessage;
import com.ecommerce.user_service.model.dto.response.JwtResponseMessage;
import com.ecommerce.user_service.model.entity.RoleName;
import com.ecommerce.user_service.model.entity.User;
import com.ecommerce.user_service.repository.UserRepository;
import com.ecommerce.user_service.security.jwt.JwtProvider;
import com.ecommerce.user_service.security.userprinciple.UserDetailService;
import com.ecommerce.user_service.service.RoleService;
import com.ecommerce.user_service.service.UserService;
import org.springframework.data.domain.Page;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final ModelMapper modelMapper;
    private final UserDetailService userDetailService;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider, ModelMapper modelMapper, UserDetailService userDetailService,
            RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.modelMapper = modelMapper;
        this.userDetailService = userDetailService;
        this.roleService = roleService;
    }

    @Override
    public Mono<User> register(SignUp signUp) {
        return Mono.fromCallable(() -> {
            if (userRepository.existsByUsername(signUp.getUsername())) {
                throw new EmailOrUsernameNotFoundException(
                        "The username " + signUp.getUsername() + " is existed, please try again.");
            }
            if (userRepository.existsByEmail(signUp.getEmail())) {
                throw new EmailOrUsernameNotFoundException(
                        "The email " + signUp.getEmail() + " is existed, please try again.");
            }
            if (userRepository.existsByPhone(signUp.getPhone())) {
                throw new PhoneNumberNotFoundException(
                        "The phone number " + signUp.getPhone() + " is existed, please try again.");
            }
            User user = modelMapper.map(signUp, User.class);
            user.setPassword(passwordEncoder.encode(signUp.getPassword()));
            user.setRoles(signUp.getRoles()
                    .stream()
                    .map(role -> roleService.findByName(mapToRoleName(role))
                            .orElseThrow(() -> new RuntimeException("Role not found: " + role)))
                    .collect(Collectors.toSet()));

            System.out.println("User: ");
            System.out.println(user);

            return userRepository.save(user);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<JwtResponseMessage> login(Login signInForm) {
        return Mono.fromCallable(() -> {
            String usernameOrEmail = signInForm.getUsername();
            boolean isEmail = usernameOrEmail.contains("@");

            UserDetails userDetails;
            if (isEmail) {
                userDetails = userDetailService.loadUserByEmail(usernameOrEmail);
            } else {
                userDetails = userDetailService.loadUserByUsername(usernameOrEmail);
            }

            // check username
            if (userDetails == null) {
                throw new UserNotFoundException("User not found");
            }

            // check password
            if (!passwordEncoder.matches(signInForm.getPassword(), userDetails.getPassword())) {
                throw new PasswordNotFoundException("Password not found");
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    signInForm.getPassword(), userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtProvider.createToken(authentication);
            String refreshToken = jwtProvider.createRefreshToken(authentication);

            UserPrinciple userPrinciple = (UserPrinciple) userDetails;

            return JwtResponseMessage.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .information(InformationMessage.builder()
                            .id(userPrinciple.id())
                            .fullname(userPrinciple.fullname())
                            .username(userPrinciple.username())
                            .email(userPrinciple.email())
                            .phone(userPrinciple.phone())
                            .gender(userPrinciple.gender())
                            .avatar(userPrinciple.avatar())
                            .roles(userPrinciple.roles())
                            .build())
                    .build();

        }).subscribeOn(Schedulers.boundedElastic()).onErrorResume(Mono::error);
    }

    @Override
    public Mono<Void> logout() {
        return Mono.fromRunnable(() -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            SecurityContextHolder.getContext().setAuthentication(null);

            String currentToken = getCurrentToken();

            if (authentication != null && authentication.isAuthenticated()) {
                // Invalidate the current token by reducing its expiration time.
                String updatedToken = jwtProvider.reduceTokenExpiration(currentToken);
            }
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    @Override
    public Mono<User> update(Long id, SignUp update) {
        return null;
    }

    @Override
    public Mono<String> changePassword(ChangePasswordRequest request) {
        return null;
    }

    @Override
    public Mono<String> delete(Long id) {
        return null;
    }

    @Override
    public Mono<User> findById(Long userId) {
        return null;
    }

    @Override
    public Mono<User> findByUsername(String userName) {
        return null;
    }

    @Override
    public Mono<Page<UserDto>> findAllUsers(int page, int size, String sortBy, String sortOrder) {
        return null;
    }

    private RoleName mapToRoleName(String roleName) {
        return switch (roleName) {
            case "ADMIN", "admin", "Admin" -> RoleName.ADMIN;
            case "PM", "pm", "Pm" -> RoleName.PM;
            case "USER", "user", "User" -> RoleName.USER;
            default -> null;
        };
    }

    private String getCurrentToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object credentials = authentication.getCredentials();

            if (credentials instanceof String) {
                return (String) credentials;
            }
        }

        return null;
    }
}
