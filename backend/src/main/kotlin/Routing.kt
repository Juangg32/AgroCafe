package com.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.util.concurrent.CopyOnWriteArrayList

@Serializable
data class UserDTO(
    val username: String,
    val password: String,
    val role: String,
    val fullName: String? = null,
    val email: String? = null
)

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val fullName: String,
    val email: String,
    val role: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val role: String? = null,
    val username: String? = null
)

private val routingJson = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}

private val usersStore = CopyOnWriteArrayList<UserDTO>()

fun Application.configureRouting() {
    if (usersStore.isEmpty()) {
        usersStore.addAll(loadUsersFromJson())
    }

    routing {
        get("/") {
            call.respondText("Servidor Backend de AgroCafé activo en puerto 8081", ContentType.Text.Plain)
        }

        post("/login") {
            val request = try {
                call.receive<LoginRequest>()
            } catch (_: Exception) {
                call.respond(
                    LoginResponse(
                        success = false,
                        message = "Formato de solicitud de inicio de sesión inválido"
                    )
                )
                return@post
            }

            if (request.username.isBlank() || request.password.isBlank()) {
                call.respond(
                    LoginResponse(
                        success = false,
                        message = "Por favor ingresa usuario/correo y contraseña"
                    )
                )
                return@post
            }

            val input = request.username.trim()
            val matchedUser = usersStore.find { user ->
                val matches = user.username.equals(input, ignoreCase = true) ||
                        (!user.email.isNullOrBlank() && user.email.equals(input, ignoreCase = true)) ||
                        (!user.fullName.isNullOrBlank() && user.fullName.equals(input, ignoreCase = true))
                matches && user.password == request.password
            }

            if (matchedUser != null) {
                val displayName = matchedUser.fullName?.takeIf { it.isNotBlank() } ?: matchedUser.username
                call.respond(
                    LoginResponse(
                        success = true,
                        message = "¡Bienvenido a AgroCafé, $displayName!",
                        role = matchedUser.role,
                        username = displayName
                    )
                )
            } else {
                call.respond(
                    LoginResponse(
                        success = false,
                        message = "Usuario o contraseña incorrectos"
                    )
                )
            }
        }

        post("/register") {
            val request = try {
                call.receive<RegisterRequest>()
            } catch (_: Exception) {
                call.respond(
                    LoginResponse(
                        success = false,
                        message = "Formato de solicitud de registro inválido"
                    )
                )
                return@post
            }

            if (request.fullName.isBlank() || request.email.isBlank() || request.password.isBlank()) {
                call.respond(
                    LoginResponse(
                        success = false,
                        message = "Todos los campos son obligatorios (nombre, correo y contraseña)"
                    )
                )
                return@post
            }

            val trimmedEmail = request.email.trim()
            val trimmedName = request.fullName.trim()
            val selectedRole = if (request.role.isNotBlank()) request.role.trim() else "Colaborador"

            val alreadyExists = usersStore.any { user ->
                (!user.email.isNullOrBlank() && user.email.equals(trimmedEmail, ignoreCase = true)) ||
                        user.username.equals(trimmedName, ignoreCase = true)
            }

            if (alreadyExists) {
                call.respond(
                    LoginResponse(
                        success = false,
                        message = "Ya existe una cuenta registrada con el correo $trimmedEmail"
                    )
                )
                return@post
            }

            val newUser = UserDTO(
                username = trimmedName,
                password = request.password,
                role = selectedRole,
                fullName = trimmedName,
                email = trimmedEmail
            )

            usersStore.add(newUser)

            call.respond(
                LoginResponse(
                    success = true,
                    message = "Usuario registrado exitosamente en AgroCafé",
                    role = newUser.role,
                    username = trimmedName
                )
            )
        }
    }
}

fun loadUsersFromJson(): List<UserDTO> {
    return try {
        val fileCandidates = listOf(
            File("backend/src/main/resources/users.json"),
            File("src/main/resources/users.json"),
            File("users.json")
        )
        val file = fileCandidates.firstOrNull { it.exists() }
        if (file != null) {
            return routingJson.decodeFromString(file.readText())
        }

        val jsonStream = object {}.javaClass.classLoader.getResourceAsStream("users.json")
        val jsonString = jsonStream?.bufferedReader()?.readText() ?: "[]"
        routingJson.decodeFromString(jsonString)
    } catch (_: Exception) {
        listOf(
            UserDTO("admin", "123", "Administrador", "Administrador AgroCafé", "admin@agrocafe.com"),
            UserDTO("propietario", "123", "Propietario", "Carlos Delgado", "propietario@agrocafe.com"),
            UserDTO("encargado", "123", "Encargado", "María Restrepo", "encargado@agrocafe.com"),
            UserDTO("colaborador", "123", "Colaborador", "Pedro Gómez", "colaborador@agrocafe.com")
        )
    }
}
