# Ecommerce Microservices — Architecture Overview

## High-Level System Design

```mermaid
graph TB
    subgraph Client
        WEB["Web / Mobile Client"]
    end

    subgraph Infrastructure
        GW["API Gateway<br/>:8080"]
        DS["Discovery Service<br/>(Eureka) :8761"]
    end

    subgraph Business Services
        US["User Service<br/>:8081"]
        PS["Product Service<br/>:8082 (TBD)"]
        IS["Inventory Service<br/>:8082"]
        FS["Favourite Service<br/>:8084 (TBD)"]
        NS["Notification Service<br/>:8083"]
    end

    subgraph Shared
        CL["Common Lib<br/>(shared JAR)"]
    end

    subgraph Data Stores
        PG1[("PostgreSQL<br/>user-service")]
        PG2[("PostgreSQL<br/>inventory-service")]
        MY1[("MySQL<br/>product-service")]
        MY2[("MySQL<br/>favourite-service")]
        MG[("MongoDB<br/>notification-service")]
    end

    subgraph Messaging
        KF["Apache Kafka<br/>:9092"]
        ZK["Zookeeper<br/>:2181"]
    end

    subgraph Observability
        ZP["Zipkin<br/>:9411"]
    end

    WEB -->|HTTP| GW
    GW -->|Route| US
    GW -->|Route| PS
    GW -->|Route| IS
    GW -->|Route| FS
    GW -->|Route| NS

    US -.->|register| DS
    PS -.->|register| DS
    IS -.->|register| DS
    FS -.->|register| DS
    NS -.->|register| DS
    GW -.->|discover| DS

    US --> PG1
    IS --> PG2
    PS --> MY1
    FS --> MY2
    NS --> MG

    NS -->|consume| KF
    KF --> ZK

    US -.->|traces| ZP
    PS -.->|traces| ZP
    IS -.->|traces| ZP
    NS -.->|traces| ZP

    PS -.-> CL
    FS -.-> CL
```

## Service Registry

| Service | Port | Database | Eureka Name |
|---|---|---|---|
| Discovery Service | 8761 | — | DISCOVERY-SERVICE |
| API Gateway | 8080 | — | API-GATEWAY |
| User Service | 8081 | PostgreSQL | USER-SERVICE |
| Inventory Service | 8082 | PostgreSQL | INVENTORY-SERVICE |
| Notification Service | 8083 | MongoDB | NOTIFICATION-SERVICE |
| Product Service | TBD | MySQL | PRODUCT-SERVICE |
| Favourite Service | TBD | MySQL | FAVOURITE-SERVICE |

## Technology Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.3 |
| Cloud | Spring Cloud 2025.1.0 |
| Service Discovery | Netflix Eureka |
| API Gateway | Spring Cloud Gateway |
| Security | Spring Security + JWT (HS512) |
| ORM | Spring Data JPA / Hibernate |
| Databases | PostgreSQL, MySQL, MongoDB |
| Messaging | Apache Kafka |
| Tracing | Zipkin + Sleuth |
| Docs | SpringDoc OpenAPI (Swagger) |
| Build | Maven (multi-module) |
| Containerization | Docker + Docker Compose |

## Communication Patterns

| Pattern | Usage |
|---|---|
| Synchronous (REST) | Client → Gateway → Service |
| Service Discovery | Eureka-based `lb://SERVICE-NAME` routing |
| Async Messaging | Kafka for payment/order event notifications |
| Reactive | WebFlux `Mono`/`Flux` wrappers for non-blocking I/O |

## Package Convention

All services follow the package naming standard:
```
com.ecommerce.<service_name>
```

Examples:
- `com.ecommerce.user_service`
- `com.ecommerce.product_service`
- `com.ecommerce.common_lib`
