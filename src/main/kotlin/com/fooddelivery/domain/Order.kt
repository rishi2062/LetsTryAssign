package com.fooddelivery.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.index.Indexed
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.bson.types.ObjectId
import java.math.BigDecimal
import java.time.Instant

enum class OrderStatus {
    PENDING,
    ACCEPTED,
    PREPARING,
    READY,
    REJECTED
}

@Document(collection = "orders")
data class Order(
    @Id
    val id: ObjectId? = ObjectId.get(),

    @Indexed
    @NotBlank
    val customerId: ObjectId,

    @Indexed
    val restaurantId: ObjectId,

    val items: List<OrderItem>,

    @Indexed
    var status: OrderStatus = OrderStatus.PENDING,

    val timestamp: Instant = Instant.now(),

    val totalAmount: BigDecimal
)

data class OrderItem(
    @NotNull
    val menuItemId: ObjectId? = ObjectId.get(),

    @NotNull
    @Positive
    val quantity: Int,

    @NotNull
    @Positive
    val price: BigDecimal
) 