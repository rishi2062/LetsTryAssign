package com.fooddelivery.service

import com.fooddelivery.domain.MenuItem
import com.fooddelivery.domain.Restaurant
import com.fooddelivery.dto.*
import com.fooddelivery.repository.MenuItemRepository
import com.fooddelivery.repository.RestaurantRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.data.mongodb.core.MongoTemplate
import java.util.NoSuchElementException

@Service
class RestaurantService(
    private val restaurantRepository: RestaurantRepository,
    private val menuItemRepository: MenuItemRepository,
    private val mongoTemplate: MongoTemplate
) {
    fun getAllRestaurants(): List<RestaurantDto> =
        restaurantRepository.findAll().map { it.toDto() }

    fun getRestaurantById(id: String): RestaurantDto =
        restaurantRepository.findById(ObjectId(id))
            .orElseThrow { NoSuchElementException("Restaurant not found with id: $id") }
            .toDto()

    @Transactional
    fun createRestaurant(request: CreateRestaurantRequest): RestaurantDto =
        Restaurant(
            name = request.name,
            address = request.address
        ).let { restaurantRepository.save(it) }
            .toDto()

    fun getRestaurantMenu(restaurantId: String): List<MenuItemDto> =
        menuItemRepository.findByRestaurantId(ObjectId(restaurantId))
            .map { it.toDto() }

    @Transactional
    fun upsertMenuItem(restaurantId: String, request: UpsertMenuItemRequest): MenuItemDto {
        val restMongoId = ObjectId(restaurantId)
        if (!restaurantRepository.existsById(restMongoId)) {
            throw NoSuchElementException("Restaurant not found with id: $restaurantId")
        }

        request.id?.let{ menuItemId ->
            val menuItem = menuItemRepository.findById(ObjectId(menuItemId))
                .orElseThrow { NoSuchElementException("Menu item not found with id: $menuItemId") }

            return menuItem.copy(
                name = request.name,
                price = request.price
            ).let { menuItemRepository.save(it) }
                .toDto()
        }.run {
            return MenuItem(
                name = request.name,
                price = request.price,
                restaurantId = restMongoId
            ).let { menuItemRepository.save(it) }
                .toDto()
        }

    }

    @Transactional
    fun deleteMenuItem(menuItemId: String) {
        val menuItemMongoId = ObjectId(menuItemId)
        if (!menuItemRepository.existsById(menuItemMongoId)) {
            throw NoSuchElementException("Menu item not found with id: $menuItemId")
        }
        menuItemRepository.deleteById(menuItemMongoId)
    }

} 