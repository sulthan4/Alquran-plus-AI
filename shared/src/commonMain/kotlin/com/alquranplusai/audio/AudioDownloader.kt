package com.alquranplusai.audio

class AudioDownloader {
    suspend fun downloadAudio(url: String, destinationPath: String): Boolean {
        // Platform specific download logic
        return true
    }
}
