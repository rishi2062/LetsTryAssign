package com.fooddelivery.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordEncoder {
    private val bCrypt = BCryptPasswordEncoder()

    fun encode(password: String): String = bCrypt.encode(password)

    fun matches(password: String, encodedPassword: String): Boolean = bCrypt.matches(password, encodedPassword)
}