package com.fooddelivery.repository

import com.fooddelivery.domain.Order
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : MongoRepository<Order, String> {
    fun findByCustomerId(customerId: String): List<Order>
    fun findByRestaurantId(restaurantId: String): List<Order>
} 