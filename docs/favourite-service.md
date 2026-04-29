# Favourite Service Documentation

## Overview
The Favourite Service manages **user wishlists/favourites** — allowing users to like/unlike products. It uses a **composite primary key** (userId + productId + likeDate) to track each favourite action.

**Port:** TBD | **Database:** MySQL | **Eureka Name:** `FAVOURITE-SERVICE`

---

## Features

| Feature | Status | Description |
|---|---|---|
| List All Favourites | ✅ | Returns all favourite records |
| Get Favourite by ID | ✅ | Composite key lookup (userId + productId + likeDate) |
| Add Favourite | ✅ | Create new favourite entry |
| Update Favourite | ✅ | Modify existing favourite |
| Delete Favourite | ✅ | Remove by composite key (path or body) |
| Distributed Tracing | ✅ | Zipkin + Micrometer Brave |
| Eureka Registration | ✅ | Registers via `@EnableEurekaClient` |

---

## High-Level Design

```mermaid
graph TB
    C["Client"] -->|via Gateway| FS["Favourite Service"]
    FS --> MY[("MySQL<br/>favourite-service")]
    FS -.-> EU["Eureka :8761"]
    FS -.-> CL["common-lib"]
```

## Low-Level Design

### Entity Model

```mermaid
erDiagram
    FAVOURITES {
        Integer user_id PK,FK
        Integer product_id PK,FK
        LocalDateTime like_date PK
    }
```

### Class Diagram

```mermaid
classDiagram
    class FavouriteApi {
        +findAll() ResponseEntity
        +findById(userId, productId, likeDate) ResponseEntity
        +save(FavouriteDto) ResponseEntity
        +update(FavouriteDto) ResponseEntity
        +deleteById(userId, productId, likeDate) ResponseEntity
    }

    class FavouriteId {
        -Integer userId
        -Integer productId
        -LocalDateTime likeDate
    }

    class Favourite {
        -Integer userId
        -Integer productId
        -LocalDateTime likeDate
    }

    Favourite --> FavouriteId : compositeKey
```

### API Endpoints

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/favourites` | List all favourites |
| `GET` | `/api/favourites/{userId}/{productId}/{likeDate}` | Get by composite key |
| `GET` | `/api/favourites/find` | Get by composite key (body) |
| `POST` | `/api/favourites` | Add favourite |
| `PUT` | `/api/favourites` | Update favourite |
| `DELETE` | `/api/favourites/{userId}/{productId}/{likeDate}` | Remove by path |
| `DELETE` | `/api/favourites/delete` | Remove by body |

### Flow: Add to Favourites

```mermaid
sequenceDiagram
    participant C as Client
    participant FA as FavouriteApi
    participant FS as FavouriteServiceImpl
    participant FR as FavouriteRepository
    participant DB as MySQL

    C->>FA: POST /api/favourites {userId, productId, likeDate}
    FA->>FS: save(favouriteDto)
    FS->>FS: map DTO → Entity
    FS->>FR: save(favourite)
    FR->>DB: INSERT INTO favourites
    DB-->>FR: saved
    FR-->>FS: Favourite entity
    FS->>FS: map Entity → DTO
    FS-->>FA: FavouriteDto
    FA-->>C: 200 OK
```
