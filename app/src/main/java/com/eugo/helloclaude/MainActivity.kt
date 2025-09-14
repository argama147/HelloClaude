package com.eugo.helloclaude

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eugo.helloclaude.ui.screen.CameraScreen
import com.eugo.helloclaude.ui.screen.SettingsScreen
import com.eugo.helloclaude.ui.theme.HelloClaudeTheme
import com.eugo.helloclaude.viewmodel.BarcodeScannerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloClaudeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BarcodeScannerApp()
                }
            }
        }
    }
}

@Composable
fun BarcodeScannerApp() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val viewModel: BarcodeScannerViewModel = viewModel()
    val settings by viewModel.settings.collectAsState()
    val barcodeResult by viewModel.barcodeResult.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()
    val isPaused by viewModel.isPaused.collectAsState()

    // Initialize sound player
    LaunchedEffect(Unit) {
        viewModel.initializeSoundPlayer(context)
        viewModel.updateScannerLibrary(settings.scannerLibrary)
    }

    if (isScanning) {
        CameraScreen(
            cameraFacing = settings.cameraFacing,
            barcodeResult = barcodeResult,
            isPaused = isPaused,
            onBackPressed = { viewModel.stopScanning() },
            onImageAnalysis = { imageData ->
                viewModel.processImageData(imageData)
            }
        )
    } else {
        SettingsScreen(
            settings = settings,
            barcodeResult = barcodeResult,
            onCameraFacingChanged = { facing ->
                viewModel.updateCameraFacing(facing)
            },
            onScannerLibraryChanged = { library ->
                viewModel.updateScannerLibrary(library)
            },
            onStartScan = {
                viewModel.startScanning()
            },
            onClearResult = {
                viewModel.clearResult()
            }
        )
    }
}