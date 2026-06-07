-- Jewellery Shop Billing & Inventory Management System
-- Database Schema for MySQL

CREATE DATABASE IF NOT EXISTS jewellery_billing;
USE jewellery_billing;

-- Users table (Admin authentication)
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(20)  NOT NULL DEFAULT 'ADMIN',
    enabled     BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Customers table
CREATE TABLE IF NOT EXISTS customers (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    mobile_number   VARCHAR(15)  NOT NULL,
    address         VARCHAR(500),
    created_date    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Products table
CREATE TABLE IF NOT EXISTS products (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name    VARCHAR(150)   NOT NULL,
    category        VARCHAR(50)    NOT NULL,
    current_rate    DECIMAL(12,2)  NOT NULL,
    description     VARCHAR(1000)
);

-- Invoices table
CREATE TABLE IF NOT EXISTS invoices (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_number  VARCHAR(30)    NOT NULL UNIQUE,
    customer_id     BIGINT         NOT NULL,
    invoice_date    DATE           NOT NULL,
    subtotal        DECIMAL(14,2)  NOT NULL,
    gst_amount      DECIMAL(14,2)  NOT NULL,
    grand_total     DECIMAL(14,2)  NOT NULL,
    created_at      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Invoice items table
CREATE TABLE IF NOT EXISTS invoice_items (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_id      BIGINT         NOT NULL,
    item_name       VARCHAR(150)   NOT NULL,
    weight          DECIMAL(10,3)  NOT NULL,
    rate_per_gram   DECIMAL(12,2)  NOT NULL,
    making_charges  DECIMAL(12,2)  NOT NULL DEFAULT 0,
    gst_percentage  DECIMAL(5,2)  NOT NULL,
    item_total      DECIMAL(14,2)  NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE
);

-- Shop settings table
CREATE TABLE IF NOT EXISTS shop_settings (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    shop_name       VARCHAR(150) NOT NULL,
    address         VARCHAR(500),
    phone           VARCHAR(20),
    email           VARCHAR(100),
    gstin               VARCHAR(20),
    signature_text      VARCHAR(200),
    signature_image     LONGBLOB,
    signature_image_type VARCHAR(50)
);

-- Indexes for performance
CREATE INDEX idx_customers_name ON customers(name);
CREATE INDEX idx_customers_mobile ON customers(mobile_number);
CREATE INDEX idx_products_name ON products(product_name);
CREATE INDEX idx_invoices_number ON invoices(invoice_number);
CREATE INDEX idx_invoices_date ON invoices(invoice_date);
CREATE INDEX idx_invoice_items_invoice ON invoice_items(invoice_id);
