package com.eugo.helloclaude.data

data class BarcodeResult(
    val value: String,
    val format: String,
    val timestamp: Long = System.currentTimeMillis()
)