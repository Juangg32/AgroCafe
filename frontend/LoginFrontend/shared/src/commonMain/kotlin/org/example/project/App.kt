package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

enum class Screen {
    LOGIN,
    REGISTER,
    DASHBOARD,
    SCANNER
}

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.LOGIN) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var currentUsername by remember { mutableStateOf("Juan Pérez") }
    var currentUserRole by remember { mutableStateOf("Propietario") }

    val coroutineScope = rememberCoroutineScope()
    val biometricAuthenticator = rememberBiometricAuthenticator()

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (currentScreen) {
                Screen.LOGIN -> LoginScreen(
                    onLoginClick = { username, password ->
                        coroutineScope.launch {
                            isLoading = true
                            errorMessage = null
                            val response = AuthService.login(username, password)
                            isLoading = false
                            if (response.success) {
                                currentUsername = response.username ?: username
                                currentUserRole = response.role ?: "Propietario"
                                currentScreen = Screen.DASHBOARD
                            } else {
                                errorMessage = response.message
                                currentUsername = if (username.isNotBlank()) username else "Juan Pérez"
                            }
                        }
                    },
                    onBiometricLoginClick = {
                        biometricAuthenticator.authenticate(
                            title = "Autenticación AgroCafé",
                            subtitle = "Coloca tu huella dactilar para acceder",
                            onSuccess = {
                                errorMessage = null
                                currentScreen = Screen.DASHBOARD
                            },
                            onError = { err ->
                                errorMessage = err
                            }
                        )
                    },
                    onDemoLoginClick = {
                        val response = AuthService.getDemoLoginResponse(currentUsername, currentUserRole)
                        currentUsername = response.username ?: "Juan Pérez"
                        currentUserRole = response.role ?: "Propietario"
                        errorMessage = null
                        currentScreen = Screen.DASHBOARD
                    },
                    onNavigateToRegister = {
                        errorMessage = null
                        currentScreen = Screen.REGISTER
                    },
                    isLoading = isLoading,
                    errorMessage = errorMessage
                )

                Screen.REGISTER -> RegisterScreen(
                    onBackClick = {
                        errorMessage = null
                        currentScreen = Screen.LOGIN
                    },
                    onRegisterSubmit = { fullName, email, role, password ->
                        coroutineScope.launch {
                            isLoading = true
                            errorMessage = null
                            val response = AuthService.register(fullName, email, role, password)
                            isLoading = false
                            if (response.success) {
                                currentUsername = fullName
                                currentUserRole = role
                                currentScreen = Screen.DASHBOARD
                            } else {
                                errorMessage = response.message
                            }
                        }
                    },
                    isLoading = isLoading
                )

                Screen.DASHBOARD -> DashboardScreen(
                    username = currentUsername,
                    role = currentUserRole,
                    onOpenScanner = {
                        currentScreen = Screen.SCANNER
                    },
                    onTriggerBiometrics = {
                        biometricAuthenticator.authenticate(
                            title = "Verificación Biométrica",
                            subtitle = "Confirma tu huella para acceder a los registros",
                            onSuccess = {},
                            onError = {}
                        )
                    },
                    onLogout = {
                        currentUsername = ""
                        currentUserRole = "Propietario"
                        errorMessage = null
                        currentScreen = Screen.LOGIN
                    }
                )

                Screen.SCANNER -> BarcodeScannerScreen(
                    onCloseScanner = {
                        currentScreen = Screen.DASHBOARD
                    }
                )
            }
        }
    }
}
