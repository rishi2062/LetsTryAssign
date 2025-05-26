package com.fooddelivery.repository

import com.fooddelivery.domain.MenuItem
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MenuItemRepository : MongoRepository<MenuItem, ObjectId> {
    fun findByRestaurantId(restaurantId: ObjectId): List<MenuItem>
} 