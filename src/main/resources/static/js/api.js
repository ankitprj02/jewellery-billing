const Api = {
    getToken() {
        return localStorage.getItem('token');
    },

    getHeaders() {
        const headers = { 'Content-Type': 'application/json' };
        const token = this.getToken();
        if (token) headers['Authorization'] = `Bearer ${token}`;
        return headers;
    },

    async request(endpoint, options = {}) {
        const url = `${API_BASE_URL}${endpoint}`;
        const config = {
            headers: this.getHeaders(),
            ...options
        };

        try {
            const response = await fetch(url, config);
            if (response.status === 401) {
                localStorage.clear();
                window.location.href = '/index.html';
                return;
            }

            if (options.raw) return response;

            const data = await response.json();
            if (!response.ok) {
                throw new Error(data.message || 'Request failed');
            }
            return data;
        } catch (error) {
            throw error;
        }
    },

    get(endpoint) { return this.request(endpoint); },
    post(endpoint, body) { return this.request(endpoint, { method: 'POST', body: JSON.stringify(body) }); },
    put(endpoint, body) { return this.request(endpoint, { method: 'PUT', body: JSON.stringify(body) }); },
    delete(endpoint) { return this.request(endpoint, { method: 'DELETE' }); },

    async upload(endpoint, formData) {
        const headers = {};
        const token = this.getToken();
        if (token) headers['Authorization'] = `Bearer ${token}`;
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'POST',
            headers,
            body: formData
        });
        if (response.status === 401) {
            localStorage.clear();
            window.location.href = '/index.html';
            return;
        }
        const data = await response.json();
        if (!response.ok) throw new Error(data.message || 'Upload failed');
        return data;
    },

    async getImageUrl(endpoint) {
        const response = await this.request(endpoint, { raw: true });
        if (!response.ok) return null;
        const blob = await response.blob();
        return window.URL.createObjectURL(blob);
    },

    parseFilenameFromResponse(response, fallback) {
        const disposition = response.headers.get('Content-Disposition');
        if (disposition) {
            const match = disposition.match(/filename="?([^";\n]+)"?/i);
            if (match) return match[1];
        }
        return fallback;
    },

    async downloadPdf(invoiceId, filename) {
        const response = await this.request(`/invoices/${invoiceId}/pdf`, { raw: true });
        const blob = await response.blob();
        const name = filename || this.parseFilenameFromResponse(response, `invoice-${invoiceId}.pdf`);
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = name.endsWith('.pdf') ? name : `${name}.pdf`;
        a.click();
        window.URL.revokeObjectURL(url);
    },

    async openPdfForPrint(invoiceId, filename) {
        const response = await this.request(`/invoices/${invoiceId}/pdf`, { raw: true });
        const blob = await response.blob();
        const name = filename || this.parseFilenameFromResponse(response, `invoice-${invoiceId}.pdf`);
        const pdfName = name.endsWith('.pdf') ? name : `${name}.pdf`;
        const url = window.URL.createObjectURL(blob);
        const win = window.open(url, '_blank');
        if (win) {
            win.onload = () => {
                win.document.title = pdfName;
                win.print();
            };
        }
    }
};
