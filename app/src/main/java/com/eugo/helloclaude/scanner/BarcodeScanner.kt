package com.eugo.helloclaude.scanner

import com.eugo.helloclaude.data.BarcodeResult

interface BarcodeScanner {
    suspend fun scanBarcode(imageData: ByteArray): BarcodeResult?
    fun getLibraryName(): String
}