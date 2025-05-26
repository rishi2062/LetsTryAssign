package com.fooddelivery.dto

import com.fooddelivery.domain.Role
import com.fooddelivery.domain.User
import org.springframework.data.mongodb.core.aggregation.MergeOperation.UniqueMergeId.id

data class UserDto(
    val id: String? = null,
    val name: String,
    val email: String,
    val encodedPassword: String,
    val roles: Role
)

data class RegisterRequest(val name: String, val email: String, val password: String)

data class RefreshTokenRequest(val refreshToken: String)