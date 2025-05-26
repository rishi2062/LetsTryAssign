package com.fooddelivery.service

import com.fooddelivery.domain.*
import com.fooddelivery.dto.*
import com.fooddelivery.repository.MenuItemRepository
import com.fooddelivery.repository.OrderRepository
import com.fooddelivery.repository.RestaurantRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException
import java.math.BigDecimal

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val restaurantRepository: RestaurantRepository,
    private val menuItemRepository: MenuItemRepository
) {
    @Transactional
    fun createOrder(request: CreateOrderRequest): OrderDto {
        if (!restaurantRepository.existsById(request.restaurantId)) {
            throw NoSuchElementException("Restaurant not found with id: ${request.restaurantId}")
        }

        val orderItems = request.items.map { itemRequest ->
            val menuItem = menuItemRepository.findById(itemRequest.menuItemId)
                .orElseThrow { NoSuchElementException("Menu item not found with id: ${itemRequest.menuItemId}") }

            if (menuItem.restaurantId != request.restaurantId) {
                throw IllegalArgumentException("Menu item ${menuItem.id} does not belong to restaurant ${request.restaurantId}")
            }

            OrderItem(
                menuItemId = menuItem.id!!,
                quantity = itemRequest.quantity,
                price = menuItem.price
            )
        }

        val totalAmount = orderItems.sumOf { it.price.multiply(BigDecimal(it.quantity)) }

        val order = Order(
            customerId = request.customerId,
            restaurantId = request.restaurantId,
            items = orderItems,
            totalAmount = totalAmount
        )

        return orderRepository.save(order).toDto()
    }

    fun getCustomerOrders(customerId: String): List<OrderDto> =
        orderRepository.findByCustomerId(customerId)
            .map { it.toDto() }

    fun getRestaurantOrders(restaurantId: String): List<OrderDto> =
        orderRepository.findByRestaurantId(restaurantId)
            .map { it.toDto() }

    @Transactional
    fun updateOrderStatus(orderId: String, request: UpdateOrderStatusRequest): OrderDto {
        val order = orderRepository.findById(orderId)
            .orElseThrow { NoSuchElementException("Order not found with id: $orderId") }

        order.status = request.status
        return orderRepository.save(order).toDto()
    }

    private fun Order.toDto() = OrderDto(
        id = id,
        customerId = customerId,
        restaurantId = restaurantId,
        items = items.map { it.toDto() },
        status = status,
        timestamp = timestamp,
        totalAmount = totalAmount
    )

    private fun OrderItem.toDto() = OrderItemDto(
        menuItemId = menuItemId,
        quantity = quantity,
        price = price
    )
} 