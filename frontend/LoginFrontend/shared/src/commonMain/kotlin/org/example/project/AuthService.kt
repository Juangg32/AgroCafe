package org.example.project

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object AuthService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    var customServerIp: String = ""

    private fun getBaseUrl(): String {
        if (customServerIp.isNotBlank()) {
            return "http://${customServerIp.trim()}:8081"
        }
        return if (getPlatform().name.startsWith("Android")) {
            "http://10.0.2.2:8081"
        } else {
            "http://127.0.0.1:8081"
        }
    }

    suspend fun login(username: String, password: String): LoginResponse {
        return try {
            val response = client.post("${getBaseUrl()}/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username = username, password = password))
            }
            response.body<LoginResponse>()
        } catch (e: Exception) {
            // Retorna respuesta de falla pero permite modo offline/demo si el usuario lo solicita
            LoginResponse(
                success = false,
                message = "No se pudo conectar al servidor backend (${e.localizedMessage}). Puedes ingresar en Modo Demostración Offline.",
                role = "Propietario",
                username = username.ifBlank { "Usuario AgroCafé" }
            )
        }
    }

    suspend fun register(fullName: String, email: String, role: String, password: String): LoginResponse {
        return try {
            val response = client.post("${getBaseUrl()}/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(fullName = fullName, email = email, role = role, password = password))
            }
            response.body<LoginResponse>()
        } catch (e: Exception) {
            LoginResponse(
                success = true, // Permite completar el registro y continuar
                message = "Registro completado exitosamente (Modo Local / Offline).",
                role = role,
                username = fullName
            )
        }
    }

    fun getDemoLoginResponse(username: String, role: String = "Propietario"): LoginResponse {
        return LoginResponse(
            success = true,
            message = "Sesión iniciada con éxito",
            role = role,
            username = if (username.isNotBlank()) username else "Juan Pérez"
        )
    }
}
