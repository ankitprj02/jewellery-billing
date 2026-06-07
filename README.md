# JewelBill — Jewellery Shop Billing & Inventory Management System

A unified, full-stack enterprise web application for jewellery shop billing, inventory management, and professional invoice generation. Built with **Java Spring Boot**, **PostgreSQL (Supabase)**, and a responsive, modern **Bootstrap 5** frontend served from a single origin.

---

## 🚀 Key Features

* **Unified Single-Origin Architecture** — Frontend static files are packaged and served directly from the Spring Boot server (`http://localhost:8080/`), eliminating external hosting (like Vercel) and cross-origin (CORS) complexity.
* **Modern Authentication Flow** — Secure login and register capability featuring:
  * **Local Admin Registration**: Create custom admin accounts directly from the login page, secured with `BCrypt` hash encryption.
  * **Google Sign-In integration**: Dual local and Google OAuth 2.0 single sign-on mechanism.
  * **JWT Security**: Protected stateless `/api` routes validated via authorization headers containing JWT tokens.
* **Dashboard & Analytics** — Rich analytics displaying real-time business statistics, recent invoices, and a sales analytics trend chart.
* **Customer Management** — Full CRUD management system with responsive pagination and instant search.
* **Product Management** — Gold and silver inventory control with dynamic rates per gram.
* **Billing & Invoicing** — Interactive billing engine supporting multi-item orders, live metal rate estimation, making charges, and automated SGST/CGST calculation.
* **Invoice History** — Complete audit trail of invoices with customer search, date range filtering, and inline details.
* **PDF Printing engine** — Generates high-fidelity PDF invoices on the fly using **OpenPDF**, formatted with customized shop name, address, and GSTIN settings.
* **Responsive Visuals** — Supports **Dark Mode** toggle and adapts to desktops, tablets, and mobile devices natively.

---

## 🛠️ Technology Stack

| Layer | Technologies |
|---|---|
| **Core Framework** | Java 21, Spring Boot 4.0, Spring Data JPA, Spring Security 7.0 |
| **Database** | PostgreSQL 17 (Hosted securely on **Supabase**) |
| **Frontend** | HTML5, CSS3, JavaScript (ES6+), Bootstrap 5, Bootstrap Icons, Chart.js |
| **PDF Rendering** | OpenPDF |
| **Auth Strategy** | Stateless JSON Web Tokens (jjwt), Google Identity Services (OAuth 2.0) |
| **Build Automation** | Maven |
| **Target Deployment** | Render, Railway, or AWS Elastic Beanstalk (Single Unified JAR) |

---

## 📂 Project Directory Structure

```text
jewellery-billing/
├── src/main/java/com/ankit/jewellery_billing/
│   ├── config/          # Security configuration, CORS configurations, database initialization
│   ├── controller/      # REST API endpoints (Auth, Invoices, Products, Customers, Settings)
│   ├── dto/             # Data Transfer Objects (Register, login requests)
│   ├── entity/          # JPA database models (User, Customer, Product, Invoice, ShopSettings)
│   ├── exception/       # Global MVC exception handling mapping
│   ├── repository/      # Spring Data JPA repository interfaces
│   ├── security/        # JWT Authentication Filter & Google token verification utilities
│   ├── service/         # Business services containing logical execution
│   └── util/            # PDF generation, calculations
├── src/main/resources/
│   ├── static/          # Bundled frontend static assets (HTML, CSS, JS, Pages)
│   ├── application.properties       # Global project configuration properties
│   └── application-local.properties # Local environment override secrets (gitignored)
├── pom.xml              # Maven dependency manager configuration
└── README.md            # Comprehensive project details
```

---

## ⚙️ Quick Start (Local Run)

### 1. Prerequisites
* **Java Development Kit (JDK) 21** or higher
* **PostgreSQL database** (Local instance or [Supabase](https://supabase.com) remote connection)
* **Google Cloud Console Credentials** (Optional, required for Google Sign-In)

### 2. Configure Local Database and Secrets
Create an `application-local.properties` file in `src/main/resources/` (if not already present) to override default configurations securely:

```properties
# Supabase PostgreSQL connection details (Session Pooler recommended for IPv4 compatibility)
spring.datasource.url=jdbc:postgresql://<YOUR_SUPABASE_POOLER_HOST>:5432/postgres?sslmode=require
spring.datasource.username=<YOUR_SUPABASE_USERNAME>
spring.datasource.password=<YOUR_SUPABASE_PASSWORD>

# Google OAuth Client Credentials
google.client.id=<YOUR_GOOGLE_CLIENT_ID>
```

### 3. Build and Package
Run the Maven build script to compile the application and bundle the static assets inside the executable JAR:

```bash
./mvnw clean package -DskipTests
```

### 4. Run the Application
Launch the unified server locally:

```bash
java -jar target/jewellery-billing-0.0.1-SNAPSHOT.jar
```
The server will boot up on **`http://localhost:8080`**.

### 5. Access and Registration
Open `http://localhost:8080/` in your browser. 
> [!IMPORTANT]
> **No Default Credentials**: To ensure production security, there are no default hardcoded credentials (like `admin/admin123`). On the first run, click **"Don't have an account? Create one"** at the bottom of the login screen to register your admin user. Once registered, you will be automatically redirected to the dashboard.

---

## 📈 Invoice Calculations

Invoices are dynamically calculated and stored using the following arithmetic rules:

$$\text{Base Amount} = \text{Product Weight (g)} \times \text{Live Rate per Gram}$$
$$\text{Subtotal} = \text{Base Amount} + \text{Making Charges}$$
$$\text{GST Amount (CGST 1.5\% + SGST 1.5\%)} = \text{Subtotal} \times 3.0\%$$
$$\text{Grand Total} = \text{Subtotal} + \text{GST Amount}$$

---

## 🔒 Security Practices

1. **Password Encryption**: All passwords registered are encrypted using the cryptographic hashing function `BCrypt` prior to database persistence.
2. **Stateless API Authorization**: Endpoints residing under `/api/` (except public health check and auth requests) require a valid token passed inside the `Authorization: Bearer <TOKEN>` header.
3. **Google Token Verification**: Google Sign-In tokens are verified cryptographically on the backend using Google's public key provider to guarantee security.
