# CoffeeShop Order Backend

This project is a robust and scalable backend developed with Spring Boot for managing orders in a coffee shop application. The application provides a complete REST API that allows both customers and staff to efficiently manage orders.

The system is designed to cover all the needs of a modern coffee shop business, from order creation to complete tracking. Key features include:

- **Complete Order Management**: From initial creation to tracking status (Pending, Preparing, Ready, Completed, or Cancelled)
- **Enterprise Security**: Spring Security implementation to protect sensitive data and ensure only authorized personnel can modify order status
- **Database Integration**: Spring Data JPA for efficient data management and transactions
- **Automatic Validation**: Spring Validation implementation to ensure data integrity across all operations
- **Modern REST API**: Well-structured RESTful endpoints following API design best practices

The system is optimized for high concurrency and provides excellent performance even under heavy load. It's ideal for coffee shops looking to digitize their ordering process and improve the experience for both customers and staff.

## 🚀 Features

- 🛍️ Create new orders
- 🔄 Track order status (Pending, Preparing, Ready, Completed, Cancelled)
- 🔄 Update order status (Start preparation, Mark as ready, Complete, Cancel)
- 📊 Retrieve orders by ID or list all orders
- 🔒 Secure API endpoints with Spring Security

## 🛠️ Tech Stack

- Spring Boot 3.3.12
- Java 21
- Spring Data JPA
- Spring Security
- Spring Validation
- Maven

## 📦 Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Validation

## 🚀 Getting Started

### Prerequisites

- Java 21 JDK
- Maven 3.8 or higher
- Database (configured in application.properties)

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
```

2. Navigate to the project directory:
```bash
cd coffeeshop-order-backend
```

3. Build the project:
```bash
./mvnw clean install
```

### Running the Application

You can run the application using Maven:

```bash
./mvnw spring-boot:run
```

Or using Java directly:

```bash
java -jar target/coffeeshop-order-backend-0.0.1-SNAPSHOT.jar
```

## 📄 API Documentation

The application provides the following REST endpoints:

- `GET /api/orders` - Retrieve all orders
- `GET /api/orders/{id}` - Retrieve a specific order by ID
- `POST /api/orders` - Create a new order
- `PUT /api/orders/{id}/start` - Start order preparation
- `PUT /api/orders/{id}/ready` - Mark order as ready
- `PUT /api/orders/{id}/complete` - Complete order
- `PUT /api/orders/{id}/cancel` - Cancel order


