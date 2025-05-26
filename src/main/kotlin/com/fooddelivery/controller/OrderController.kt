package com.fooddelivery.controller

import com.fooddelivery.dto.*
import com.fooddelivery.service.OrderService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
@Tag(name = "Order Management", description = "APIs for managing food orders")
class OrderController(private val orderService: OrderService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place a new order")
    fun createOrder(@Valid @RequestBody request: CreateOrderRequest): OrderDto =
        orderService.createOrder(request)

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get all orders for a customer")
    fun getCustomerOrders(@PathVariable customerId: String): List<OrderDto> =
        orderService.getCustomerOrders(customerId)

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Get all orders for a restaurant")
    fun getRestaurantOrders(@PathVariable restaurantId: String): List<OrderDto> =
        orderService.getRestaurantOrders(restaurantId)

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update order status")
    fun updateOrderStatus(
        @PathVariable id: String,
        @Valid @RequestBody request: UpdateOrderStatusRequest
    ): OrderDto =
        orderService.updateOrderStatus(id, request)
} 