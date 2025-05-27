package com.fooddelivery.security

import com.fooddelivery.domain.RefreshToken
import com.fooddelivery.domain.User
import com.fooddelivery.dto.RegisterRequest
import com.fooddelivery.repository.RefreshTokenRepository
import com.fooddelivery.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64

@Service
class AuthService(
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    fun register(request: RegisterRequest) : User {
        val email = userRepository.findByEmail(request.email)
        if(email != null) {
            throw IllegalArgumentException("User already exists")
        }
        val userName = request.name ?:  request.email.substringBefore("@")
        val user = User(
            name = userName,
            email = request.email,
            encodedPassword = passwordEncoder.encode(request.password),
            role = request.role
        )
        return userRepository.save(user)
    }

    fun login(email: String, password: String): TokenPair {
        val user = userRepository.findByEmail(email) ?: throw BadCredentialsException("Invalid Credentials")

        if(!passwordEncoder.matches(password , user.encodedPassword)) {
            throw BadCredentialsException("Invalid password")
        }

        val accessToken = jwtService.generateAccessToken(user.id.toHexString())
        val refreshToken = jwtService.generateRefreshToken(user.id.toHexString())

        storeRefreshToken(refreshToken , user.id)

        return TokenPair(accessToken , refreshToken)
    }


    // When Access token is expired , refresh token will be used to get new access token
    // Annotation as it refers , it will perform all transactions at once, i.e if any one of them fails , whole thing fails and db will rollback
    // Edge case : refreshToken deleted, but somehow new refreshToken not got stored and returned failure, at this point no refresh token will be there in db for that particular user
    @Transactional
    fun refresh(refreshToken: String): TokenPair {
        if(!jwtService.validateToken(refreshToken , "refresh")) {
            throw IllegalArgumentException("Invalid refresh token")
        }

        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findById(ObjectId(userId)).orElseThrow {
            IllegalArgumentException("Invalid refresh token")
        }
        val hashedToken = hashedToken(refreshToken)

        refreshTokenRepository.findByUserIdAndHashedToken(user.id , hashedToken) ?: throw BadCredentialsException("Invalid refresh token")

        refreshTokenRepository.deleteByUserIdAndHashedToken(user.id , hashedToken)

        val newAccessToken = jwtService.generateAccessToken(userId)
        val newRefreshToken = jwtService.generateRefreshToken(userId)

        storeRefreshToken(newRefreshToken , user.id)

        return TokenPair(newAccessToken , newRefreshToken)
    }

    private fun storeRefreshToken(rawRefreshToken: String, userId: ObjectId) {
        val hashedToken = hashedToken(rawRefreshToken)
        val expiryMs = jwtService.refreshTokenExpireInMs
        val expiresAt = Instant.now().plusMillis(expiryMs)
        val refreshToken = RefreshToken(
            userId = userId,
            hashedToken = hashedToken,
            expiresAt = expiresAt
        )

        refreshTokenRepository.save(refreshToken)

    }

    private fun hashedToken(token : String) : String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(token.toByteArray())
        return Base64.getEncoder().encodeToString(hash)
    }

    data class TokenPair(val accessToken: String, val refreshToken: String)
}