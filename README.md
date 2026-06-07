# JewelBill — Jewellery Shop Billing & Inventory Management System

> A full-stack, production-ready web application for jewellery shop billing, inventory management, and professional invoice generation — live on the cloud.

🌐 **Live Demo**: [https://jewellery-billing.onrender.com](https://jewellery-billing.onrender.com)  
📦 **GitHub**: [https://github.com/ankitprj02/jewellery-billing](https://github.com/ankitprj02/jewellery-billing)

---

## 📸 Overview

JewelBill is a unified, enterprise-grade billing and inventory system built specifically for jewellery shops. It provides a complete business management suite — from product and customer management, to live-rate invoicing, PDF generation, and analytics — all accessible through a responsive, modern web interface.

The entire application (frontend + backend) runs as a **single Spring Boot service**, deployed on **Render** via Docker, with data stored in **Supabase (PostgreSQL)**.

---

## 🚀 Key Features

### 🔐 Authentication & Security
- **Local Registration** — Create admin accounts from the login page with BCrypt-hashed passwords
- **Google Sign-In** — OAuth 2.0 integration for one-click login via Google accounts
- **JWT-Protected API** — All `/api` routes require a valid Bearer token; stateless and secure
- **Google Token Verification** — Backend cryptographically verifies Google ID tokens using Google's public key provider

### 📊 Dashboard & Analytics
- Real-time business statistics (total sales, customers, products, invoices)
- Sales trend chart powered by **Chart.js**
- Recent invoices summary at a glance

### 👥 Customer Management
- Full CRUD — Add, view, edit, and delete customer records
- Instant search and paginated listing
- Customer details linked to invoice history

### 💎 Product Management
- Manage gold and silver jewellery inventory
- Set dynamic live rates per gram for each metal type
- Product weight and category tracking

### 🧾 Billing & Invoice Generation
- Multi-item interactive billing engine
- Live metal rate integration with automatic calculation
- Making charges support per item
- **GST auto-calculation** — CGST 1.5% + SGST 1.5% = 3% total
- Save and review invoices before finalizing

### 📄 PDF Invoice Export
- High-fidelity PDF generation using **OpenPDF**
- Includes shop name, address, GSTIN, customer details, and itemized billing
- Customizable shop details via the Settings page

### 📜 Invoice History
- Complete searchable and filterable audit trail
- Filter by customer name or date range
- Inline invoice detail view with PDF download option

### ⚙️ Shop Settings
- Configure shop name, address, GSTIN, and contact details
- Settings reflected in all generated PDF invoices

### 🌙 Dark Mode & Responsive Design
- Toggle between light and dark themes
- Fully responsive — works on desktop, tablet, and mobile

---

## 🛠️ Technology Stack

| Layer | Technologies |
|---|---|
| **Backend Framework** | Java 21, Spring Boot 4.0, Spring Security 7.0, Spring Data JPA |
| **Database** | PostgreSQL 17 (hosted on **Supabase** — cloud-managed) |
| **Frontend** | HTML5, CSS3, JavaScript (ES6+), Bootstrap 5, Bootstrap Icons, Chart.js |
| **PDF Generation** | OpenPDF |
| **Authentication** | JWT (jjwt library), Google Identity Services (OAuth 2.0) |
| **Build Tool** | Apache Maven |
| **Containerization** | Docker (`eclipse-temurin:21-jre`) |
| **Deployment** | Render (Docker Web Service) |
| **Database Provider** | Supabase (PostgreSQL with Session Mode Pooler) |

---

## 📂 Project Structure

```text
jewellery-billing/
├── Dockerfile                          # Docker build definition for Render deployment
├── pom.xml                             # Maven dependency configuration
├── src/
│   └── main/
│       ├── java/com/ankit/jewellery_billing/
│       │   ├── config/                 # SecurityConfig, CORS, DataInit
│       │   ├── controller/             # REST API controllers
│       │   │   ├── AuthController.java
│       │   │   ├── CustomerController.java
│       │   │   ├── DashboardController.java
│       │   │   ├── HealthController.java
│       │   │   ├── InvoiceController.java
│       │   │   ├── ProductController.java
│       │   │   └── SettingsController.java
│       │   ├── dto/                    # Request/Response DTOs
│       │   ├── entity/                 # JPA entities (User, Customer, Product, Invoice, InvoiceItem, ShopSettings)
│       │   ├── exception/              # Global exception handlers
│       │   ├── repository/             # Spring Data JPA repositories
│       │   ├── security/               # JWT filter & Google token verifier
│       │   ├── service/                # Business logic services
│       │   └── util/                   # PDF generation utilities
│       └── resources/
│           ├── static/                 # Bundled frontend (HTML, CSS, JS pages)
│           ├── application.properties  # Main configuration (reads from env vars)
│           └── application-local.properties  # Local secrets (gitignored)
├── frontend/                           # Frontend source files
│   ├── index.html                      # Login page
│   └── pages/
│       ├── dashboard.html
│       ├── customers.html
│       ├── products.html
│       ├── create-invoice.html
│       ├── invoice-history.html
│       └── settings.html
└── README.md
```

---

## ⚙️ Quick Start (Local Development)

### Prerequisites
- **Java Development Kit (JDK) 21+**
- **Maven** (or use the included `./mvnw` wrapper)
- A **Supabase** project (or local PostgreSQL instance)
- *(Optional)* Google Cloud Console credentials for Google Sign-In

### 1. Clone the Repository
```bash
git clone https://github.com/ankitprj02/jewellery-billing.git
cd jewellery-billing
```

### 2. Configure Local Database Credentials
Create `src/main/resources/application-local.properties` (this file is gitignored):

```properties
# Supabase PostgreSQL Session Pooler — get from Supabase Dashboard > Settings > Database
spring.datasource.url=jdbc:postgresql://<YOUR_POOLER_HOST>:5432/postgres?sslmode=require
spring.datasource.username=postgres.<YOUR_PROJECT_REF>
spring.datasource.password=<YOUR_DATABASE_PASSWORD>

# Optional: Google OAuth Client ID
google.client.id=<YOUR_GOOGLE_CLIENT_ID>
```

### 3. Build the Application
```bash
./mvnw clean package -DskipTests
```

### 4. Run Locally
```bash
java -jar target/jewellery-billing-0.0.1-SNAPSHOT.jar
```
Open **[http://localhost:8080](http://localhost:8080)** in your browser.

### 5. First Login
Click **"Don't have an account? Create one"** on the login screen to register your first admin account. Once registered, you'll be redirected to the dashboard automatically.

---

## 📈 Invoice Calculation Logic

Invoices are computed using the following formulas:

| Formula | Calculation |
|---|---|
| **Base Amount** | Weight (g) × Live Rate per Gram |
| **Subtotal** | Base Amount + Making Charges |
| **GST (CGST 1.5% + SGST 1.5%)** | Subtotal × 3.0% |
| **Grand Total** | Subtotal + GST Amount |

---

## 🔒 Security Practices

| Practice | Details |
|---|---|
| **Password Hashing** | BCrypt adaptive hashing before database persistence |
| **Stateless JWT Auth** | All `/api/**` routes require `Authorization: Bearer <token>` |
| **Google Token Verification** | ID tokens verified using Google's public key infrastructure |
| **No Hardcoded Secrets** | All sensitive values read from environment variables at runtime |
| **SSL/TLS Database** | Supabase connections use `sslmode=require` |

---

## 🌐 Live Deployment

The application is deployed as a **Docker Web Service on Render**:

- **Live URL**: [https://jewellery-billing.onrender.com](https://jewellery-billing.onrender.com)
- **Database**: Supabase PostgreSQL (Session Mode Pooler, `ap-south-1` Mumbai region)
- **Container**: `eclipse-temurin:21-jre` base image
- **Port**: `8080`

> **Note**: The free tier on Render will spin down after inactivity. The first request after sleep may take ~30–60 seconds to respond. Upgrade to a paid instance for always-on availability.

---

## 📖 Deployment Guide

For full deployment instructions (Supabase setup, Google OAuth configuration, Render Docker deployment), refer to [DEPLOYMENT_GUIDE.md](./DEPLOYMENT_GUIDE.md).

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m "feat: add your feature"`
4. Push to the branch: `git push origin feature/your-feature-name`
5. Open a Pull Request

---

## 👤 Author

**Ankit Prajapati**  
GitHub: [@ankitprj02](https://github.com/ankitprj02)

---

## 📄 License

This project is for educational and portfolio purposes.
