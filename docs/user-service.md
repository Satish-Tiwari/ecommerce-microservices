# User Service Documentation

## Overview
The User Service handles **authentication, authorization, and user profile management**. It issues JWT tokens (access + refresh), manages role-based access control (RBAC), and provides user CRUD operations.

**Port:** `8081` &nbsp;|&nbsp; **Database:** PostgreSQL &nbsp;|&nbsp; **Eureka Name:** `USER-SERVICE`

---

## Features

| Feature | Status | Description |
|---|---|---|
| User Registration | ✅ | Sign up with validation (email, phone, password rules) |
| User Login | ✅ | Login via username or email, returns JWT access + refresh tokens |
| User Logout | ✅ | Invalidates JWT by reducing expiration |
| JWT Authentication | ✅ | HS512-signed tokens with configurable expiration |
| OAuth2 Resource Server | ✅ | Validates incoming JWTs via Spring OAuth2 |
| Role Management | ✅ | Assign/revoke roles (USER, PM, ADMIN) |
| Token Validation | ✅ | Validate and extract authorities from JWT |
| Swagger UI | ✅ | OpenAPI docs at `/swagger-ui/index.html` |
| Update User | ⬜ | Stub — not yet implemented |
| Change Password | ⬜ | Stub — not yet implemented |
| Delete User | ⬜ | Stub — not yet implemented |
| Find Users (Paged) | ⬜ | Stub — not yet implemented |

---

## High-Level Design

```mermaid
graph TB
    C["Client"] -->|via Gateway :8080| US["User Service :8081"]

    US --> PG[("PostgreSQL<br/>user-service DB")]
    US -.-> EU["Eureka :8761"]

    subgraph User Service
        AUTH["Auth Controller<br/>/api/auth"]
        SEC["Security Layer<br/>JWT + OAuth2"]
        SVC["User Service Impl"]
        REPO["User Repository"]
    end

    AUTH --> SEC --> SVC --> REPO --> PG
```

---

## Low-Level Design

### Entity Relationship

```mermaid
erDiagram
    USERS {
        Long userId PK
        String fullName
        String userName
        String email UK
        String password
        String gender
        String phone
        String imageUrl
    }

    ROLES {
        Long id PK
        String roleName UK "USER | PM | ADMIN"
    }

    USER_ROLE {
        Long user_id FK
        Long role_id FK
    }

    USERS ||--o{ USER_ROLE : has
    ROLES ||--o{ USER_ROLE : assigned_to
```

### Class Diagram

```mermaid
classDiagram
    class UserAuth {
        +register(SignUp) Mono~ResponseMessage~
        +login(Login) Mono~ResponseEntity~
        +logout() Mono~ResponseEntity~
    }

    class UserService {
        <<interface>>
        +register(SignUp) Mono~User~
        +login(Login) Mono~JwtResponseMessage~
        +logout() Mono~Void~
        +update(Long, SignUp) Mono~User~
        +changePassword(ChangePasswordRequest) Mono~String~
        +delete(Long) Mono~String~
        +findById(Long) Mono~User~
        +findByUsername(String) Mono~User~
        +findAllUsers(int, int, String, String) Mono~Page~
    }

    class UserServiceImpl {
        -UserRepository userRepository
        -PasswordEncoder passwordEncoder
        -JwtProvider jwtProvider
        -ModelMapper modelMapper
        -UserDetailService userDetailService
        -RoleService roleService
    }

    class JwtProvider {
        +createToken(Authentication) String
        +createRefreshToken(Authentication) String
        +validateToken(String) Boolean
        +getUserNameFromToken(String) String
        +reduceTokenExpiration(String) String
    }

    class WebSecurityConfig {
        +filterChain(HttpSecurity) SecurityFilterChain
        +jwtDecoder() JwtDecoder
        +jwtAuthenticationConverter() JwtAuthenticationConverter
    }

    UserAuth --> UserService
    UserService <|.. UserServiceImpl
    UserServiceImpl --> JwtProvider
    UserServiceImpl --> UserDetailService
    WebSecurityConfig --> JwtProvider
```

### Security Configuration
- **Public endpoints:** `/api/auth/**`, `/api/manager/user/**`, `/v3/api-docs/**`, `/swagger-ui/**`, `/actuator/**`
- **Authenticated endpoints:** `/api/manager/change-password`, `/api/manager/delete/**`, `/api/auth/logout`
- **Session policy:** `STATELESS` (no server-side sessions)
- **JWT Algorithm:** `HS512` with configurable secret key
- **Token expiration:** Access = 24h (86400s), Refresh = 48h (172800s)

---

## Flows

### Flow: User Registration

```mermaid
sequenceDiagram
    participant C as Client
    participant UC as UserAuth Controller
    participant US as UserServiceImpl
    participant UR as UserRepository
    participant RS as RoleService
    participant DB as PostgreSQL

    C->>UC: POST /api/auth/signup
    UC->>US: register(SignUp)

    US->>UR: existsByUsername()
    UR-->>US: false
    US->>UR: existsByEmail()
    UR-->>US: false
    US->>UR: existsByPhone()
    UR-->>US: false

    US->>RS: findByName(RoleName)
    RS-->>US: Role entity

    US->>US: BCrypt encode password
    US->>UR: save(User)
    UR->>DB: INSERT INTO users
    DB-->>UR: User saved
    UR-->>US: User entity
    US-->>UC: Mono<User>
    UC-->>C: 200 "User registered successfully"
```

### Flow: User Login

```mermaid
sequenceDiagram
    participant C as Client
    participant UC as UserAuth Controller
    participant US as UserServiceImpl
    participant UDS as UserDetailService
    participant JWT as JwtProvider
    participant DB as PostgreSQL

    C->>UC: POST /api/auth/login {username, password}
    UC->>US: login(Login)

    alt username contains @
        US->>UDS: loadUserByEmail(email)
    else
        US->>UDS: loadUserByUsername(username)
    end

    UDS->>DB: findByUsername/Email
    DB-->>UDS: User entity
    UDS-->>US: UserPrinciple (UserDetails)

    US->>US: BCrypt.matches(password)
    US->>JWT: createToken(authentication)
    JWT-->>US: accessToken (HS512)
    US->>JWT: createRefreshToken(authentication)
    JWT-->>US: refreshToken (HS512)

    US-->>UC: JwtResponseMessage
    UC-->>C: 200 {access_token, refresh_token, user_info}
```
