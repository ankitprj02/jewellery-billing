# Jewellery Shop Billing & Inventory Management System

A full-stack web application for jewellery shop billing, inventory management, and invoice generation built with **Java Spring Boot**, **MySQL**, and a responsive **Bootstrap 5** frontend.

## Features

- **Authentication** — JWT-based admin login with Spring Security
- **Dashboard** — Stats, recent invoices, and sales analytics chart
- **Customer Management** — CRUD with search
- **Product Management** — Gold/Silver products with rates
- **Invoice/Billing** — Multi-item invoices with auto GST calculation
- **Invoice History** — Search, date filter, view details
- **PDF Generation** — Professional invoice PDF with shop details
- **Settings** — Configure shop details for invoices
- **Dark Mode** — Toggle light/dark theme
- **Mobile Responsive** — Bootstrap 5 responsive design

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Spring Boot 4, Spring Security, Spring Data JPA |
| Database | MySQL 8 |
| Frontend | HTML5, CSS3, JavaScript, Bootstrap 5, Chart.js |
| PDF | OpenPDF |
| Auth | JWT (jjwt) |
| Build | Maven |
| Deployment | Vercel (frontend), Render (backend), Railway (MySQL) |

## Project Structure

```
jewellery-billing/
├── src/main/java/com/ankit/jewellery_billing/
│   ├── config/          # Security, CORS, data initializer
│   ├── controller/      # REST API controllers
│   ├── dto/             # Request/Response DTOs
│   ├── entity/          # JPA entities
│   ├── exception/       # Global exception handling
│   ├── repository/      # Spring Data JPA repositories
│   ├── security/        # JWT filter, token provider
│   ├── service/         # Business logic
│   └── util/            # Mappers, calculators
├── src/main/resources/
│   └── application.properties
├── frontend/
│   ├── css/             # Stylesheets
│   ├── js/              # API client, common utilities
│   ├── pages/           # Dashboard, customers, products, etc.
│   └── index.html       # Login page
├── docs/
│   ├── API_DOCUMENTATION.md
│   ├── DATABASE_SCHEMA.sql
│   └── ERD.md
├── DEPLOYMENT_GUIDE.md
├── pom.xml
└── README.md
```

## Quick Start

### Prerequisites
- Java 21+
- MySQL 8+
- Node.js (optional, for serving frontend)

### 1. Database Setup
```bash
mysql -u root -p < docs/DATABASE_SCHEMA.sql
```

### 2. Run Backend
```bash
./mvnw spring-boot:run
```
Backend starts at `http://localhost:8080`

### 3. Run Frontend
```bash
cd frontend && npx serve .
```
Open the URL shown (e.g. `http://localhost:3000`)

### 4. Login
| Field | Value |
|-------|-------|
| Username | `admin` |
| Password | `admin123` |

## API Overview

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Admin login |
| GET | `/api/dashboard/stats` | Dashboard statistics |
| CRUD | `/api/customers` | Customer management |
| CRUD | `/api/products` | Product management |
| CRUD | `/api/invoices` | Invoice management |
| GET | `/api/invoices/{id}/pdf` | Download invoice PDF |
| GET/PUT | `/api/settings` | Shop settings |

See [docs/API_DOCUMENTATION.md](docs/API_DOCUMENTATION.md) for full API reference.

## Invoice Calculation

```
Base Amount    = Weight × Rate per Gram
Subtotal       = Base Amount + Making Charges
GST Amount     = Subtotal × GST% / 100
Item Total     = Subtotal + GST Amount
Grand Total    = Sum of all Item Totals
```

## Documentation

- [API Documentation](docs/API_DOCUMENTATION.md)
- [Database Schema](docs/DATABASE_SCHEMA.sql)
- [Entity Relationship Diagram](docs/ERD.md)
- [Deployment Guide](DEPLOYMENT_GUIDE.md)

## Deployment

| Component | Platform |
|-----------|----------|
| Frontend | [Vercel](https://vercel.com) |
| Backend | [Render](https://render.com) |
| Database | [Railway MySQL](https://railway.app) |

See [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) for step-by-step instructions.

## License

This project is built for educational/internship purposes.
