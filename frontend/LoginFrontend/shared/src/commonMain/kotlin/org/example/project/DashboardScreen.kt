package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DeepCoffeeGreen = Color(0xFF1B4D3E)
private val BrightForestGreen = Color(0xFF2D7D46)
private val CoffeeAmber = Color(0xFFC67C4E)
private val WarmBackground = Color(0xFFF9F6F0)
private val SoftCardBg = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF2C3E50)
private val MutedGrey = Color(0xFF7F8C8D)

@Composable
fun DashboardScreen(
    username: String,
    role: String,
    onOpenScanner: () -> Unit,
    onTriggerBiometrics: () -> Unit,
    onLogout: () -> Unit
) {
    val roleBadge = when (role) {
        "Propietario" -> "👑 Propietario de Finca"
        "Administrador" -> "🛡️ Administrador General"
        "Encargado de Lote" -> "🚜 Encargado de Lote"
        else -> "🤝 Colaborador de Campo"
    }

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

            // User Header & Role Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .shadow(4.dp, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "☕", fontSize = 28.sp)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "¡Hola, $username!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Surface(
                            color = CoffeeAmber,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = roleBadge,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Cerrar Sesión",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Quick Actions Title
            Text(
                text = "Acciones Principales & Sensores",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Sensor 1: Barcode / QR Scanner Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(20.dp))
                    .clickable { onOpenScanner() },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SoftCardBg)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(BrightForestGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📷", fontSize = 26.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Escáner de Lotes & Sacos",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = "Escanear códigos de barras de paquetes y entregas en campo",
                            fontSize = 12.sp,
                            color = MutedGrey
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Abrir",
                        tint = BrightForestGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Sensor 2: Biometric Sensor Verification Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(20.dp))
                    .clickable { onTriggerBiometrics() },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SoftCardBg)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(CoffeeAmber.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👆", fontSize = 26.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Sensor Biométrico (Huella)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = "Autenticación rápida mediante sensor de huella activado",
                            fontSize = 12.sp,
                            color = MutedGrey
                        )
                    }
                    Surface(
                        color = BrightForestGreen.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Activo ✓",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrightForestGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Role-Specific Section Header
            Text(
                text = "Panel de Control: $role",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Role Metrics Grid
            when (role) {
                "Propietario" -> {
                    MetricCard("Quintales Cosechados", "1,240 QQ", "Finca Ofelia - Producción 2026", Icons.Default.Star)
                    Spacer(modifier = Modifier.height(12.dp))
                    MetricCard("Lotes Activos", "8 Lotes", "Caturra, Castillo & Geisha", Icons.Default.Home)
                }
                "Administrador" -> {
                    MetricCard("Personal en Campo", "18 Colaboradores", "Asistencia registrada hoy", Icons.Default.Person)
                    Spacer(modifier = Modifier.height(12.dp))
                    MetricCard("Sacos en Bodega", "340 Sacos (48kg)", "Listo para exportación", Icons.Default.ShoppingCart)
                }
                "Encargado de Lote" -> {
                    MetricCard("Lote Asignado", "Lote 4 - El Roble", "Fase de Recolección", Icons.Default.Place)
                    Spacer(modifier = Modifier.height(12.dp))
                    MetricCard("Calidad de Grano", "98.5% Aprobado", "Muestra de laboratorio", Icons.Default.CheckCircle)
                }
                else -> {
                    MetricCard("Jornal Recolectado", "12 Sacos", "Registro del día", Icons.Default.Favorite)
                    Spacer(modifier = Modifier.height(12.dp))
                    MetricCard("Estado de Entrega", "Validado en Pesaje", "Verificado por Encargado", Icons.Default.Done)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Logout Button
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar Sesión",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MetricCard(title: String, value: String, description: String, icon: ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftCardBg)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(DeepCoffeeGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = DeepCoffeeGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontSize = 12.sp, color = MutedGrey)
                Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Text(text = description, fontSize = 11.sp, color = DeepCoffeeGreen, fontWeight = FontWeight.Medium)
            }
        }
    }
}
