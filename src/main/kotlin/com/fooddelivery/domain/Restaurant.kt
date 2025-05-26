package com.fooddelivery.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.index.Indexed
import jakarta.validation.constraints.NotBlank
import org.bson.types.ObjectId

@Document(collection = "restaurants")
data class Restaurant(
    @Id
    val id: ObjectId? = ObjectId.get(),

    @Indexed(unique = true)
    @NotBlank
    val name: String,

    @NotBlank
    val address: String
) 