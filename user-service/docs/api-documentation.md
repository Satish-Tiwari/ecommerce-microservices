# User Service API Documentation

The User Service handles user authentication, registration, and profile management for the Ecommerce Microservices platform.

## Base URL
`http://localhost:8081`

## Authentication
Most endpoints are public, but some require a valid JWT token in the `Authorization` header.
- **Header:** `Authorization: Bearer <access_token>`

## Swagger UI
Interactive API documentation is available at:
`http://localhost:8081/swagger-ui/index.html`

---

## Endpoints

### 1. User Registration
Registers a new user in the system.

- **URL:** `/api/auth/signup` or `/api/auth/register`
- **Method:** `POST`
- **Access:** Public
- **Request Body:**
  ```json
  {
    "fullname": "John Doe",
    "username": "johndoe123",
    "password": "Password123",
    "email": "john.doe@example.com",
    "gender": "MALE",
    "phone": "0123456789",
    "avatar": "https://example.com/avatar.jpg",
    "roles": ["USER"]
  }
  ```
- **Constraints:**
  - `fullname`: 6-50 characters.
  - `username`: 6-50 characters, unique.
  - `password`: Min 8 characters, must contain uppercase, lowercase, and numbers.
  - `email`: Valid email format, unique.
  - `phone`: 10-11 digits, start with +84 or 0, unique.
  - `roles`: Set of roles (e.g., "USER", "ADMIN", "PM").

- **Success Response:**
  - **Code:** `200 OK`
  - **Content:**
    ```json
    {
      "message": "User registered successfully"
    }
    ```

- **Error Response:**
  - **Code:** `500 Internal Server Error`
  - **Content:**
    ```json
    {
      "message": "User registration failed"
    }
    ```

### 2. User Login
Authenticates a user and returns JWT tokens.

- **URL:** `/api/auth/signin` or `/api/auth/login`
- **Method:** `POST`
- **Access:** Public
- **Request Body:**
  ```json
  {
    "username": "johndoe123",
    "password": "Password123"
  }
  ```
  *Note: `username` field can also accept the user's email.*

- **Success Response:**
  - **Code:** `200 OK`
  - **Content:**
    ```json
    {
      "access_token": "eyJhbGciOiJIUzI1NiJ9...",
      "refresh_token": "eyJhbGciOiJIUzI1NiJ9...",
      "user_info": {
        "id": 1,
        "fullname": "John Doe",
        "username": "johndoe123",
        "email": "john.doe@example.com",
        "gender": "MALE",
        "phone": "0123456789",
        "avatar": "https://example.com/avatar.jpg",
        "roles": [
          { "authority": "USER" }
        ]
      }
    }
    ```

- **Error Response:**
  - **Code:** `500 Internal Server Error` (or `401 Unauthorized` depending on security config)
  - **Content:**
    ```json
    {
      "access_token": null,
      "refresh_token": null,
      "user_info": {}
    }
    ```

### 3. User Logout
Invalidates the current session.

- **URL:** `/api/auth/logout`
- **Method:** `POST`
- **Access:** Authenticated (Role: USER)
- **Header:** `Authorization: Bearer <access_token>`

- **Success Response:**
  - **Code:** `200 OK`
  - **Content:** `"Logged out successfully."`

- **Error Response:**
  - **Code:** `400 Bad Request`
  - **Content:** `"Logout failed."`

---

## Data Models

### SignUp Request
| Field | Type | Description | Constraints |
| :--- | :--- | :--- | :--- |
| `fullname` | String | User's full name | 6-50 chars |
| `username` | String | Unique username | 6-50 chars |
| `password` | String | User's password | 8-50 chars, mixed case + digits |
| `email` | String | Unique email | Valid email format |
| `gender` | String | User's gender | Not blank |
| `phone` | String | Unique phone number | 10-11 digits (+84 or 0) |
| `avatar` | String | URL to user's avatar | Valid HTTP/HTTPS URL |
| `roles` | Set<String> | Assigned roles | ADMIN, PM, USER |

### Login Request
| Field | Type | Description |
| :--- | :--- | :--- |
| `username` | String | Username or Email |
| `password` | String | User's password |

### JwtResponseMessage
| Field | Type | Description |
| :--- | :--- | :--- |
| `access_token` | String | JWT Access Token |
| `refresh_token` | String | JWT Refresh Token |
| `user_info` | Object | User details (InformationMessage) |
