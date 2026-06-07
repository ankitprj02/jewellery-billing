# Entity Relationship Diagram (ERD)

## Jewellery Shop Billing & Inventory Management System

```mermaid
erDiagram
    USERS {
        bigint id PK
        varchar username UK
        varchar password
        varchar role
        boolean enabled
    }

    CUSTOMERS {
        bigint id PK
        varchar name
        varchar mobile_number
        varchar address
        datetime created_date
    }

    PRODUCTS {
        bigint id PK
        varchar product_name
        varchar category
        decimal current_rate
        varchar description
    }

    INVOICES {
        bigint id PK
        varchar invoice_number UK
        bigint customer_id FK
        date invoice_date
        decimal subtotal
        decimal gst_amount
        decimal grand_total
        datetime created_at
    }

    INVOICE_ITEMS {
        bigint id PK
        bigint invoice_id FK
        varchar item_name
        decimal weight
        decimal rate_per_gram
        decimal making_charges
        decimal gst_percentage
        decimal item_total
    }

    SHOP_SETTINGS {
        bigint id PK
        varchar shop_name
        varchar address
        varchar phone
        varchar email
        varchar gstin
        varchar signature_text
    }

    CUSTOMERS ||--o{ INVOICES : "places"
    INVOICES ||--|{ INVOICE_ITEMS : "contains"
```

## Relationships

| Relationship | Type | Description |
|-------------|------|-------------|
| Customer → Invoice | One-to-Many | A customer can have multiple invoices |
| Invoice → InvoiceItem | One-to-Many | An invoice contains multiple line items |
| User | Standalone | Admin authentication (no direct FK to other entities) |
| Product | Standalone | Inventory catalog (referenced by name in invoice items) |
| ShopSettings | Standalone | Shop details for PDF invoices |

## Cardinality

- **1 Customer : N Invoices** — Each invoice belongs to exactly one customer
- **1 Invoice : N InvoiceItems** — Each item belongs to exactly one invoice
- **Cascade**: Deleting an invoice deletes its items (orphanRemoval)
