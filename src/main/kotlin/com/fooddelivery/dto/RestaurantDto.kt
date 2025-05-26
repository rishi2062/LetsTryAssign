package com.fooddelivery.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class RestaurantDto(
    val id: String?=null,
    val name: String,
    val address: String
)

data class CreateRestaurantRequest(
    @field:NotBlank(message = "Restaurant name is required")
    val name: String,
    
    @field:NotBlank(message = "Restaurant address is required")
    val address: String
)

data class MenuItemDto(
    val id: String? = null,
    val name: String,
    val price: BigDecimal,
    val restaurantId: String
)

data class UpsertMenuItemRequest(

    val id: String? = null,

    @field:NotBlank(message = "Menu item name is required")
    val name: String,
    
    @field:Positive(message = "Price must be positive")
    val price: BigDecimal
)

data class UpdateMenuItemRequest(
    @field:NotBlank(message = "Menu item name is required")
    val name: String,
    
    @field:Positive(message = "Price must be positive")
    val price: BigDecimal
) 