# Installation & Running Guide

This guide provides step-by-step instructions for setting up and running the Ecommerce Microservices platform on your local machine.

---

## 📋 Prerequisites

Before you begin, ensure you have the following installed:
- **Java 17 JDK** (Standard for all services)
- **Maven 3.8+** (For building the project)
- **Docker Desktop** (For running databases, Kafka, and Zipkin)
- **Git** (To clone the repository)

---

## 🛠️ Installation Steps

### 1. Clone the Repository
```bash
git clone <repository-url>
cd ecommerce-microservices-learning
```

### 2. Configure Environment (Optional)
Most services use sensible defaults for `localhost`. However, you can customize database passwords or Kafka URIs in each service's `src/main/resources/application.yml` or by setting environment variables (e.g., `MYSQL_PASSWORD`, `EUREKA_URI`).

### 3. Build the Entire Project
From the root directory, run the Maven build command:
```bash
mvn clean install -DskipTests
```
*Note: We skip tests initially to ensure all modules are compiled and the `common-lib` is installed in your local repository.*

---

## 🚀 Running the Platform

### Step 1: Start Infrastructure (Docker)
Ensure Docker is running, then start the shared infrastructure:
```bash
docker-compose up -d
```
This will start:
- **PostgreSQL**: Port 5432 (User, Inventory)
- **MySQL**: Port 3306 (Product, Favourite)
- **MongoDB**: Port 27017 (Notification)
- **Kafka + Zookeeper**: Port 9092 (Event Streaming)
- **Zipkin**: Port 9411 (Distributed Tracing)
- **Kafka UI**: Port 8085 (Topic Management)

### Step 2: Start Core Infrastructure Services
Launch these services in order and wait for them to be fully ready:

1.  **Discovery Service** (`discovery-service`)
    - **Port:** `8761`
    - **Main URL:** [http://localhost:8761](http://localhost:8761) (Dashboard)
2.  **API Gateway** (`api-gateway`)
    - **Port:** `8080`
    - **Entry Point:** All business requests go through this port.

### Step 3: Start Business Microservices
You can start these in any order:

| Service | Port | Base URL (Direct) | Swagger UI (Main Entry Point) |
|---|---|---|---|
| **User Service** | `8081` | `http://localhost:8081` | `http://localhost:8081/swagger-ui/index.html` |
| **Inventory Service** | `8082` | `http://localhost:8082` | `http://localhost:8082/swagger-ui/index.html` |
| **Notification Service** | `8083` | `http://localhost:8083` | `http://localhost:8083/swagger-ui/index.html` |
| **Favourite Service** | `8084` | `http://localhost:8084` | `http://localhost:8084/swagger-ui.html` |
| **Product Service** | `8086` | `http://localhost:8086` | `http://localhost:8086/swagger-ui.html` |

---

## 🔗 Main Entry Point (API Gateway)

While services have their own ports, the **API Gateway (8080)** is the recommended entry point for clients. It routes requests as follows:

| Path Pattern | Target Service | Example URL |
|---|---|---|
| `/api/auth/**` | User Service | [http://localhost:8080/api/auth/login](http://localhost:8080/api/auth/login) |
| `/api/role/**` | User Service | [http://localhost:8080/api/role/all](http://localhost:8080/api/role/all) |
| `/api/inventory/**` | Inventory Service | [http://localhost:8080/api/inventory/check](http://localhost:8080/api/inventory/check) |
| `/api/email/**` | Notification Service | [http://localhost:8080/api/email/sendSimpleMail](http://localhost:8080/api/email/sendSimpleMail) |
| `/api/products/**` | Product Service | [http://localhost:8080/api/products](http://localhost:8080/api/products) |
| `/api/categories/**` | Product Service | [http://localhost:8080/api/categories](http://localhost:8080/api/categories) |
| `/api/favourites/**` | Favourite Service | [http://localhost:8080/api/favourites](http://localhost:8080/api/favourites) |

---

## 🔍 Monitoring & Debugging

- **Check Service Status:** Visit the [Eureka Dashboard](http://localhost:8761) to see which services are "UP".
- **Trace Requests:** Use [Zipkin](http://localhost:9411) to see how requests flow between services.
- **Manage Kafka Topics:** Use [Kafka UI](http://localhost:8085) to inspect messages in topics like `notificationTopic`.
- **Health Checks:** Most services provide health info at `http://localhost:<port>/actuator/health`.
