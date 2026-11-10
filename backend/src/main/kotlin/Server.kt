import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.concurrent.CopyOnWriteArrayList

@Serializable
data class User(
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

private val jsonFormat = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}

private val possibleUserFiles = listOf(
    File("backend/src/main/resources/users.json"),
    File("src/main/resources/users.json"),
    File("users.json")
)

private fun resolveUsersFile(): File? {
    return possibleUserFiles.firstOrNull { it.exists() }
        ?: possibleUserFiles.firstOrNull { it.parentFile?.exists() == true }
}

private fun loadUsers(): CopyOnWriteArrayList<User> {
    try {
        val file = resolveUsersFile()
        if (file != null && file.exists()) {
            val content = file.readText()
            val list = jsonFormat.decodeFromString<List<User>>(content)
            println("Cargados ${list.size} usuarios desde ${file.absolutePath}")
            return CopyOnWriteArrayList(list)
        }

        val stream = object {}.javaClass.getResourceAsStream("/users.json")
        if (stream != null) {
            val content = stream.bufferedReader().use { it.readText() }
            val list = jsonFormat.decodeFromString<List<User>>(content)
            println("Cargados ${list.size} usuarios desde resources (/users.json)")
            return CopyOnWriteArrayList(list)
        }
    } catch (e: Exception) {
        println("Error cargando usuarios: ${e.message}")
    }

    // Usuarios por defecto para AgroCafé en caso de error de lectura
    return CopyOnWriteArrayList(
        listOf(
            User(username = "admin", fullName = "Administrador AgroCafé", email = "admin@agrocafe.com", password = "123", role = "Administrador"),
            User(username = "propietario", fullName = "Carlos Delgado", email = "propietario@agrocafe.com", password = "123", role = "Propietario"),
            User(username = "encargado", fullName = "María Restrepo", email = "encargado@agrocafe.com", password = "123", role = "Encargado"),
            User(username = "colaborador", fullName = "Pedro Gómez", email = "colaborador@agrocafe.com", password = "123", role = "Colaborador")
        )
    )
}

private fun saveUsers(users: List<User>) {
    try {
        val file = resolveUsersFile()
        if (file != null) {
            val jsonString = jsonFormat.encodeToString(users)
            file.writeText(jsonString)
            println("Usuarios guardados exitosamente en ${file.absolutePath}")
        }
    } catch (e: Exception) {
        println("Aviso: No se pudo persistir users.json en disco (${e.message}), se mantendrá en memoria.")
    }
}

fun main() {
    val users = loadUsers()

    // Prueba de despliegue automático
    println("Iniciando servidor AgroCafé en el puerto 8081 (host 0.0.0.0)...")
    println("Despliegue automático verificado - AgroCafé backend OK")

    embeddedServer(Netty, port = 8081, host = "0.0.0.0") {
        install(CORS) {
            anyHost()
            allowHeader(HttpHeaders.ContentType)
            allowHeader(HttpHeaders.Authorization)
            allowHeader(HttpHeaders.Accept)
            allowHeader(HttpHeaders.Origin)
            allowHeader(HttpHeaders.AccessControlAllowOrigin)
            allowMethod(HttpMethod.Options)
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Put)
            allowMethod(HttpMethod.Delete)
            allowNonSimpleContentTypes = true
        }

        install(ContentNegotiation) {
            json(jsonFormat)
        }

        routing {
            get("/") {
                call.respondText(
                    "Servidor Backend de AgroCafé activo en puerto 8081 (0.0.0.0)",
                    ContentType.Text.Plain
                )
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

                val inputIdentifier = request.username.trim()
                val matchedUser = users.find { user ->
                    val matchesUser = user.username.equals(inputIdentifier, ignoreCase = true) ||
                            (!user.email.isNullOrBlank() && user.email.equals(inputIdentifier, ignoreCase = true)) ||
                            (!user.fullName.isNullOrBlank() && user.fullName.equals(inputIdentifier, ignoreCase = true))
                    matchesUser && user.password == request.password
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

                val alreadyExists = users.any { user ->
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

                val newUser = User(
                    username = trimmedName,
                    password = request.password,
                    role = selectedRole,
                    fullName = trimmedName,
                    email = trimmedEmail
                )

                users.add(newUser)
                saveUsers(users)

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
    }.start(wait = true)
}
