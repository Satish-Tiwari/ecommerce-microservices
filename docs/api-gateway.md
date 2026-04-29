# API Gateway Documentation

## Overview
The API Gateway is the **single entry point** for all client requests. Built on **Spring Cloud Gateway**, it routes incoming HTTP requests to the appropriate downstream microservice using Eureka-based service discovery and client-side load balancing.

**Port:** `8080` &nbsp;|&nbsp; **Eureka Name:** `API-GATEWAY`

---

## Features

| Feature | Status | Description |
|---|---|---|
| Dynamic Routing | ✅ | Routes to services via `lb://SERVICE-NAME` |
| Service Discovery | ✅ | Integrates with Eureka for automatic resolution |
| Load Balancing | ✅ | Client-side load balancing via Spring Cloud LoadBalancer |
| Path-based Routing | ✅ | Routes based on URL path predicates |
| Centralized Logging | ✅ | TRACE-level gateway logs, DEBUG-level Netty logs |

---

## High-Level Design

```mermaid
graph LR
    C["Client"] -->|HTTP :8080| GW["API Gateway"]

    GW -->|/api/auth/**<br/>/api/role/**<br/>/api/manager/**| US["User Service<br/>:8081"]
    GW -->|/api/inventory/**| IS["Inventory Service<br/>:8082"]
    GW -->|/api/email/**| NS["Notification Service<br/>:8083"]

    GW -.->|discover| EU["Eureka :8761"]
```

---

## Low-Level Design

### Route Configuration

| Route ID | Path Predicate | Target Service | Load Balanced |
|---|---|---|---|
| `user-service-auth` | `/api/auth/**` | `lb://USER-SERVICE` | ✅ |
| `user-service-information` | `/api/role/**` | `lb://USER-SERVICE` | ✅ |
| `user-service-manager` | `/api/manager/**` | `lb://USER-SERVICE` | ✅ |
| `inventory-service` | `/api/inventory/**` | `lb://INVENTORY-SERVICE` | ✅ |
| `notification-service-notifications` | `/api/email/**` | `lb://NOTIFICATION-SERVICE` | ✅ |

### Class Diagram

```mermaid
classDiagram
    class ApiGatewayApplication {
        +main(String[] args)
    }
    note for ApiGatewayApplication "@SpringBootApplication\nPure YAML-driven gateway\nNo custom filters (yet)"
```

### Configuration Highlights
- **Discovery Locator enabled** — Auto-creates routes for all Eureka-registered services
- **`lower-case-service-id: true`** — Normalizes service IDs to lowercase in auto-routes

---

## Flow: Request Routing

```mermaid
sequenceDiagram
    participant C as Client
    participant GW as API Gateway :8080
    participant EU as Eureka :8761
    participant US as User Service :8081

    C->>GW: POST /api/auth/login
    GW->>EU: Resolve USER-SERVICE
    EU-->>GW: [192.168.1.5:8081]
    GW->>US: POST /api/auth/login
    US-->>GW: 200 OK {JWT tokens}
    GW-->>C: 200 OK {JWT tokens}
```
