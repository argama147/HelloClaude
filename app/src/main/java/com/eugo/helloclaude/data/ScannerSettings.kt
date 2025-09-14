package com.eugo.helloclaude.data

enum class CameraFacing {
    FRONT, BACK
}

enum class ScannerLibrary {
    ZXING, ML_KIT
}

data class ScannerSettings(
    val cameraFacing: CameraFacing = CameraFacing.BACK,
    val scannerLibrary: ScannerLibrary = ScannerLibrary.ZXING
)