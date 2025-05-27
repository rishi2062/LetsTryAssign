# Food Delivery Platform API

A Spring Boot-based REST API for a food delivery platform that supports multiple restaurants. The system allows customers to place food orders and restaurants to manage them.

## Features

- Authentication & Authorization
    - User registration
    - User login with JWT authentication
    - Token refresh mechanism
    - Role-based access control (Customer/Restaurant)
  
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

### Environment Variables

The following environment variables are required:
- `MONGO_DB_LETSTRY_ASSIGNMENT_CONNECTION`: MongoDB connection string
- `JWT_ENCODER_BASE64`: Base64-encoded secret key for JWT signing


## Getting Started

### Note : If you find issue in running locally, try with IntelliJ IDE

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
   docker run -d -p 27017:27017 --name mongodb mongo:7.0
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

#### Authentication

- `POST /auth/register` - Register a new user
- `POST /auth/login` - Login user and get access/refresh tokens
- `POST /auth/refresh` - Refresh access token using refresh token

## Data Models

### User

```json 
{ 
  "id": "507f1f77bcf86cd799439014",
  "name": "John Doe", 
  "email": "john@example.com",
  "role": "CUSTOMER" }
```



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
│   ├── AuthController.kt
│   └── OrderController.kt
├── service/
│   ├── RestaurantService.kt
│   └── OrderService.kt
├── repository/
│   ├── RestaurantRepository.kt
│   ├── MenuItemRepository.kt
│   ├── UserRepository.kt
│   ├── RefreshTokenRepository.kt
│   └── OrderRepository.kt
├── domain/
│   ├── Restaurant.kt
│   ├── User.kt
│   ├── RefreshToken.kt
│   ├── MenuItem.kt
│   └── Order.kt
├── dto/
│   ├── RestaurantDto.kt
│   ├── Mappers.kt
│   ├── UserDto.kt
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



## Security

The API uses JWT (JSON Web Tokens) for authentication and authorization:

- Access tokens expire after 15 minutes
- Refresh tokens are valid for 30 days
- Passwords are encrypted using BCrypt
- Protected endpoints require a valid JWT in the Authorization header
- Token format: `Bearer <token>`

To access protected endpoints:
1. Register a user or login to get tokens
2. Include the access token in the Authorization header
3. Use refresh token to get a new access token when expired

## Postman
https://.postman.co/workspace/My-Workspace~2e565256-8030-42d5-87db-77201bed717f/collection/29786233-4e4f99b6-3406-46ce-8a1a-a5c383c65c4e?action=share&creator=29786233

## Still Pending

- [ ] Role Based Api access
- [ ] Instead of taking customerId , restaurantId based on role , we can make use of userId
- [ ] Valid Error Codes
