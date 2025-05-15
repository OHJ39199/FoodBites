# Truck Bites API

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)
![Java](https://img.shields.io/badge/java-17-orange)
![Spring Boot](https://img.shields.io/badge/spring%20boot-3.3.5-green)

**Author**: Andrés Caso Iglesias  
**Date**: May 2025

The **Truck Bites API** is a RESTful web service built with Spring Boot and Java, designed to manage food trucks, menus, orders, users, locations, and notifications for the Truck Bites platform. It uses a MySQL database with the `proyectoad` schema and provides endpoints for CRUD operations, location-based searches, and order analytics. The API supports JSON payloads and is tested with curl commands for both functionality and error handling.

This project serves as the backend for a food truck management application, accessible locally at `http://localhost:8080/api`.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [API Endpoints](#api-endpoints)
- [Testing the API](#testing-the-api)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Extending the Project](#extending-the-project)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features

- **Food Trucks**: Create, read, update, and delete food trucks; search by location or cuisine; retrieve top trucks by order count.
- **Menus**: Manage menu items, including bulk creation and filtering by food truck.
- **Orders**: Handle order creation, updates, and deletions; retrieve pending orders or total sales.
- **Users**: Manage user accounts with email-based lookups.
- **Locations**: Track food truck locations by city and street.
- **Notifications**: Send and manage user notifications.
- **Error Handling**: Returns standard HTTP status codes (200, 400, 404, 500) with JSON error messages.

## Prerequisites

- **Java**: 17 or higher
- **Maven**: 3.6.x or higher
- **MySQL**: 8.x
- **curl**: For testing API endpoints (or use Postman)
- **Git**: To clone the repository
- **IDE**: Optional (e.g., IntelliJ IDEA, Eclipse)

## Setup Instructions

### 1. Clone the Repository
Clone the project from GitHub:

```bash
git clone https://github.com/your-username/truck-bites-api.git
cd truck-bites-api
```

### 2. Configure the Database
1. Install and start MySQL.
2. Create a database named `proyectoad`:
   ```sql
   CREATE DATABASE proyectoad;
   ```
3. Create the required tables. Below is a sample SQL script for the `foodtrucks` table. Add similar scripts for `menus`, `pedidos`, `usuarios`, `ubicaciones`, and `notificaciones`:
   ```sql
   USE proyectoad;
   CREATE TABLE foodtrucks (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       nombre VARCHAR(255) NOT NULL,
       tipoCocina VARCHAR(255) NOT NULL,
       ubicacionActual VARCHAR(255) NOT NULL
   );
   ```

### 3. Configure Application Properties
Edit `src/main/resources/application.properties` to set your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/proyectoad?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
server.port=8080
```

### 4. Build and Run the Application
Build and start the Spring Boot application:

```bash
mvn clean install
mvn spring-boot:run
```

The API will be available at `http://localhost:8080/api`.

## API Endpoints

Below is a summary of key endpoints for the `FoodTrucks` resource. For a complete list, see the [API Documentation](#api-documentation).

| Method | Endpoint                       | Description                          |
|--------|--------------------------------|--------------------------------------|
| GET    | `/api/foodtrucks`             | Retrieve all food trucks            |
| GET    | `/api/foodtrucks/{id}`        | Retrieve a food truck by ID         |
| POST   | `/api/foodtrucks`             | Create a new food truck             |
| PUT    | `/api/foodtrucks/{id}`        | Update a food truck                 |
| DELETE | `/api/foodtrucks/{id}`        | Delete a food truck                 |
| GET    | `/api/foodtrucks/cerca`       | Find trucks by city and street      |
| GET    | `/api/foodtrucks/recomendar`  | Recommend trucks by city and cuisine|

### Example Request
Create a new food truck:

```bash
curl -X POST http://localhost:8080/api/foodtrucks \
-H "Content-Type: application/json" \
-d '{"nombre":"Pizza Palazzo","tipoCocina":"Italiana","ubicacionActual":"Nueva York, 10th St"}'
```

**Response**:
```json
{
  "id": 7,
  "nombre": "Pizza Palazzo",
  "tipoCocina": "Italiana",
  "ubicacionActual": "Nueva York, 10th St"
}
```

## Testing the API

A test battery is available in `docs/Andres_CasoIglesias_testAPI.docx`, containing curl commands for all endpoints, including success and error cases. Example test for invalid input:

```bash
curl -X POST http://localhost:8080/api/foodtrucks \
-H "Content-Type: application/json" \
-d '{"tipoCocina":"Italiana"}'
```

**Expected Response** (400 Bad Request):
```json
{
  "message": "Faltan campos requeridos: nombre, ubicacionActual"
}
```

Run tests in a terminal or import commands into Postman. For verbose output, add `-v` to curl commands:

```bash
curl -v -X GET http://localhost:8080/api/foodtrucks
```

## API Documentation

Detailed documentation is in `docs/Truck_Bites_API_Documentation.docx`, covering all endpoints, request/response formats, parameters, and examples. Key notes:
- **Base URL**: `http://localhost:8080/api`
- **Content Type**: JSON (`Content-Type: application/json`) for POST/PUT requests
- **Date Format**: ISO 8601 (e.g., `2025-05-14T22:00:00`)

## Project Structure

```plaintext
truck-bites-api/
├── src/
│   ├── main/
│   │   ├── java/com/truckbites/api/
│   │   │   ├── controller/    # REST controllers
│   │   │   ├── service/       # Business logic
│   │   │   ├── repository/    # JPA repositories
│   │   │   ├── model/         # Entity classes
│   │   │   └── TruckBitesApiApplication.java
│   │   └── resources/
│   │       └── application.properties
├── docs/
│   ├── Truck_Bites_API_Documentation.docx
│   └── Andres_CasoIglesias_testAPI.docx
├── pom.xml
└── README.md
```

## Extending the Project

To add support for other resources (e.g., `Menus`, `Pedidos`):
1. Create entity classes for tables in the `proyectoad` schema.
2. Define JPA repositories extending `JpaRepository`.
3. Implement service classes with business logic.
4. Add controllers with REST endpoints.

Example: For `Menus`, create `Menu.java`, `MenuRepository.java`, `MenuService.java`, and `MenuController.java`, following the `FoodTruck` pattern.

## Contributing

We welcome contributions! To contribute:
1. Fork the repository.
2. Create a feature branch:
   ```bash
   git checkout -b feature/your-feature
   ```
3. Commit changes:
   ```bash
   git commit -m 'Add your feature'
   ```
4. Push to the branch:
   ```bash
   git push origin feature/your-feature
   ```
5. Open a pull request.

Please include tests and update documentation for new features.

## License

This project is licensed under the [MIT License](LICENSE). See the `LICENSE` file for details.

## Contact

For questions or issues, contact Andrés Caso Iglesias via [GitHub Issues](https://github.com/your-username/truck-bites-api/issues) or email at [your-email@example.com].

---

⭐ If you find this project useful, give it a star on GitHub!