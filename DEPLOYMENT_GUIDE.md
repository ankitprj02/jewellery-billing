# Unified Deployment Guide: JewelBill

This guide provides instructions for deploying the **JewelBill Jewellery Billing System** as a unified single-origin application. The frontend static files are served directly by the Spring Boot server on port `8080`, and the application connects to a cloud-hosted **Supabase PostgreSQL** database.

---

## 💻 System Architecture

* **Unified Server**: Single Web Service hosting both API endpoints and the Bootstrap frontend.
* **Database**: PostgreSQL 17 (hosted on Supabase).
* **Identity Provider**: Google Sign-In (OAuth 2.0) and local credential database.

---

## 🛠️ 1. Database Setup (Supabase)

1. Create a free account at [Supabase](https://supabase.com).
2. Create a new project (e.g., `jewelbill-db`). Note the database region (e.g., Mumbai, `ap-south-1`).
3. Navigate to **Project Settings → Database → Connection Pooler**.
4. Set the pool mode to **Session** (highly recommended for transactional Java applications).
5. Copy the connection string. It will look like this:
   ```text
   jdbc:postgresql://<POOLER_HOST>:5432/postgres?sslmode=require
   ```
6. Note your master database password and pooler username (default is usually `postgres.<YOUR_PROJECT_REF>`).

---

## 🔑 2. Google OAuth 2.0 Setup

To enable **Google Sign-In**:
1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
2. Create a new project or select an existing one.
3. Go to **APIs & Services → OAuth consent screen**, select User Type, and fill in the app details.
4. Go to **Credentials → Create Credentials → OAuth client ID**.
5. Select Application Type: **Web application**.
6. Configure the restrictions:
   * **Authorized JavaScript origins**:
     * `http://localhost:8080` (for local development)
     * `https://<YOUR_RENDER_SUBDOMAIN>.onrender.com` (for production)
7. Save and copy the generated **Client ID**.

---

## 🚀 3. Render Web Service Deployment

Since the frontend is bundled inside the Spring Boot JAR, you do not need separate Vercel or Netlify hosting. Simply deploy the backend:

1. Push your consolidated project to **GitHub**.
2. Log into [Render](https://render.com).
3. Click **New → Web Service** and authorize access to your repository.
4. Configure the service:
   * **Environment/Language**: `Java`
   * **Build Command**: `./mvnw clean package -DskipTests`
   * **Start Command**: `java -jar target/jewellery-billing-0.0.1-SNAPSHOT.jar`
5. Click **Advanced** and add the following **Environment Variables**:

| Environment Variable | Description / Recommended Value |
|---|---|
| `DATABASE_URL` | Supabase Pooler JDBC URL (e.g., `jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:5432/postgres?sslmode=require`) |
| `DB_USERNAME` | Supabase pooler username (e.g., `postgres.zdklwkizbzamehwgojvg`) |
| `DB_PASSWORD` | Supabase project database password |
| `JWT_SECRET` | A secure, random string at least 256 bits long for JWT signatures |
| `GOOGLE_CLIENT_ID` | Your Google OAuth Web Client ID |

6. Click **Create Web Service** to trigger the build. Render will auto-assign a public URL for your application (e.g., `https://jewelbill.onrender.com`).

---

## 🏠 4. Local Development Run

To run the application locally on your computer:

### Step A: Configure Local Credentials
Create a file named `src/main/resources/application-local.properties` (this file is gitignored to keep credentials safe) and populate it with your local/remote database keys:
```properties
spring.datasource.url=jdbc:postgresql://<POOLER_HOST>:5432/postgres?sslmode=require
spring.datasource.username=postgres.<PROJECT_REF>
spring.datasource.password=<YOUR_DATABASE_PASSWORD>
```

### Step B: Build and Run
```bash
# Clean and package the JAR
./mvnw clean package -DskipTests

# Run the packaged executable JAR
java -jar target/jewellery-billing-0.0.1-SNAPSHOT.jar
```
Open **`http://localhost:8080/`** in your browser.

---

## 🔒 5. User Registration & Security Warning

> [!WARNING]
> **No Default Credentials**: To prevent security vulnerabilities in production, the default credentials (`admin` / `admin123`) have been disabled. 
> 
> * **Creating an Admin Account**: On your first launch (either locally or in production), navigate to the login screen and click **"Don't have an account? Create one"**. Enter your desired admin username and secure password, then click **Register**.
> * **Subsequent Access**: For subsequent accesses, sign in using the credentials you registered or link your verified Google account.
