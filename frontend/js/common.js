// Common utilities shared across pages

function checkAuth() {
    if (!localStorage.getItem('token')) {
        window.location.href = '/index.html';
        return false;
    }
    return true;
}

function logout() {
    localStorage.clear();
    window.location.href = '/index.html';
}

function formatCurrency(amount) {
    return '₹' + parseFloat(amount || 0).toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function formatDate(dateStr) {
    if (!dateStr) return '-';
    const d = new Date(dateStr);
    return d.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
}

function showToast(message, type = 'success') {
    const toastEl = document.getElementById('toast');
    if (!toastEl) return;
    const body = toastEl.querySelector('.toast-body');
    toastEl.className = `toast align-items-center text-bg-${type === 'error' ? 'danger' : 'success'} border-0`;
    body.textContent = message;
    const toast = new bootstrap.Toast(toastEl);
    toast.show();
}

function initDarkMode() {
    const isDark = localStorage.getItem('darkMode') === 'true';
    document.documentElement.setAttribute('data-bs-theme', isDark ? 'dark' : 'light');
    const toggle = document.getElementById('darkModeToggle');
    if (toggle) {
        toggle.checked = isDark;
        toggle.addEventListener('change', () => {
            const dark = toggle.checked;
            localStorage.setItem('darkMode', dark);
            document.documentElement.setAttribute('data-bs-theme', dark ? 'dark' : 'light');
        });
    }
}

function setActiveNav(page) {
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.toggle('active', link.dataset.page === page);
    });
}

function getSidebarHTML(activePage) {
    const username = localStorage.getItem('username') || 'Admin';
    return `
    <nav class="sidebar d-flex flex-column flex-shrink-0 p-3">
        <a href="/pages/dashboard.html" class="d-flex align-items-center mb-3 mb-md-4 text-decoration-none brand-link">
            <i class="bi bi-gem fs-4 me-2 text-warning"></i>
            <span class="fs-5 fw-bold">JewelBill</span>
        </a>
        <hr>
        <ul class="nav nav-pills flex-column mb-auto gap-1">
            <li><a href="/pages/dashboard.html" class="nav-link ${activePage === 'dashboard' ? 'active' : ''}" data-page="dashboard"><i class="bi bi-speedometer2 me-2"></i>Dashboard</a></li>
            <li><a href="/pages/customers.html" class="nav-link ${activePage === 'customers' ? 'active' : ''}" data-page="customers"><i class="bi bi-people me-2"></i>Customers</a></li>
            <li><a href="/pages/products.html" class="nav-link ${activePage === 'products' ? 'active' : ''}" data-page="products"><i class="bi bi-box-seam me-2"></i>Products</a></li>
            <li><a href="/pages/create-invoice.html" class="nav-link ${activePage === 'create-invoice' ? 'active' : ''}" data-page="create-invoice"><i class="bi bi-receipt me-2"></i>Create Invoice</a></li>
            <li><a href="/pages/invoice-history.html" class="nav-link ${activePage === 'invoice-history' ? 'active' : ''}" data-page="invoice-history"><i class="bi bi-clock-history me-2"></i>Invoice History</a></li>
            <li><a href="/pages/settings.html" class="nav-link ${activePage === 'settings' ? 'active' : ''}" data-page="settings"><i class="bi bi-gear me-2"></i>Settings</a></li>
        </ul>
        <hr>
        <div class="d-flex align-items-center justify-content-between">
            <span class="small text-muted"><i class="bi bi-person-circle me-1"></i>${username}</span>
            <button class="btn btn-sm btn-outline-danger" onclick="logout()"><i class="bi bi-box-arrow-right"></i></button>
        </div>
        <div class="form-check form-switch mt-3">
            <input class="form-check-input" type="checkbox" id="darkModeToggle">
            <label class="form-check-label small" for="darkModeToggle">Dark Mode</label>
        </div>
    </nav>`;
}

function getLayoutHTML(activePage, title) {
    return `
    <div class="d-flex">
        ${getSidebarHTML(activePage)}
        <main class="main-content flex-grow-1 p-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="page-title mb-0">${title}</h2>
            </div>
            <div id="pageContent"></div>
        </main>
    </div>
    <div class="toast-container position-fixed bottom-0 end-0 p-3">
        <div id="toast" class="toast" role="alert"><div class="toast-body"></div></div>
    </div>`;
}
