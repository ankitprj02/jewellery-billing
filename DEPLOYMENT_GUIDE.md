# Deployment Guide

Deploy the Jewellery Shop Billing System with:
- **Frontend** → Vercel
- **Backend** → Render
- **Database** → Railway MySQL

---

## 1. Railway MySQL Database

1. Create account at [railway.app](https://railway.app)
2. Create a new project → **Add MySQL**
3. Copy connection details from the **Variables** tab:
   - `MYSQLHOST`, `MYSQLPORT`, `MYSQLUSER`, `MYSQLPASSWORD`, `MYSQLDATABASE`
4. Build JDBC URL:
   ```
   jdbc:mysql://<HOST>:<PORT>/<DATABASE>?useSSL=true&requireSSL=true&serverTimezone=UTC
   ```

---

## 2. Render Backend Deployment

1. Push code to GitHub
2. Create account at [render.com](https://render.com)
3. **New → Web Service** → Connect your repository
4. Configure:
   - **Environment:** Java
   - **Build Command:** `./mvnw clean package -DskipTests`
   - **Start Command:** `java -jar target/jewellery-billing-0.0.1-SNAPSHOT.jar`
   - **Root Directory:** `/` (project root)

5. Set **Environment Variables:**

   | Variable | Value |
   |----------|-------|
   | `DATABASE_URL` | `jdbc:mysql://host:port/db?useSSL=true&serverTimezone=UTC` |
   | `DB_USERNAME` | Railway MySQL user |
   | `DB_PASSWORD` | Railway MySQL password |
   | `JWT_SECRET` | Strong random string (256+ bits) |
   | `CORS_ORIGINS` | Your Vercel frontend URL |
   | `PORT` | `8080` (Render sets this automatically) |

6. Deploy and note your backend URL: `https://your-app.onrender.com`

---

## 3. Vercel Frontend Deployment

1. Create account at [vercel.com](https://vercel.com)
2. **New Project** → Import GitHub repository
3. Set **Root Directory** to `frontend`
4. Framework Preset: **Other** (static site)
5. No build command needed
6. Deploy

7. Update API URL — edit `frontend/js/config.js` or set in `index.html`:
   ```javascript
   window.API_URL = 'https://your-app.onrender.com/api';
   ```

8. Update Render `CORS_ORIGINS` with your Vercel URL:
   ```
   https://your-app.vercel.app
   ```

---

## 4. Local Development

### Prerequisites
- Java 21+
- MySQL 8+
- Maven (or use `./mvnw`)

### Backend
```bash
# Create database
mysql -u root -p -e "CREATE DATABASE jewellery_billing;"

# Run backend
./mvnw spring-boot:run
```

### Frontend
Serve the `frontend` folder with any static server:
```bash
cd frontend
npx serve .
# or use Live Server extension in VS Code
```

Open `http://localhost:3000` (or port shown) and ensure backend runs on `http://localhost:8080`.

### Default Login
- **Username:** admin
- **Password:** admin123

---

## 5. Environment Variables Reference

| Variable | Description | Default |
|----------|-------------|---------|
| `DATABASE_URL` | MySQL JDBC URL | `jdbc:mysql://localhost:3306/jewellery_billing` |
| `DB_USERNAME` | Database username | `root` |
| `DB_PASSWORD` | Database password | empty |
| `JWT_SECRET` | JWT signing key | (dev default in properties) |
| `JWT_EXPIRATION` | Token expiry (ms) | `86400000` (24h) |
| `CORS_ORIGINS` | Allowed frontend origins | localhost URLs |
| `PORT` | Server port | `8080` |

---

## 6. Post-Deployment Checklist

- [ ] Backend health check: `GET /api/health`
- [ ] Login works from frontend
- [ ] CORS configured correctly
- [ ] Database tables auto-created (JPA `ddl-auto=update`)
- [ ] Default admin user created
- [ ] PDF download works
- [ ] Change default admin password in production
