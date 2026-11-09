package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val DeepCoffeeGreen = Color(0xFF1B4D3E)
private val BrightForestGreen = Color(0xFF2D7D46)
private val CoffeeAmber = Color(0xFFC67C4E)
private val SoftCardBg = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF2C3E50)

@Composable
fun BarcodeScannerScreen(
    onCloseScanner: () -> Unit,
    onLotScanned: (CoffeeLotScan) -> Unit = {}
) {
    var isScanning by remember { mutableStateOf(false) }
    var scannedLot by remember { mutableStateOf<CoffeeLotScan?>(null) }
    var flashEnabled by remember { mutableStateOf(false) }

    val sampleLots = remember {
        listOf(
            CoffeeLotScan(
                code = "LOTE-2026-ARABICA-F04",
                lotName = "Lote El Roble - Finca Ofelia",
                variety = "Arábica Caturra",
                weightKg = 48.5,
                moistureContent = "11.2%",
                qualityGrade = "Grado Especial A1",
                scanTimestamp = "Hoy, 10:30 AM"
            ),
            CoffeeLotScan(
                code = "LOTE-2026-GEISHA-F02",
                lotName = "Lote La Cascada",
                variety = "Geisha Lavado",
                weightKg = 52.0,
                moistureContent = "10.8%",
                qualityGrade = "Calidad de Exportación",
                scanTimestamp = "Hoy, 11:15 AM"
            ),
            CoffeeLotScan(
                code = "SACO-ENTREGA-883",
                lotName = "Saco de Recolección Directa",
                variety = "Castillo Supremo",
                weightKg = 45.2,
                moistureContent = "12.0%",
                qualityGrade = "Grado Estándar",
                scanTimestamp = "Hoy, 11:45 AM"
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DeepCoffeeGreen,
                        Color(0xFF0F2B23),
                        Color(0xFF1A1A1A)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onCloseScanner,
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
                    text = "Escáner de Lotes en Campo",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "📷 Escáner de Códigos de Barras / QR",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Apunta la cámara a la etiqueta del lote o saco de café",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Viewfinder Box with REAL Live Camera Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(
                        width = 3.dp,
                        brush = Brush.horizontalGradient(listOf(BrightForestGreen, CoffeeAmber)),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Real Live Camera Feed
                CameraPreviewView(
                    modifier = Modifier.fillMaxSize(),
                    flashEnabled = flashEnabled,
                    onBarcodeDetected = { code ->
                        scannedLot = sampleLots.random()
                    }
                )

                // Viewfinder Reticle Overlay over Camera Preview
                Box(
                    modifier = Modifier
                        .size(200.dp, 150.dp)
                        .border(3.dp, BrightForestGreen, RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (isScanning) {
                        CircularProgressIndicator(
                            color = BrightForestGreen,
                            strokeWidth = 3.5.dp,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                // Text Overlay at bottom of Viewfinder
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isScanning) "Analizando etiqueta de lote..." else "Cámara en vivo - Listo para escanear",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Flash toggle button on top right of Viewfinder
                IconButton(
                    onClick = { flashEnabled = !flashEnabled },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(CircleShape)
                        .background(if (flashEnabled) CoffeeAmber else Color.Black.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = if (flashEnabled) "⚡" else "💡",
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons to Trigger Scanning
            Button(
                onClick = {
                    isScanning = true
                    scannedLot = null
                },
                enabled = !isScanning,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrightForestGreen)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Scan",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Escanear Etiqueta de Lote",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Auto-trigger analysis simulation
            LaunchedEffect(isScanning) {
                if (isScanning) {
                    delay(1500)
                    val result = sampleLots.random()
                    scannedLot = result
                    isScanning = false
                    onLotScanned(result)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Scanned Result Card Container
            AnimatedVisibility(
                visible = scannedLot != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                scannedLot?.let { lot ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SoftCardBg)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Success",
                                    tint = BrightForestGreen,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "¡Lote Validado!",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                    Text(
                                        text = lot.code,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = CoffeeAmber
                                    )
                                }
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = Color.LightGray.copy(alpha = 0.5f)
                            )

                            DetailRow("Nombre del Lote:", lot.lotName)
                            DetailRow("Variedad de Café:", lot.variety)
                            DetailRow("Peso Registrado:", "${lot.weightKg} kg")
                            DetailRow("Humedad:", lot.moistureContent)
                            DetailRow("Calidad:", lot.qualityGrade)
                            DetailRow("Fecha/Hora:", lot.scanTimestamp)

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { onCloseScanner() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = DeepCoffeeGreen)
                            ) {
                                Text(
                                    text = "Guardar y Confirmar Entrega",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = Color.Gray)
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
    }
}
