package com.fooddelivery.controller

import com.fooddelivery.dto.*
import com.fooddelivery.service.RestaurantService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/restaurants")
@Tag(name = "Restaurant Management", description = "APIs for managing restaurants and their menus")
class RestaurantController(private val restaurantService: RestaurantService) {

    @GetMapping
    @Operation(summary = "Get all restaurants")
    fun getAllRestaurants(): List<RestaurantDto> =
        restaurantService.getAllRestaurants()

    @GetMapping("/{id}")
    @Operation(summary = "Get restaurant by ID")
    fun getRestaurantById(@PathVariable id: String): RestaurantDto =
        restaurantService.getRestaurantById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new restaurant")
    fun createRestaurant(@Valid @RequestBody request: CreateRestaurantRequest): RestaurantDto =
        restaurantService.createRestaurant(request)

    @GetMapping("/{id}/menu")
    @Operation(summary = "Get restaurant menu")
    fun getRestaurantMenu(@PathVariable id: String): List<MenuItemDto> =
        restaurantService.getRestaurantMenu(id)

    @PostMapping("/{id}/menu")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new menu item to restaurant")
    fun addMenuItem(
        @PathVariable id: String,
        @Valid @RequestBody request: CreateMenuItemRequest
    ): MenuItemDto =
        restaurantService.addMenuItem(id, request)

    @PutMapping("/menu-items/{id}")
    @Operation(summary = "Update a menu item")
    fun updateMenuItem(
        @PathVariable id: String,
        @Valid @RequestBody request: UpdateMenuItemRequest
    ): MenuItemDto =
        restaurantService.updateMenuItem(id, request)

    @DeleteMapping("/menu-items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a menu item")
    fun deleteMenuItem(@PathVariable id: String) =
        restaurantService.deleteMenuItem(id)
} 