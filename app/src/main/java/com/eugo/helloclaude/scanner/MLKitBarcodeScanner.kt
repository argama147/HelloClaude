package com.eugo.helloclaude.scanner

import com.eugo.helloclaude.data.BarcodeResult
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class MLKitBarcodeScanner : BarcodeScanner {
    private val scanner = BarcodeScanning.getClient()

    override suspend fun scanBarcode(imageData: ByteArray): BarcodeResult? {
        return suspendCancellableCoroutine { continuation ->
            try {
                // This is a simplified implementation
                // In a real app, you'd convert camera image data to InputImage properly
                val image = InputImage.fromByteArray(imageData, 640, 480, 0, InputImage.IMAGE_FORMAT_YV12)
                
                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        val barcode = barcodes.firstOrNull()
                        if (barcode != null) {
                            val result = BarcodeResult(
                                value = barcode.rawValue ?: "",
                                format = getBarcodeFormatName(barcode.format)
                            )
                            continuation.resume(result)
                        } else {
                            continuation.resume(null)
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(null)
                    }
            } catch (e: Exception) {
                continuation.resume(null)
            }
        }
    }

    private fun getBarcodeFormatName(format: Int): String {
        return when (format) {
            Barcode.FORMAT_CODE_128 -> "CODE_128"
            Barcode.FORMAT_CODE_39 -> "CODE_39"
            Barcode.FORMAT_CODE_93 -> "CODE_93"
            Barcode.FORMAT_CODABAR -> "CODABAR"
            Barcode.FORMAT_DATA_MATRIX -> "DATA_MATRIX"
            Barcode.FORMAT_EAN_13 -> "EAN_13"
            Barcode.FORMAT_EAN_8 -> "EAN_8"
            Barcode.FORMAT_ITF -> "ITF"
            Barcode.FORMAT_QR_CODE -> "QR_CODE"
            Barcode.FORMAT_UPC_A -> "UPC_A"
            Barcode.FORMAT_UPC_E -> "UPC_E"
            Barcode.FORMAT_PDF417 -> "PDF417"
            Barcode.FORMAT_AZTEC -> "AZTEC"
            else -> "UNKNOWN"
        }
    }

    override fun getLibraryName(): String = "ML Kit"
}