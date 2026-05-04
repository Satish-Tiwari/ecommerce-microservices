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
import com.ecommerce.user_service.exception.wrapper.PasswordNotFoundException;
import com.ecommerce.user_service.exception.wrapper.UserAlreadyExistsException;
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
import com.ecommerce.user_service.service.KafkaProducerService;
import com.ecommerce.user_service.event.UserEvent;
import com.ecommerce.user_service.constant.KafkaConstant;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final ModelMapper modelMapper;
    private final UserDetailService userDetailService;
    private final RoleService roleService;
    private final KafkaProducerService kafkaProducerService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider, ModelMapper modelMapper, UserDetailService userDetailService,
            RoleService roleService, KafkaProducerService kafkaProducerService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.modelMapper = modelMapper;
        this.userDetailService = userDetailService;
        this.roleService = roleService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public Mono<User> register(SignUp signUp) {
        return Mono.fromCallable(() -> {
            if (userRepository.existsByUsername(signUp.getUsername())) {
                throw new UserAlreadyExistsException(
                        "The username " + signUp.getUsername() + " already exists, please try again.");
            }
            if (userRepository.existsByEmail(signUp.getEmail())) {
                throw new UserAlreadyExistsException(
                        "The email " + signUp.getEmail() + " already exists, please try again.");
            }
            if (userRepository.existsByPhone(signUp.getPhone())) {
                throw new UserAlreadyExistsException(
                        "The phone number " + signUp.getPhone() + " already exists, please try again.");
            }
            User user = modelMapper.map(signUp, User.class);
            user.setPassword(passwordEncoder.encode(signUp.getPassword()));
            user.setRoles(signUp.getRoles()
                    .stream()
                    .map(role -> roleService.findByName(mapToRoleName(role))
                            .orElseThrow(() -> new RuntimeException("Role not found: " + role)))
                    .collect(Collectors.toSet()));

            User savedUser = userRepository.save(user);

            // Send Kafka Event
            kafkaProducerService.sendUserEvent(KafkaConstant.profileOnboarding, UserEvent.builder()
                    .email(savedUser.getEmail())
                    .firstName(savedUser.getFullname()) // Assuming fullname is used for first name or split
                    .lastName("")
                    .type("USER_CREATED")
                    .build());

            return savedUser;
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
            User user = userRepository.findByUsername(userPrinciple.username())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            user.setTokenCreatedAt(java.time.LocalDateTime.now());
            userRepository.save(user);

            // Send Kafka Event
            kafkaProducerService.sendUserEvent(KafkaConstant.userLogin, UserEvent.builder()
                    .email(userPrinciple.email())
                    .firstName(userPrinciple.fullname())
                    .lastName("")
                    .type("USER_LOGIN")
                    .build());

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
    public Mono<String> logout() {
        // Capture context and token while still on the request thread
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentToken = getCurrentToken(authentication);
        String username = (authentication != null) ? authentication.getName() : null;

        return Mono.fromCallable(() -> {
            if (username == null) {
                return "User";
            }

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            // Compare stored token with the token used for this request
            if (user.getAccessToken() == null || !user.getAccessToken().equals(currentToken)) {
                return "ALREADY_LOGGED_OUT:" + username;
            }

            user.setAccessToken(null);
            user.setRefreshToken(null);
            user.setTokenCreatedAt(null);
            userRepository.save(user);
            SecurityContextHolder.getContext().setAuthentication(null);

            // Send Kafka Event
            kafkaProducerService.sendUserEvent(KafkaConstant.userLogout, UserEvent.builder()
                    .email(user.getEmail())
                    .firstName(user.getFullname())
                    .lastName("")
                    .type("USER_LOGOUT")
                    .build());

            return username;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<InformationMessage> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : null;

        if (username == null) {
            return Mono.error(new UserNotFoundException("User not authenticated"));
        }

        return Mono.fromCallable(() -> {
            User user = userRepository.findByUsernameWithRoles(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            return InformationMessage.builder()
                    .id(user.getId())
                    .fullname(user.getFullname())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .gender(user.getGender())
                    .avatar(user.getAvatar())
                    .roles(user.getRoles().stream()
                            .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                    role.getName().name()))
                            .collect(java.util.stream.Collectors.toList()))
                    .build();
        }).subscribeOn(Schedulers.boundedElastic());
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

    private String getCurrentToken(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object credentials = authentication.getCredentials();

            if (credentials instanceof String && !((String) credentials).isEmpty()) {
                return (String) credentials;
            }
            // For OAuth2 Resource Server
            if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
                return jwt.getTokenValue();
            }
        }
        return null;
    }
}
