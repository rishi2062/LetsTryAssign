package com.fooddelivery.repository

import com.fooddelivery.domain.Restaurant
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository : MongoRepository<Restaurant, String> 