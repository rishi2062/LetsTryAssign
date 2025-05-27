package com.fooddelivery.controller

import com.fooddelivery.dto.RefreshTokenRequest
import com.fooddelivery.dto.RegisterRequest
import com.fooddelivery.security.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Management", description = "APIs for authentication and authorization of users")
class AuthController(
    private val authService: AuthService
) {

    @RequestMapping("/login")
    @Operation(summary = "Login User")
    fun login(
        @RequestBody request: RegisterRequest
    ) = authService.login(request.email,request.password)

    @PostMapping("/register")
    @Operation(summary = "Register New User")
    fun register(
        @RequestBody request: RegisterRequest
    ) {
        authService.register(request)
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh Tokens")
    fun refresh(refreshTokenRequest: RefreshTokenRequest) = authService.refresh(refreshTokenRequest.refreshToken)
}