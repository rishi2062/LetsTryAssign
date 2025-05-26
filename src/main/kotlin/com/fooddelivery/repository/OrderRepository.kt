package com.fooddelivery.repository

import com.fooddelivery.domain.Order
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : MongoRepository<Order, ObjectId> {
    fun findByCustomerId(customerId: ObjectId): List<Order>
    fun findByRestaurantId(restaurantId: ObjectId): List<Order>
} 