package com.fooddelivery.dto

import com.fooddelivery.domain.MenuItem
import com.fooddelivery.domain.Order
import com.fooddelivery.domain.OrderItem
import com.fooddelivery.domain.Restaurant
import com.fooddelivery.domain.User


// User Mapper
fun User.toDto() = UserDto(id = id?.toHexString(), name, email, encodedPassword, role)


//Restaurant Mapper
 fun Restaurant.toDto() = RestaurantDto(
    id = id!!.toHexString(),
    name = name,
    address = address
)

 fun MenuItem.toDto() = MenuItemDto(
    id = id!!.toHexString(),
    name = name,
    price = price,
    restaurantId = restaurantId.toHexString()
)

// Order Mapper
 fun Order.toDto() = OrderDto(
    id = id!!.toHexString(),
    customerId = customerId.toHexString(),
    restaurantId = restaurantId.toHexString(),
    items = items.map { it.toDto() },
    status = status,
    timestamp = timestamp,
    totalAmount = totalAmount
)

 fun OrderItem.toDto() = OrderItemDto(
    menuItemId = menuItemId,
    quantity = quantity,
    price = price
)
