package com.fooddelivery.service

import com.fooddelivery.domain.*
import com.fooddelivery.dto.*
import com.fooddelivery.repository.MenuItemRepository
import com.fooddelivery.repository.OrderRepository
import com.fooddelivery.repository.RestaurantRepository
import org.bson.types.ObjectId
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

        val customerId = ObjectId(request.customerId)
        val restaurantId = ObjectId(request.restaurantId)

        if (!restaurantRepository.existsById(ObjectId(request.restaurantId))) {
            throw NoSuchElementException("Restaurant not found with id: ${request.restaurantId}")
        }

        val orderItems = request.items.map { itemRequest ->
            val menuItem = menuItemRepository.findById(ObjectId(itemRequest.menuItemId))
                .orElseThrow { NoSuchElementException("Menu item not found with id: ${itemRequest.menuItemId}") }

            if (menuItem.restaurantId != restaurantId) {
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
            customerId = customerId,
            restaurantId = restaurantId,
            items = orderItems,
            totalAmount = totalAmount
        )

        return orderRepository.save(order).toDto()
    }

    fun getCustomerOrders(customerId: String): List<OrderDto> =
        orderRepository.findByCustomerId(ObjectId(customerId))
            .map { it.toDto() }

    fun getRestaurantOrders(restaurantId: String): List<OrderDto> =
        orderRepository.findByRestaurantId(ObjectId(restaurantId))
            .map { it.toDto() }

    @Transactional
    fun updateOrderStatus(orderId: String, request: UpdateOrderStatusRequest): OrderDto {
        val order = orderRepository.findById(ObjectId(orderId))
            .orElseThrow { NoSuchElementException("Order not found with id: $orderId") }

        order.status = request.status
        return orderRepository.save(order).toDto()
    }

} 