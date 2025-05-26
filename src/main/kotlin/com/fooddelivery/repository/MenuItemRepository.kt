package com.fooddelivery.repository

import com.fooddelivery.domain.MenuItem
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MenuItemRepository : MongoRepository<MenuItem, String> {
    fun findByRestaurantId(restaurantId: String): List<MenuItem>
} 