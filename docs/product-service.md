# Product Service Documentation

## Overview
The Product Service manages the **product catalog and categories** for the ecommerce platform. It supports full CRUD operations on products and categories, including hierarchical (nested) categories with pagination and sorting.

**Port:** TBD &nbsp;|&nbsp; **Database:** MySQL &nbsp;|&nbsp; **Eureka Name:** `PRODUCT-SERVICE`

---

## Features

| Feature | Status | Description |
|---|---|---|
| List All Products | ✅ | Reactive `Flux` returning all products |
| Get Product by ID | ✅ | Fetch single product by ID |
| Create Product | ✅ | Create with validation |
| Update Product | ✅ | Update by ID or full entity |
| Delete Product | ✅ | Delete by ID |
| List All Categories | ✅ | Reactive `Flux` returning all categories |
| Get Category by ID | ✅ | Fetch single category |
| Create Category | ✅ | Create with reactive `Mono` return |
| Update Category | ✅ | Update by ID or full entity |
| Delete Category | ✅ | Delete by ID |
| Category Pagination | ✅ | Paged results with `Page<CategoryDto>` |
| Category Sorting | ✅ | Paging + sorting via query params |
| Nested Categories | ✅ | Self-referencing parent/child hierarchy |
| Distributed Tracing | ✅ | Zipkin + Micrometer Brave |

---

## High-Level Design

```mermaid
graph TB
    C["Client"] -->|via Gateway| PS["Product Service"]
    PS --> MY[("MySQL<br/>product-service DB")]
    PS -.-> EU["Eureka :8761"]
    PS -.-> ZP["Zipkin :9411"]
    PS -.-> CL["common-lib"]

    subgraph Product Service
        PC["Product Controller<br/>/api/products"]
        CC["Category Controller<br/>/api/categories"]
        PSvc["Product Service"]
        CSvc["Category Service"]
        PR["Product Repository"]
        CR["Category Repository"]
    end

    PC --> PSvc --> PR --> MY
    CC --> CSvc --> CR --> MY
```

---

## Low-Level Design

### Entity Relationship

```mermaid
erDiagram
    CATEGORIES {
        Integer category_id PK
        String category_title
        String image_url
        Integer parent_category_id FK "self-referencing"
        Timestamp created_at
        Timestamp updated_at
    }

    PRODUCTS {
        Integer product_id PK
        String product_title
        String image_url
        String sku UK
        Decimal price_unit
        Integer quantity
        Integer category_id FK
        Timestamp created_at
        Timestamp updated_at
    }

    CATEGORIES ||--o{ PRODUCTS : contains
    CATEGORIES ||--o{ CATEGORIES : has_subcategories
```

### Class Diagram

```mermaid
classDiagram
    class ProductController {
        +findAll() Flux~List~ProductDto~~
        +findById(String) ResponseEntity
        +save(ProductDto) ResponseEntity
        +update(ProductDto) ResponseEntity
        +update(String, ProductDto) ResponseEntity
        +deleteById(String) ResponseEntity
    }

    class CategoryController {
        +findAll() ResponseEntity
        +getAllCategories(int, int) ResponseEntity
        +getAllEmployees(int, int, String) ResponseEntity
        +findById(String) ResponseEntity
        +save(CategoryDto) ResponseEntity
        +update(CategoryDto) ResponseEntity
        +update(String, CategoryDto) ResponseEntity
        +deleteById(String) ResponseEntity
    }

    class Product {
        -Integer productId
        -String productTitle
        -String imageUrl
        -String sku
        -Double priceUnit
        -Integer quantity
        -Category category
    }

    class Category {
        -Integer categoryId
        -String categoryTitle
        -String imageUrl
        -Set~Category~ subCategories
        -Category parentCategory
        -Set~Product~ products
    }

    class AbstractMappedEntity {
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
    }

    Product --|> AbstractMappedEntity
    Category --|> AbstractMappedEntity
    Product --> Category : belongs_to
    Category --> Category : parent
```

### API Endpoints

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/products` | List all products |
| `GET` | `/api/products/{id}` | Get product by ID |
| `POST` | `/api/products` | Create product |
| `PUT` | `/api/products` | Update product (full) |
| `PUT` | `/api/products/{id}` | Update product by ID |
| `DELETE` | `/api/products/{id}` | Delete product |
| `GET` | `/api/categories` | List all categories |
| `GET` | `/api/categories/{id}` | Get category by ID |
| `GET` | `/api/categories/paging` | Paginated categories |
| `GET` | `/api/categories/paging-and-sorting` | Paginated + sorted |
| `POST` | `/api/categories` | Create category |
| `PUT` | `/api/categories` | Update category (full) |
| `PUT` | `/api/categories/{id}` | Update category by ID |
| `DELETE` | `/api/categories/{id}` | Delete category |

---

## Flows

### Flow: Create Product

```mermaid
sequenceDiagram
    participant C as Client
    participant PC as ProductController
    participant PS as ProductService
    participant PR as ProductRepository
    participant DB as MySQL

    C->>PC: POST /api/products {productDto}
    PC->>PS: save(productDto)
    PS->>PS: map DTO → Entity (ProductMappingHelper)
    PS->>PR: save(product)
    PR->>DB: INSERT INTO products
    DB-->>PR: Product saved
    PR-->>PS: Product entity
    PS->>PS: map Entity → DTO
    PS-->>PC: ProductDto
    PC-->>C: 200 OK {productDto}
```

### Flow: Get Paginated Categories

```mermaid
sequenceDiagram
    participant C as Client
    participant CC as CategoryController
    participant CS as CategoryService
    participant CR as CategoryRepository
    participant DB as MySQL

    C->>CC: GET /api/categories/paging?page=0&size=10
    CC->>CS: findAllCategory(0, 10)
    CS->>CR: findAll(PageRequest.of(0, 10))
    CR->>DB: SELECT * FROM categories LIMIT 10 OFFSET 0
    DB-->>CR: Page<Category>
    CR-->>CS: Page<Category>
    CS->>CS: map each Entity → DTO
    CS-->>CC: Page<CategoryDto>
    CC-->>C: 200 OK {content, totalElements, totalPages}
```
