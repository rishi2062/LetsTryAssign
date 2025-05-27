package com.fooddelivery.dto

import com.fooddelivery.domain.Role
import com.fooddelivery.domain.User
import jakarta.validation.constraints.NotBlank
import org.springframework.data.mongodb.core.aggregation.MergeOperation.UniqueMergeId.id

data class UserDto(
    val id: String? = null,
    val name: String,
    val email: String,
    val encodedPassword: String,
    val roles: Role
)

data class RegisterRequest(
    val name: String? = null,

    @field:NotBlank(message = "Email is required")
    val email: String,

    @field:NotBlank(message = "Password is required")
    val password: String,

    val role: Role = Role.CUSTOMER
)

data class RefreshTokenRequest(val refreshToken: String)