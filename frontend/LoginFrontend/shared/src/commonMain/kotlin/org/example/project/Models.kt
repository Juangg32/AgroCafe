package org.example.project

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val role: String? = null,
    val username: String? = null
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class RegisterRequest(
    val fullName: String,
    val email: String,
    val role: String,
    val password: String
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class CoffeeLotScan(
    val code: String,
    val lotName: String,
    val variety: String,
    val weightKg: Double,
    val moistureContent: String,
    val qualityGrade: String,
    val scanTimestamp: String
)
