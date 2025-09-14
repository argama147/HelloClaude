package com.eugo.helloclaude.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eugo.helloclaude.data.BarcodeResult
import com.eugo.helloclaude.data.CameraFacing
import com.eugo.helloclaude.data.ScannerLibrary
import com.eugo.helloclaude.data.ScannerSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: ScannerSettings,
    barcodeResult: BarcodeResult?,
    onCameraFacingChanged: (CameraFacing) -> Unit,
    onScannerLibraryChanged: (ScannerLibrary) -> Unit,
    onStartScan: () -> Unit,
    onClearResult: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "バーコードスキャナー設定",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        // Camera Selection
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "カメラ選択",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                Column(modifier = Modifier.selectableGroup()) {
                    CameraFacing.values().forEach { facing ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = settings.cameraFacing == facing,
                                    onClick = { onCameraFacingChanged(facing) },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = settings.cameraFacing == facing,
                                onClick = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (facing) {
                                    CameraFacing.FRONT -> "フロントカメラ"
                                    CameraFacing.BACK -> "バックカメラ"
                                }
                            )
                        }
                    }
                }
            }
        }

        // Scanner Library Selection
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "スキャンライブラリ選択",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                Column(modifier = Modifier.selectableGroup()) {
                    ScannerLibrary.values().forEach { library ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = settings.scannerLibrary == library,
                                    onClick = { onScannerLibraryChanged(library) },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = settings.scannerLibrary == library,
                                onClick = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (library) {
                                    ScannerLibrary.ZXING -> "ZXing ライブラリ"
                                    ScannerLibrary.ML_KIT -> "ML Kit ライブラリ"
                                }
                            )
                        }
                    }
                }
            }
        }

        // Scan Result Display
        if (barcodeResult != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "スキャン結果",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = "値: ${barcodeResult.value}",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "フォーマット: ${barcodeResult.format}",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Button(
                        onClick = onClearResult,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("結果をクリア")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Start Scan Button
        Button(
            onClick = onStartScan,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "スキャン開始",
                fontSize = 18.sp
            )
        }
    }
}