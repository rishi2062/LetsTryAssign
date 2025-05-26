package com.fooddelivery.service

import com.fooddelivery.domain.MenuItem
import com.fooddelivery.domain.Restaurant
import com.fooddelivery.dto.*
import com.fooddelivery.repository.MenuItemRepository
import com.fooddelivery.repository.RestaurantRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
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
        restaurantRepository.findById(id)
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
        menuItemRepository.findByRestaurantId(restaurantId)
            .map { it.toDto() }

    @Transactional
    fun addMenuItem(restaurantId: String, request: CreateMenuItemRequest): MenuItemDto {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw NoSuchElementException("Restaurant not found with id: $restaurantId")
        }

        return MenuItem(
            name = request.name,
            price = request.price,
            restaurantId = restaurantId
        ).let { menuItemRepository.save(it) }
            .toDto()
    }

    @Transactional
    fun updateMenuItem(menuItemId: String, request: UpdateMenuItemRequest): MenuItemDto {
        val menuItem = menuItemRepository.findById(menuItemId)
            .orElseThrow { NoSuchElementException("Menu item not found with id: $menuItemId") }

        return menuItem.copy(
            name = request.name,
            price = request.price
        ).let { menuItemRepository.save(it) }
            .toDto()
    }

    @Transactional
    fun deleteMenuItem(menuItemId: String) {
        if (!menuItemRepository.existsById(menuItemId)) {
            throw NoSuchElementException("Menu item not found with id: $menuItemId")
        }
        menuItemRepository.deleteById(menuItemId)
    }

    private fun Restaurant.toDto() = RestaurantDto(
        id = id,
        name = name,
        address = address
    )

    private fun MenuItem.toDto() = MenuItemDto(
        id = id,
        name = name,
        price = price,
        restaurantId = restaurantId
    )
} 