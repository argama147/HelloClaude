package com.eugo.helloclaude.utils

import android.content.Context
import android.media.ToneGenerator
import android.media.AudioManager
import kotlinx.coroutines.delay

class SoundPlayer(private val context: Context) {
    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
        } catch (e: RuntimeException) {
            // Handle case where ToneGenerator fails to initialize
        }
    }

    suspend fun playSuccessBeeps() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
            delay(300)
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
        } catch (e: Exception) {
            // Ignore sound errors
        }
    }

    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
}