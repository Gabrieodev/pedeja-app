# App_PedeJa

Academic project developed for mobile programming course inspired by delivery apps.

## 📱 Overview

PedeJá simulates a basic food delivery system, allowing management of:

- Product categories  
- Products  
- Customers  
- Orders  
- Order items  

The goal is to represent a real-world delivery flow using a structured relational database.

## 🗄️ Database Diagram

```mermaid
erDiagram
    CATEGORIA {
        INTEGER id PK
        TEXT nome
    }

    PRODUTO {
        INTEGER id PK
        TEXT nome
        REAL preco
        INTEGER categoria_id FK
    }

    CLIENTE {
        INTEGER id PK
        TEXT nome
        TEXT email
        TEXT endereco
    }

    PEDIDO {
        INTEGER id PK
        INTEGER cliente_id FK
        REAL total
        TEXT data
        TEXT status
    }

    ITEM_PEDIDO {
        INTEGER id PK
        INTEGER pedido_id FK
        INTEGER produto_id FK
        INTEGER quantidade
        REAL subtotal
    }

    CATEGORIA ||--o{ PRODUTO : has
    CLIENTE ||--o{ PEDIDO : places
    PEDIDO ||--o{ ITEM_PEDIDO : contains
    PRODUTO ||--o{ ITEM_PEDIDO : included_in
```
