package com.fooddelivery.domain

import jakarta.validation.constraints.NotBlank
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

enum class Role {
    CUSTOMER , RESTAURANT
}

@Document(collection = "users")
data class User(
    @Id val id : ObjectId = ObjectId(),

    @NotBlank
    val name: String,
    @NotBlank
    val email: String,
    @NotBlank
    val encodedPassword: String,
    val role: Role = Role.CUSTOMER
)
