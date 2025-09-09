package com.eugo.helloclaude

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.google.zxing.BarcodeFormat
import java.text.SimpleDateFormat
import java.util.*

data class ScannedCode(
    val code: String,
    val format: String,
    val timestamp: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarcodeScannerScreen() {
    var isScanning by remember { mutableStateOf(false) }
    var scannedCodes by remember { mutableStateOf(listOf<ScannedCode>()) }
    var hasCameraPermission by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    // カメラ権限のランチャー
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            isScanning = true
        }
    }
    
    // 権限チェック
    LaunchedEffect(Unit) {
        hasCameraPermission = ContextCompat.checkSelfPermission(
            context, 
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // タイトル
        Text(
            text = "バーコードスキャナー",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isScanning) {
            // スキャン中のカメラビュー
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                AndroidView(
                    factory = { context ->
                        DecoratedBarcodeView(context).apply {
                            val formats = listOf(
                                BarcodeFormat.CODE_39,
                                BarcodeFormat.CODE_128,
                                BarcodeFormat.QR_CODE
                            )
                            decoderFactory = com.journeyapps.barcodescanner.DefaultDecoderFactory(formats)
                            
                            val capture = CaptureManager(context as androidx.activity.ComponentActivity, this)
                            capture.initializeFromIntent(context.intent, null)
                            capture.decode()
                            
                            setStatusText("")
                            
                            // スキャン結果のコールバック
                            decodeContinuous { result ->
                                val timestamp = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPAN).format(Date())
                                val newCode = ScannedCode(
                                    code = result.text,
                                    format = result.barcodeFormat.toString(),
                                    timestamp = timestamp
                                )
                                scannedCodes = listOf(newCode) + scannedCodes
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { isScanning = false },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("スキャン停止")
            }
        } else {
            // スキャン開始ボタン
            Button(
                onClick = {
                    if (hasCameraPermission) {
                        isScanning = true
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("スキャン開始")
            }
            
            if (!hasCameraPermission) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "カメラの権限が必要です",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // スキャン履歴
        if (scannedCodes.isNotEmpty()) {
            Text(
                text = "スキャン履歴",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(scannedCodes) { code ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = code.code,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "形式: ${code.format}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = code.timestamp,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        } else if (!isScanning) {
            Text(
                text = "スキャンしたコードがここに表示されます",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}