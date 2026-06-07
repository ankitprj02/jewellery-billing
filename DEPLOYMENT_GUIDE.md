# Deployment Guide — JewelBill

This guide walks you through deploying **JewelBill** to the cloud using **Render** (Docker Web Service) with **Supabase PostgreSQL** as the database and **Google OAuth 2.0** for authentication.

🌐 **Live Application**: [https://jewellery-billing.onrender.com](https://jewellery-billing.onrender.com)

---

## 🏗️ Architecture Overview

```
┌──────────────────────────────────────────┐
│             Render (Docker)               │
│   ┌──────────────────────────────────┐   │
│   │     Spring Boot (Port 8080)       │   │
│   │  - REST API endpoints (/api/**)   │   │
│   │  - Serves static frontend files   │   │
│   └──────────────┬───────────────────┘   │
└──────────────────┼───────────────────────┘
                   │ JDBC (SSL)
         ┌─────────▼──────────┐
         │  Supabase Postgres  │
         │  (Session Pooler)   │
         │  ap-south-1 Mumbai  │
         └────────────────────┘
```

- **Single service** — frontend (HTML/CSS/JS) is bundled inside the Spring Boot JAR and served from the same origin.
- **No separate frontend hosting** needed (no Vercel, no Netlify).
- **Docker-based** deployment — runs on `eclipse-temurin:21-jre`.

---

## Step 1 — Supabase Database Setup

1. Create a free account at [supabase.com](https://supabase.com).
2. Click **New Project** → give it a name (e.g., `jewelbill-db`) → choose your region → set a strong password.
3. Wait for the project to provision (~2 minutes).
4. Navigate to **Project Settings → Database → Connection Pooling**.
5. Set Pool Mode to **Session** (required for Spring Boot / Hibernate DDL operations).
6. Copy the **JDBC connection string** — it looks like:
   ```
   jdbc:postgresql://aws-0-ap-south-1.pooler.supabase.com:5432/postgres?sslmode=require
   ```
7. Note your **pooler username** (format: `postgres.<your-project-ref>`) and **database password**.

> **Important**: Tables are auto-created by Hibernate (`spring.jpa.hibernate.ddl-auto=update`) on first application startup. No manual SQL migration is needed.

---

## Step 2 — Google OAuth 2.0 Setup

To enable **Google Sign-In**:

1. Go to [Google Cloud Console](https://console.cloud.google.com/) → **APIs & Services → Credentials**.
2. Click **Create Credentials → OAuth client ID**.
3. Select Application Type: **Web application**.
4. Add the following **Authorized JavaScript origins**:
   ```
   http://localhost:8080
   https://jewellery-billing.onrender.com
   ```
5. Add the following **Authorized redirect URIs**:
   ```
   http://localhost:8080/login/oauth2/code/google
   https://jewellery-billing.onrender.com/login/oauth2/code/google
   ```
6. Click **Create** and copy the generated **Client ID**.

> When deploying to a new domain/URL, you must add it to both the JavaScript origins and redirect URIs in the Google Cloud Console. Failure to do so results in `Error 400: origin_mismatch`.

---

## Step 3 — Render Docker Deployment

JewelBill uses a **Docker-based deployment** on Render (since Render's native Java runtime is not available on all plans).

### A. Push to GitHub
Ensure your project is pushed to a GitHub repository:
```bash
git add .
git commit -m "deploy: production-ready build"
git push origin main
```

### B. Create a Web Service on Render
1. Log into [render.com](https://render.com) → click **New → Web Service**.
2. Connect your GitHub account and select the `jewellery-billing` repository.
3. Configure the service:
   - **Name**: `jewellery-billing`
   - **Environment**: `Docker`
   - **Branch**: `main`
   - **Dockerfile Path**: `./Dockerfile` (auto-detected)
4. Click **Advanced** and add the following **Environment Variables**:

| Variable | Value | Notes |
|---|---|---|
| `DATABASE_URL` | `jdbc:postgresql://aws-0-ap-south-1.pooler.supabase.com:5432/postgres?sslmode=require` | Get exact URL from Supabase dashboard |
| `DB_USERNAME` | `postgres.<your-project-ref>` | From Supabase pooler settings |
| `DB_PASSWORD` | `<your-database-password>` | Set when creating the Supabase project |
| `JWT_SECRET` | A random 64+ character string | Used to sign JWT tokens |
| `GOOGLE_CLIENT_ID` | `<your-google-oauth-client-id>` | From Google Cloud Console |
| `PORT` | `8080` | Must match `EXPOSE` in Dockerfile |

> **Tip**: To generate a secure JWT secret, run: `openssl rand -base64 64`

5. Click **Create Web Service** — Render will build the Docker image and deploy it.
6. Once deployed, your app will be accessible at `https://<your-service-name>.onrender.com`.

### C. Verify the Deployment
After the build completes (usually 5–10 minutes):
- Visit your Render URL — you should see the JewelBill login page.
- Check the **Logs** tab on Render for any startup errors.
- Look for the line: `Tomcat started on port(s): 8080` — this confirms successful startup.

---

## Step 4 — Local Development Setup

To run the application on your local machine:

### A. Prerequisites
- Java 21+ installed (`java -version`)
- Maven (or use the included `./mvnw` wrapper)
- A Supabase project or local PostgreSQL instance

### B. Configure Local Credentials
Create the file `src/main/resources/application-local.properties` (this is gitignored — never commit it):

```properties
# Supabase PostgreSQL connection (Session Pooler — recommended)
spring.datasource.url=jdbc:postgresql://<POOLER_HOST>:5432/postgres?sslmode=require
spring.datasource.username=postgres.<YOUR_PROJECT_REF>
spring.datasource.password=<YOUR_DATABASE_PASSWORD>

# Optional: Google OAuth Client ID
google.client.id=<YOUR_GOOGLE_CLIENT_ID>
```

### C. Build and Run
```bash
# Package the application (skips tests for speed)
./mvnw clean package -DskipTests

# Run the JAR
java -jar target/jewellery-billing-0.0.1-SNAPSHOT.jar
```

Open **[http://localhost:8080](http://localhost:8080)** in your browser.

### D. First Login
Click **"Don't have an account? Create one"** on the login page to register your first admin account. Once registered, you'll be redirected to the dashboard automatically.

---

## Step 5 — Dockerfile Reference

The included `Dockerfile` uses a **multi-stage build** to keep the final image small:

```dockerfile
# Build stage — compiles and packages the JAR
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# Run stage — lightweight JRE image
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/jewellery-billing-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 🔧 Troubleshooting

| Issue | Cause | Fix |
|---|---|---|
| `502 Bad Gateway` | App not started yet / crashed | Check **Logs** tab on Render for stack trace |
| `ENOTFOUND tenant/user not found` | Wrong `DATABASE_URL` hostname | Get exact JDBC URL from Supabase Dashboard → Settings → Database |
| `PSQLException: password authentication failed` | Wrong `DB_PASSWORD` | Re-enter the password from Supabase settings |
| `Error 400: origin_mismatch` | Render URL not added to Google OAuth | Add the Render URL to Authorized JavaScript origins in Google Cloud Console |
| App slow on first load | Render free tier spin-down | Free tier spins down after inactivity; first request may take 30–60s |
| Tables not created | Hibernate DDL not running | Ensure `spring.jpa.hibernate.ddl-auto=update` in `application.properties` |

---

## 🔒 Security Checklist Before Going Live

- [ ] `application-local.properties` is in `.gitignore` (never committed)
- [ ] `DB_PASSWORD` and `JWT_SECRET` set as environment variables on Render (not hardcoded)
- [ ] Google OAuth client restricted to your production domain only
- [ ] HTTPS enforced (Render provides SSL by default)
- [ ] Supabase connection uses `sslmode=require`

---

## 📞 Support

If you encounter issues not covered here, check:
- [Render Deploy Troubleshooting](https://render.com/docs/troubleshooting-deploys)
- [Supabase Connection Pooler Docs](https://supabase.com/docs/guides/database/connecting-to-postgres#connection-pooler)
- [Google OAuth Setup Guide](https://developers.google.com/identity/protocols/oauth2)
