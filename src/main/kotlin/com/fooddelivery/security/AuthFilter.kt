package com.fooddelivery.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter

@Service
class AuthFilter(
    private val jwtService: JWTService
) : OncePerRequestFilter(){

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // will be in format Bearer <token>
        val authHeader = request.getHeader("Authorization")

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            if(jwtService.validateToken(authHeader , "access")) {
                val userId = jwtService.getUserIdFromToken(authHeader)
                val authenticatedUser = UsernamePasswordAuthenticationToken(userId , null)

                // Global context for all requests
                SecurityContextHolder.getContext().authentication = authenticatedUser

            }
        }

        filterChain.doFilter(request , response)
    }

}