package com.fooddelivery.repository

import com.fooddelivery.domain.RefreshToken
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository : MongoRepository<RefreshToken, ObjectId> {
    fun findByUserIdAndHashedToken(userId: ObjectId , hashedToken: String): RefreshToken?

    // To delete old reference of refresh token , when user manually login
    fun deleteByUserIdAndHashedToken(userId: ObjectId , hashedToken: String): Long
}