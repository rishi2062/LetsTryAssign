# Food Delivery Platform API

A Spring Boot-based REST API for a food delivery platform that supports multiple restaurants. The system allows customers to place food orders and restaurants to manage them.

## Features

- Restaurant Management
  - Create and retrieve restaurants
  - Manage restaurant menus (add, update, delete menu items)
- Order Management
  - Place new orders
  - View orders by customer or restaurant
  - Update order status

## Tech Stack

- Kotlin
- Spring Boot 3.2.3
- Spring Data MongoDB
- MongoDB
- Gradle
- Docker
- Swagger/OpenAPI for API documentation

## Prerequisites

- JDK 17 or later
- MongoDB 6.0 or later
- Docker (optional, for containerization)

## Getting Started

### Running Locally

1. Clone the repository
2. Start MongoDB:
   ```bash
   mongod --dbpath /path/to/data/directory
   ```
3. Build the project:
   ```bash
   ./gradlew build
   ```
4. Run the application:
   ```bash
   ./gradlew bootRun
   ```

The application will start on `http://localhost:8080/api`

### Running with Docker

1. Start MongoDB:
   ```bash
   docker run -d -p 27017:27017 --name mongodb mongo:6.0
   ```
2. Build the application image:
   ```bash
   docker build -t food-delivery-api .
   ```
3. Run the container:
   ```bash
   docker run -p 8080:8080 --link mongodb:mongodb food-delivery-api
   ```

## API Documentation

Once the application is running, you can access the Swagger UI at:
`http://localhost:8080/api/swagger-ui.html`

### Available Endpoints

#### Restaurant Management

- `GET /api/restaurants` - Get all restaurants
- `GET /api/restaurants/{id}` - Get restaurant by ID
- `POST /api/restaurants` - Create a new restaurant
- `GET /api/restaurants/{id}/menu` - Get restaurant menu
- `POST /api/restaurants/{id}/menu` - Add a new menu item
- `PUT /api/restaurants/menu-items/{id}` - Update a menu item
- `DELETE /api/restaurants/menu-items/{id}` - Delete a menu item

#### Order Management

- `POST /api/orders` - Place a new order
- `GET /api/orders/customer/{customerId}` - Get customer orders
- `GET /api/orders/restaurant/{restaurantId}` - Get restaurant orders
- `PATCH /api/orders/{id}/status` - Update order status

## Data Models

### Restaurant
```json
{
  "id": "507f1f77bcf86cd799439011",
  "name": "Pizza Plaza",
  "address": "123 Main Street"
}
```

### MenuItem
```json
{
  "id": "507f1f77bcf86cd799439012",
  "name": "Margherita Pizza",
  "price": 299,
  "restaurantId": "507f1f77bcf86cd799439011"
}
```

### Order
```json
{
  "id": "507f1f77bcf86cd799439013",
  "customerId": "customer123",
  "restaurantId": "507f1f77bcf86cd799439011",
  "items": [
    {
      "menuItemId": "507f1f77bcf86cd799439012",
      "quantity": 2,
      "price": 299
    }
  ],
  "status": "PENDING",
  "timestamp": "2024-02-20T10:30:00Z",
  "totalAmount": 598
}
```

## Error Handling

The API uses standard HTTP status codes and returns error responses in the following format:

```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": [
    "name: Restaurant name is required",
    "price: Price must be positive"
  ]
}
```

## Development

### Project Structure

```
src/main/kotlin/com/fooddelivery/
├── FoodDeliveryApplication.kt
├── controller/
│   ├── RestaurantController.kt
│   └── OrderController.kt
├── service/
│   ├── RestaurantService.kt
│   └── OrderService.kt
├── repository/
│   ├── RestaurantRepository.kt
│   ├── MenuItemRepository.kt
│   └── OrderRepository.kt
├── domain/
│   ├── Restaurant.kt
│   ├── MenuItem.kt
│   └── Order.kt
├── dto/
│   ├── RestaurantDto.kt
│   └── OrderDto.kt
└── exception/
    └── GlobalExceptionHandler.kt
```

### Database

The application uses MongoDB as its database. MongoDB provides:
- Document-based storage
- Flexible schema
- High performance for read and write operations
- Built-in support for horizontal scaling
- Rich querying capabilities

To connect to MongoDB:
- Host: localhost
- Port: 27017
- Database: fooddelivery

## License

This project is licensed under the MIT License. 