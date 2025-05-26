package com.fooddelivery.dto

import com.fooddelivery.domain.OrderStatus
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.bson.types.ObjectId
import java.math.BigDecimal
import java.time.Instant

data class OrderDto(
    val id: String,
    val customerId: String,
    val restaurantId: String,
    val items: List<OrderItemDto>,
    val status: OrderStatus,
    val timestamp: Instant,
    val totalAmount: BigDecimal
)

data class OrderItemDto(
    val menuItemId: ObjectId?,
    val quantity: Int,
    val price: BigDecimal
)

data class CreateOrderRequest(
    @field:NotBlank(message = "Customer ID is required")
    val customerId: String?,
    
    @field:NotNull(message = "Restaurant ID is required")
    val restaurantId: String?,
    
    @field:NotEmpty(message = "Order must contain at least one item")
    @field:Valid
    val items: List<OrderItemRequest>
)

data class OrderItemRequest(
    @field:NotNull(message = "Menu item ID is required")
    val menuItemId: String?,
    
    @field:Positive(message = "Quantity must be positive")
    val quantity: Int
)

data class UpdateOrderStatusRequest(
    @field:NotNull(message = "Order status is required")
    val status: OrderStatus
) 