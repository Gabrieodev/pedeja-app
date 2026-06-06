<div align="center">

# PedeJá — Android Client

**Native Android frontend for the PedeJá food delivery API — category browsing, order creation and real-time item management.**

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org/)
[![Material Design](https://img.shields.io/badge/Material_Design-757575?style=for-the-badge&logo=materialdesign&logoColor=white)](https://m3.material.io/)

</div>

---

## Overview

Native Android client for the **PedeJá REST API**, developed for the Mobile Programming course. The application covers the full delivery interaction flow — browsing categories and products, creating orders, adding items with quantity control, and tracking order status — all consuming the Spring Boot backend over HTTP.

Backend repository: [pedeja-api](https://github.com/gabrieodev/pedeja-api)

---

## Architecture

```text
Android App (Java / XML)
         ↓
   HTTP Requests
         ↓
PedeJá REST API (Spring Boot)
         ↓
    SQLite Database
```

The mobile layer is purely a client — all business logic, total calculation and persistence live in the backend. The Android app is responsible for presentation, user input and HTTP communication only.

---

## Features

| Module | Functionality |
|---|---|
| Categories | Browse available product categories |
| Products | List products, view details — filtered by category |
| Customers | Customer registration |
| Orders | Create orders, view history, track status |
| Order Items | Add products to orders, quantity management, automatic subtotal display |
| API Integration | Consume all backend REST endpoints with real-time data sync |

---

## Screens

- Home
- Customer registration
- Product listing
- Order creation
- Order item management

> Screenshots to be added.

---

## Tech Stack

| Technology | Role |
|---|---|
| Java | Application language |
| XML | UI layout definitions |
| Android Studio | IDE and emulator |
| Gradle | Build tool and dependency management |
| HTTP client | REST API consumption |
| Material Design Components | UI component library |

---

## Backend API Endpoints

| Endpoint | Resource |
|---|---|
| `/cliente` | Customers |
| `/categoria` | Categories |
| `/produto` | Products |
| `/pedido` | Orders |
| `/item-pedido` | Order items |

Full endpoint documentation available in the [PedeJá API Swagger UI](https://github.com/gabrieodev/pedeja-api).

---

## Architectural Decisions

**Strict client-server separation** — The Android app holds no local business logic. Order totals, subtotal calculations and data validation are enforced by the backend API. The mobile layer handles only presentation and user interaction, keeping both layers independently maintainable and testable.

**Material Design Components** — Google's Material Design library was adopted from the start to ensure UI consistency, accessibility compliance and familiar interaction patterns for Android users — without building custom components from scratch.

**HTTP over a local REST API** — The app communicates with the backend via standard HTTP requests rather than a database SDK or embedded storage. This reflects a real-world client-server pattern and makes the Android layer interchangeable with any other frontend that speaks the same API contract.

---

## Roadmap

- [x] Category and product browsing
- [x] Order creation and item management
- [x] REST API integration with PedeJá backend
- [x] Material Design UI
- [ ] Retrofit for type-safe HTTP calls
- [ ] MVVM architecture with ViewModel and LiveData
- [ ] Authentication and user login
- [ ] Shopping cart with local state management
- [ ] Push notifications for order status updates
- [ ] Real-time order tracking
- [ ] Dark mode
- [ ] Payment integration

---

## How to Run

### Prerequisites

- Android Studio (latest stable)
- JDK 21
- PedeJá API running locally — see [backend setup](https://github.com/gabrieodev/pedeja-api)

### 1. Clone the repository

```bash
git clone https://github.com/gabrieodev/app-pedeja.git
```

### 2. Open in Android Studio

Open the project folder in Android Studio and let Gradle sync complete.

### 3. Start the backend

```bash
# In the pedeja-api directory
mvn spring-boot:run
```

### 4. Configure the API base URL

Update the base URL in the network configuration file to point to your local backend:

```java
private static final String BASE_URL = "http://10.0.2.2:8080/";
```

> `10.0.2.2` is the Android emulator's alias for `localhost` on the host machine.

### 5. Run the application

Start an Android emulator or connect a physical device and run the app from Android Studio.

---

## License

This project is licensed under the [MIT License](LICENSE).

---

<div align="center">

**PedeJá Android** — Native delivery client. Java · Android · Material Design

See also: [PedeJá API — Backend](https://github.com/gabrieodev/pedeja-api)

</div>
