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

@OptIn(InternalSerializationApi::class)
@Serializable
data class Parcela(
    val id: String,
    val nombre: String,
    val comunidad: String,
    val altitud: Double,
    val variedad: String,
    val productorUsername: String
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class CosechaRequest(
    val id: String,
    val parcelaId: String,
    val productorUsername: String,
    val recolectorUsername: String,
    val comunidad: String,
    val variedad: String,
    val kilogramos: Double,
    val humedad: Double,
    val altitud: Double,
    val fechaCorte: String,
    val observaciones: String = ""
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class MicroLote(
    val id: String,
    val codigo: String,
    val productorUsername: String,
    val parcelaId: String,
    val variedad: String,
    val kilogramos: Double,
    val estado: String,
    val fechaRegistro: String
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class MacroLote(
    val id: String,
    val codigo: String,
    val variedad: String,
    val kilogramosTotales: Double,
    val estado: String,
    val microLoteIds: List<String>
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class AlertaClimatica(
    val id: String,
    val titulo: String,
    val mensaje: String,
    val tipo: String,
    val fecha: String,
    val parcelaId: String? = null
)
