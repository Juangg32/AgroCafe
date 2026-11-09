package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DeepCoffeeGreen = Color(0xFF1B4D3E)
private val BrightForestGreen = Color(0xFF2D7D46)
private val CoffeeAmber = Color(0xFFC67C4E)
private val WarmBackground = Color(0xFFF9F6F0)
private val SoftCardBg = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF2C3E50)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onRegisterSubmit: (String, String, String, String) -> Unit,
    isLoading: Boolean = false
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val roles = listOf("Propietario", "Administrador", "Encargado", "Colaborador")
    var selectedRole by remember { mutableStateOf(roles[0]) }

    var acceptedTerms by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    val passwordsMatch = password.isNotEmpty() && password == confirmPassword
    val passwordValid = password.length >= 8

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DeepCoffeeGreen,
                        BrightForestGreen,
                        WarmBackground
                    ),
                    startY = 0f,
                    endY = 550f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Top Header Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Crear nueva cuenta",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Únete a AgroCafé 🌿",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = "Gestión integral de lotes, cosechas y trazabilidad",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Card Container
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SoftCardBg)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Nombre Completo") },
                        placeholder = { Text("ej. Juan Pérez") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Name",
                                tint = DeepCoffeeGreen
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeepCoffeeGreen,
                            focusedLabelColor = DeepCoffeeGreen,
                            cursorColor = DeepCoffeeGreen
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo Electrónico") },
                        placeholder = { Text("correo@ejemplo.com") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = "Email",
                                tint = DeepCoffeeGreen
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeepCoffeeGreen,
                            focusedLabelColor = DeepCoffeeGreen,
                            cursorColor = DeepCoffeeGreen
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Role Selector Chips
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Selecciona tu Rol en la Finca:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            roles.forEach { role ->
                                val isSelected = selectedRole == role
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedRole = role },
                                    label = { Text(role, fontSize = 12.sp) },
                                    leadingIcon = if (isSelected) {
                                        {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    } else null,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = DeepCoffeeGreen,
                                        selectedLabelColor = Color.White,
                                        selectedLeadingIconColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "Lock",
                                tint = DeepCoffeeGreen
                            )
                        },
                        trailingIcon = {
                            TextButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Text(
                                    text = if (isPasswordVisible) "Ocultar" else "Mostrar",
                                    fontSize = 12.sp,
                                    color = CoffeeAmber
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeepCoffeeGreen,
                            focusedLabelColor = DeepCoffeeGreen,
                            cursorColor = DeepCoffeeGreen
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirmar Contraseña") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "Lock Check",
                                tint = DeepCoffeeGreen
                            )
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (confirmPassword.isNotEmpty() && !passwordsMatch) Color.Red else DeepCoffeeGreen,
                            focusedLabelColor = DeepCoffeeGreen,
                            cursorColor = DeepCoffeeGreen
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    // Helper indicator for Password
                    AnimatedVisibility(visible = password.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp, start = 4.dp)
                        ) {
                            Text(
                                text = if (passwordValid) "✓ Al menos 8 caracteres" else "✗ Debe tener al menos 8 caracteres",
                                fontSize = 11.sp,
                                color = if (passwordValid) BrightForestGreen else Color.Red
                            )
                            if (confirmPassword.isNotEmpty()) {
                                Text(
                                    text = if (passwordsMatch) "✓ Las contraseñas coinciden" else "✗ Las contraseñas no coinciden",
                                    fontSize = 11.sp,
                                    color = if (passwordsMatch) BrightForestGreen else Color.Red
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Terms Checkbox Card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = WarmBackground),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Checkbox(
                                checked = acceptedTerms,
                                onCheckedChange = { acceptedTerms = it },
                                colors = CheckboxDefaults.colors(checkedColor = DeepCoffeeGreen)
                            )
                            Text(
                                text = "Acepto los Términos de Servicio y la Política de Privacidad de AgroCafé.",
                                fontSize = 12.sp,
                                color = TextDark,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    val isFormValid = !isLoading && acceptedTerms && fullName.isNotBlank() && email.isNotBlank() && passwordValid && passwordsMatch

                    Button(
                        onClick = {
                            keyboardController?.hide()
                            onRegisterSubmit(fullName, email, selectedRole, password)
                        },
                        enabled = isFormValid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepCoffeeGreen,
                            disabledContainerColor = DeepCoffeeGreen.copy(alpha = 0.5f)
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                text = "Crear Cuenta",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer to Login
            Surface(
                color = SoftCardBg,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onBackClick() },
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿Ya tienes una cuenta? ",
                        fontSize = 14.sp,
                        color = TextDark
                    )
                    Text(
                        text = "Inicia Sesión",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepCoffeeGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
