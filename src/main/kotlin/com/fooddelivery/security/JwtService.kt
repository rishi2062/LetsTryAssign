package com.fooddelivery.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Base64
import java.util.Date

@Service
class JwtService(
    @Value("\${jwt.secret}") private val jwtSecret: String
) {

    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret))
    private val accessTokenExpireInMs = 15L * 60L * 1000L // 15 min
    val refreshTokenExpireInMs = 30L * 24 * 60 * 60 * 1000L // 30 Days

    private fun generateToken(
        userId : String,
        type : String,
        expireIn : Long
    ) : String {
        val currentDate = Date()
        val expiryDate = Date(currentDate.time + expireIn)
        return Jwts.builder()
            .subject(userId)
            .claim("type" , type)
            .issuedAt(currentDate)
            .expiration(expiryDate)
            .signWith(secretKey , Jwts.SIG.HS256)
            .compact()
    }

    fun generateAccessToken(userId : String) : String {
        return generateToken(userId , "access" , accessTokenExpireInMs)
    }

    fun generateRefreshToken(userId : String) : String {
        return generateToken(userId , "refresh" , refreshTokenExpireInMs)
    }

    fun validateToken(token : String , type: String) : Boolean {
        try {
            val claims = parseAllClaims(token) ?: return false

            val tokenType = claims.get("type") as? String ?: return false
            return tokenType == type
        } catch (e: Exception) {
            return false
        }

    }

    fun getUserIdFromToken(token : String) : String {
        val claims = parseAllClaims(token) ?: throw IllegalArgumentException("Invalid token")
        return claims.subject
    }


    // When token passed by client , it is parsed and if claims are valid , the token is valid else either it is wrong or expired or changed
    private fun parseAllClaims(token : String) : Claims? {
        val rawToken = if(token.startsWith("Bearer ")) token.removePrefix("Bearer ") else token
        return try{
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(rawToken)
                .payload
        } catch (e : Exception) {
            null
        }
    }
}