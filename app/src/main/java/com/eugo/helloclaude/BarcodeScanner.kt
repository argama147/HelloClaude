package com.eugo.helloclaude

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

/**
 * BarcodeScanner class for handling QR/barcode scanning functionality
 * Uses ZXing Android embedded library for barcode scanning
 */
class BarcodeScanner(private val activity: AppCompatActivity) {
    
    private lateinit var barcodeLauncher: ActivityResultLauncher<ScanOptions>
    private var onScanResult: ((String?) -> Unit)? = null
    
    init {
        initializeBarcodeScanner()
    }
    
    private fun initializeBarcodeScanner() {
        barcodeLauncher = activity.registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            if (result.contents == null) {
                onScanResult?.invoke(null)
            } else {
                onScanResult?.invoke(result.contents)
            }
        }
    }
    
    /**
     * Start barcode scanning with custom options
     */
    fun startScan(callback: (String?) -> Unit) {
        onScanResult = callback
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE, ScanOptions.CODE_128)
        options.setPrompt("Scan a barcode")
        options.setCameraId(0) // Use rear camera
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        
        barcodeLauncher.launch(options)
    }
    
    /**
     * Start QR code scanning specifically
     */
    fun startQrScan(callback: (String?) -> Unit) {
        onScanResult = callback
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan a QR code")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        
        barcodeLauncher.launch(options)
    }
}