# Sample Product DTO Requests

> **Endpoint:** `POST /api/products`  
> **Content-Type:** `multipart/form-data`  
> **Parts:**  
> - `dto` — JSON string (the `ProductDto` payload, sent as `application/json`)  
> - `files` *(optional)* — one or more image files (`multipart/form-data`)

---

## How to Send (Postman / cURL)

### Postman
1. Method: `POST`  
2. URL: `http://localhost:8080/api/products`  
3. Body → `form-data`  
   | Key    | Type | Value                      |
   |--------|------|----------------------------|
   | `dto`  | Text | *(paste the JSON below)*   |
   | `files`| File | *(attach image files)*     |
4. Set `Content-Type` of the `dto` field to `application/json`.

### cURL
```bash
curl -X POST http://localhost:8080/api/products \
  -F 'dto=<product.json;type=application/json' \
  -F 'files=@/path/to/image1.jpg' \
  -F 'files=@/path/to/image2.jpg'
```

---

## Sample 1 — Smartphone (Electronics, with sale price)

```json
{
  "sku": "ELEC-SM-001",
  "slug": "samsung-galaxy-s24-ultra-256gb",
  "externalId": "SGS24U-BLK-256",
  "productType": "PHYSICAL",
  "categoryId": "cat-electronics-smartphones",
  "brand": "Samsung",
  "manufacturer": "Samsung Electronics Co., Ltd.",
  "name": "Samsung Galaxy S24 Ultra 256GB – Titanium Black",
  "shortDescription": "The ultimate Android flagship with S Pen, 200MP camera, and AI-powered features.",
  "description": "Experience the next level of mobile innovation with the Galaxy S24 Ultra. Features a 6.8-inch Dynamic AMOLED 2X display, the built-in S Pen, a 200MP pro-grade camera system, and Galaxy AI for smarter productivity.",
  "price": 129999.00,
  "salePrice": 114999.00,
  "costPrice": 85000.00,
  "compareAtPrice": 134999.00,
  "currencyCode": "INR",
  "taxInclusive": true,
  "stockQuantity": 150,
  "reservedQuantity": 0,
  "stockStatus": "IN_STOCK",
  "backorderAllowed": false,
  "weightGrams": 232.00,
  "requiresShipping": true,
  "status": "ACTIVE",
  "featured": true,
  "meta": [
    { "metaKey": "color", "metaValue": "Titanium Black" },
    { "metaKey": "storage", "metaValue": "256GB" },
    { "metaKey": "ram", "metaValue": "12GB" },
    { "metaKey": "os", "metaValue": "Android 14" }
  ],
  "variants": [
    { "sku": "ELEC-SM-001-256-BLK", "name": "256GB Titanium Black", "price": 114999.00, "stockQuantity": 80 },
    { "sku": "ELEC-SM-001-512-BLK", "name": "512GB Titanium Black", "price": 134999.00, "stockQuantity": 40 },
    { "sku": "ELEC-SM-001-256-GRY", "name": "256GB Titanium Gray", "price": 114999.00, "stockQuantity": 30 }
  ]
}
```

---

## Sample 2 — Men's Running Shoes (Fashion, physical)

```json
{
  "sku": "FASH-SHOE-MR-002",
  "slug": "nike-air-zoom-pegasus-41-mens-running-shoe",
  "externalId": "NK-AZP41-BLU-10",
  "productType": "PHYSICAL",
  "categoryId": "cat-fashion-footwear-mens",
  "brand": "Nike",
  "manufacturer": "Nike, Inc.",
  "name": "Nike Air Zoom Pegasus 41 – Men's Running Shoe",
  "shortDescription": "Lightweight, responsive running shoe with Air Zoom cushioning.",
  "description": "The Nike Air Zoom Pegasus 41 delivers a smooth, cushioned ride whether you're training for a race or just getting your daily miles in. Updated ReactX foam provides more energy return than previous generations.",
  "price": 10995.00,
  "salePrice": null,
  "costPrice": 6500.00,
  "compareAtPrice": 12995.00,
  "currencyCode": "INR",
  "taxInclusive": true,
  "stockQuantity": 300,
  "reservedQuantity": 10,
  "stockStatus": "IN_STOCK",
  "backorderAllowed": false,
  "weightGrams": 310.00,
  "requiresShipping": true,
  "status": "ACTIVE",
  "featured": false,
  "meta": [
    { "metaKey": "material", "metaValue": "Mesh upper, ReactX foam midsole" },
    { "metaKey": "closure", "metaValue": "Lace-up" },
    { "metaKey": "gender", "metaValue": "Men" },
    { "metaKey": "sport", "metaValue": "Running" }
  ],
  "variants": [
    { "sku": "FASH-SHOE-MR-002-UK8-BLU", "name": "UK 8 – Blue", "price": 10995.00, "stockQuantity": 60 },
    { "sku": "FASH-SHOE-MR-002-UK9-BLU", "name": "UK 9 – Blue", "price": 10995.00, "stockQuantity": 80 },
    { "sku": "FASH-SHOE-MR-002-UK10-BLU", "name": "UK 10 – Blue", "price": 10995.00, "stockQuantity": 70 },
    { "sku": "FASH-SHOE-MR-002-UK11-BLU", "name": "UK 11 – Blue", "price": 10995.00, "stockQuantity": 40 }
  ]
}
```

---

## Sample 3 — Wireless Noise-Cancelling Headphones (Electronics, draft)

```json
{
  "sku": "ELEC-AUDIO-WH-003",
  "slug": "sony-wh1000xm5-wireless-noise-cancelling-headphones",
  "externalId": "SONY-WH1000XM5-BLK",
  "productType": "PHYSICAL",
  "categoryId": "cat-electronics-audio",
  "brand": "Sony",
  "manufacturer": "Sony Corporation",
  "name": "Sony WH-1000XM5 Wireless Noise-Cancelling Headphones",
  "shortDescription": "Industry-leading noise cancellation with 30-hour battery life.",
  "description": "The WH-1000XM5 headphones come with eight microphones and two processors to deliver unrivalled noise cancellation. The new HD Noise Cancelling Processor QN2 runs at 8x the processing power of the previous model.",
  "price": 29990.00,
  "salePrice": 24990.00,
  "costPrice": 18000.00,
  "compareAtPrice": 32990.00,
  "currencyCode": "INR",
  "taxInclusive": false,
  "stockQuantity": 75,
  "reservedQuantity": 5,
  "stockStatus": "IN_STOCK",
  "backorderAllowed": true,
  "weightGrams": 250.00,
  "requiresShipping": true,
  "status": "DRAFT",
  "featured": false,
  "meta": [
    { "metaKey": "connectivity", "metaValue": "Bluetooth 5.2" },
    { "metaKey": "battery_life", "metaValue": "30 hours" },
    { "metaKey": "driver_size", "metaValue": "30mm" },
    { "metaKey": "foldable", "metaValue": "Yes" }
  ],
  "variants": [
    { "sku": "ELEC-AUDIO-WH-003-BLK", "name": "Black", "price": 24990.00, "stockQuantity": 50 },
    { "sku": "ELEC-AUDIO-WH-003-SLV", "name": "Silver", "price": 24990.00, "stockQuantity": 25 }
  ]
}
```

---

## Sample 4 — Organic Green Tea (Food & Beverage, low stock)

```json
{
  "sku": "FOOD-TEA-OGT-004",
  "slug": "twinings-organic-green-tea-50-bags",
  "externalId": "TWN-OGT-50B",
  "productType": "PHYSICAL",
  "categoryId": "cat-food-beverages-tea",
  "brand": "Twinings",
  "manufacturer": "Twinings & Company Ltd.",
  "name": "Twinings Organic Green Tea – 50 Tea Bags",
  "shortDescription": "Pure, organic green tea with a delicate, fresh flavour. Certified organic.",
  "description": "Twinings Organic Green Tea is sourced from certified organic farms. Each bag delivers a light, grassy cup with natural antioxidants. No artificial flavours, colours, or preservatives. Suitable for vegans.",
  "price": 549.00,
  "salePrice": 499.00,
  "costPrice": 280.00,
  "compareAtPrice": 599.00,
  "currencyCode": "INR",
  "taxInclusive": true,
  "stockQuantity": 8,
  "reservedQuantity": 2,
  "stockStatus": "LOW_STOCK",
  "backorderAllowed": false,
  "weightGrams": 90.00,
  "requiresShipping": true,
  "status": "ACTIVE",
  "featured": false,
  "meta": [
    { "metaKey": "certification", "metaValue": "USDA Organic, Non-GMO" },
    { "metaKey": "caffeine", "metaValue": "Low" },
    { "metaKey": "allergens", "metaValue": "None" },
    { "metaKey": "shelf_life", "metaValue": "24 months" }
  ]
}
```

---

## Sample 5 — E-Book: Clean Code (Digital product, no shipping)

```json
{
  "sku": "DIGI-BOOK-CC-005",
  "slug": "clean-code-a-handbook-of-agile-software-craftsmanship-ebook",
  "externalId": "PEAR-CC-EB-978",
  "productType": "DIGITAL",
  "categoryId": "cat-books-technology-programming",
  "brand": "Pearson Education",
  "manufacturer": "Pearson Education, Inc.",
  "name": "Clean Code: A Handbook of Agile Software Craftsmanship – eBook",
  "shortDescription": "Robert C. Martin's definitive guide to writing clean, maintainable code.",
  "description": "Even bad code can function. But if code isn't clean, it can bring a development organisation to its knees. Every year, countless hours and significant resources are lost because of poorly written code. But it doesn't have to be that way. This book covers the principles, patterns, and practices of writing clean code.",
  "price": 2499.00,
  "salePrice": 1799.00,
  "costPrice": 800.00,
  "compareAtPrice": 2999.00,
  "currencyCode": "INR",
  "taxInclusive": true,
  "stockQuantity": 99999,
  "reservedQuantity": 0,
  "stockStatus": "IN_STOCK",
  "backorderAllowed": false,
  "weightGrams": null,
  "requiresShipping": false,
  "status": "ACTIVE",
  "featured": true,
  "meta": [
    { "metaKey": "author", "metaValue": "Robert C. Martin" },
    { "metaKey": "pages", "metaValue": "431" },
    { "metaKey": "format", "metaValue": "PDF, ePub, MOBI" },
    { "metaKey": "language", "metaValue": "English" },
    { "metaKey": "isbn", "metaValue": "978-0132350884" }
  ]
}
```

---

## Field Reference (Request-Only Fields)

| Field | Type | Required | Notes |
|---|---|---|---|
| `sku` | String | Yes | Unique product identifier |
| `slug` | String | No | URL-friendly name; auto-generated if omitted |
| `externalId` | String | No | Third-party / ERP ID |
| `productType` | String | Yes | `PHYSICAL` \| `DIGITAL` \| `SERVICE` |
| `categoryId` | String | No | UUID of the category |
| `brand` | String | No | Brand name |
| `manufacturer` | String | No | Manufacturer name |
| `name` | String | Yes | Product display name |
| `shortDescription` | String | No | One-liner summary |
| `description` | String | No | Full rich-text description |
| `price` | Decimal | Yes | Base price |
| `salePrice` | Decimal | No | Discounted price (activates `onSale`) |
| `costPrice` | Decimal | No | Internal cost (not shown to customers) |
| `compareAtPrice` | Decimal | No | Strikethrough "was" price |
| `currencyCode` | String | No | ISO 4217 code (default: `INR`) |
| `taxInclusive` | Boolean | No | Whether price already includes tax |
| `stockQuantity` | Integer | Yes | Total units on hand |
| `reservedQuantity` | Integer | No | Units held for pending orders |
| `stockStatus` | String | No | `IN_STOCK` \| `LOW_STOCK` \| `OUT_OF_STOCK` \| `ON_BACKORDER` \| `ON_PREORDER` |
| `backorderAllowed` | Boolean | No | Allow orders when out of stock |
| `weightGrams` | Decimal | No | Weight in grams; `null` for digital |
| `requiresShipping` | Boolean | No | `false` for digital products |
| `status` | String | No | `DRAFT` (default) \| `ACTIVE` \| `ARCHIVED` |
| `featured` | Boolean | No | Pin to featured listings |
| `meta` | Array | No | Key-value metadata pairs |
| `variants` | Array | No | Product size/colour variants |

> **Read-only fields** (returned in responses, ignored on create):  
> `id`, `slug` (if not supplied), `effectivePrice`, `availableStock`, `onSale`,  
> `averageRating`, `reviewCount`, `images`, `documents`, `reviews`,  
> `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, `version`
