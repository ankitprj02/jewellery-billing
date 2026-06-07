// API base URL - update for production (Render backend URL)
const API_BASE_URL = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
    ? 'http://localhost:8080/api'
    : (window.API_URL || 'https://your-backend.onrender.com/api');

// Google OAuth Client ID - Replace with your own client ID from Google Developer Console
const GOOGLE_CLIENT_ID = '616229059191-fim7a642ltg78iik64f1b41qifnokh75.apps.googleusercontent.com';
