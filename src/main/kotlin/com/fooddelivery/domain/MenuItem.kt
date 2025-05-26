package com.fooddelivery.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.index.Indexed
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

@Document(collection = "menu_items")
data class MenuItem(
    @Id
    val id: String? = null,

    @NotBlank
    val name: String,

    @Positive
    val price: BigDecimal,

    @Indexed
    val restaurantId: String
) 