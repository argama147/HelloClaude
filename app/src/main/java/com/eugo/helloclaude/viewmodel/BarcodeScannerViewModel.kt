package com.eugo.helloclaude.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eugo.helloclaude.data.BarcodeResult
import com.eugo.helloclaude.data.CameraFacing
import com.eugo.helloclaude.data.ScannerLibrary
import com.eugo.helloclaude.data.ScannerSettings
import com.eugo.helloclaude.scanner.BarcodeScanner
import com.eugo.helloclaude.scanner.MLKitBarcodeScanner
import com.eugo.helloclaude.scanner.ZxingBarcodeScanner
import com.eugo.helloclaude.utils.SoundPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BarcodeScannerViewModel : ViewModel() {
    private val _settings = MutableStateFlow(ScannerSettings())
    val settings: StateFlow<ScannerSettings> = _settings.asStateFlow()

    private val _barcodeResult = MutableStateFlow<BarcodeResult?>(null)
    val barcodeResult: StateFlow<BarcodeResult?> = _barcodeResult.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()

    private var soundPlayer: SoundPlayer? = null
    private var currentScanner: BarcodeScanner? = null

    fun initializeSoundPlayer(context: Context) {
        soundPlayer = SoundPlayer(context)
    }

    fun updateCameraFacing(cameraFacing: CameraFacing) {
        _settings.value = _settings.value.copy(cameraFacing = cameraFacing)
    }

    fun updateScannerLibrary(scannerLibrary: ScannerLibrary) {
        _settings.value = _settings.value.copy(scannerLibrary = scannerLibrary)
        currentScanner = when (scannerLibrary) {
            ScannerLibrary.ZXING -> ZxingBarcodeScanner()
            ScannerLibrary.ML_KIT -> MLKitBarcodeScanner()
        }
    }

    fun startScanning() {
        _isScanning.value = true
        _barcodeResult.value = null
        _isPaused.value = false
    }

    fun stopScanning() {
        _isScanning.value = false
        _isPaused.value = false
    }

    fun processImageData(imageData: ByteArray) {
        if (!_isScanning.value || _isPaused.value) return

        viewModelScope.launch {
            val scanner = currentScanner ?: return@launch
            val result = scanner.scanBarcode(imageData)
            
            if (result != null) {
                _barcodeResult.value = result
                _isPaused.value = true
                
                // Play success beeps
                soundPlayer?.playSuccessBeeps()
                
                // Pause scanning for 3 seconds
                delay(3000)
                _isPaused.value = false
            }
        }
    }

    fun clearResult() {
        _barcodeResult.value = null
    }

    override fun onCleared() {
        super.onCleared()
        soundPlayer?.release()
    }
}