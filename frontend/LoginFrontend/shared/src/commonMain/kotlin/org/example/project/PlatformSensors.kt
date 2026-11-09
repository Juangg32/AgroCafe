package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun rememberBiometricAuthenticator(): BiometricAuthenticator

interface BiometricAuthenticator {
    fun authenticate(
        title: String,
        subtitle: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
}

@Composable
expect fun CameraPreviewView(
    modifier: Modifier,
    flashEnabled: Boolean,
    onBarcodeDetected: (String) -> Unit
)
