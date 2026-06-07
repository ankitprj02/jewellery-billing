# API Documentation

Base URL: `http://localhost:8080/api` (local) | `https://your-backend.onrender.com/api` (production)

All endpoints except `/auth/login` and `/health` require JWT authentication.

**Header:** `Authorization: Bearer <token>`

---

## Authentication

### POST /auth/login
Login and receive JWT token.

**Request Body:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "admin",
    "role": "ADMIN",
    "message": "Login successful"
  }
}
```

---

## Health Check

### GET /health
Public health check endpoint.

**Response:**
```json
{
  "success": true,
  "data": { "status": "UP", "service": "jewellery-billing" }
}
```

---

## Dashboard

### GET /dashboard/stats
Returns dashboard statistics.

**Response:**
```json
{
  "success": true,
  "data": {
    "totalCustomers": 10,
    "totalProducts": 25,
    "totalInvoices": 50,
    "totalRevenue": 1500000.00,
    "recentInvoices": [...],
    "monthlySales": { "2024-01": 250000.00, "2024-02": 300000.00 }
  }
}
```

---

## Customers

### GET /customers
List all customers. Optional query: `?search=keyword`

### GET /customers/{id}
Get customer by ID.

### POST /customers
Create a new customer.

**Request Body:**
```json
{
  "name": "Rajesh Kumar",
  "mobileNumber": "9876543210",
  "address": "Mumbai, Maharashtra"
}
```

### PUT /customers/{id}
Update customer.

### DELETE /customers/{id}
Delete customer.

---

## Products

### GET /products
List all products. Optional query: `?search=keyword`

### GET /products/{id}
Get product by ID.

### POST /products
Create a new product.

**Request Body:**
```json
{
  "productName": "Gold Ring 22K",
  "category": "Gold",
  "currentRate": 6500.00,
  "description": "22 Karat gold ring"
}
```

### PUT /products/{id}
Update product.

### DELETE /products/{id}
Delete product.

---

## Invoices

### GET /invoices
List all invoices.

**Query Parameters:**
- `search` — Search by invoice number
- `startDate` & `endDate` — Filter by date range (ISO format: YYYY-MM-DD)

### GET /invoices/{id}
Get invoice details with items.

### POST /invoices
Create a new invoice.

**Request Body:**
```json
{
  "customerId": 1,
  "invoiceDate": "2024-06-06",
  "items": [
    {
      "itemName": "Gold Chain",
      "weight": 10.5,
      "ratePerGram": 6500.00,
      "makingCharges": 2000.00,
      "gstPercentage": 3.0
    }
  ]
}
```

**Calculation:**
- Base Amount = Weight × Rate
- Subtotal = Base Amount + Making Charges
- GST Amount = Subtotal × GST / 100
- Item Total = Subtotal + GST Amount

### GET /invoices/{id}/pdf
Download invoice as PDF file.

---

## Settings

### GET /settings
Get shop settings.

### PUT /settings
Update shop settings.

**Request Body:**
```json
{
  "shopName": "Golden Jewellers",
  "address": "123 Main Street, Mumbai",
  "phone": "+91 98765 43210",
  "email": "info@goldenjewellers.com",
  "gstin": "27AAAAA0000A1Z5",
  "signatureText": "Authorized Signatory"
}
```

---

## Error Responses

```json
{
  "success": false,
  "message": "Error description"
}
```

**HTTP Status Codes:**
- `200` — Success
- `201` — Created
- `400` — Bad Request / Validation Error
- `401` — Unauthorized
- `404` — Not Found
- `500` — Internal Server Error
