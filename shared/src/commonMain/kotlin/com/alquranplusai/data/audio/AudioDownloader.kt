package com.alquranplusai.data.audio

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Audio downloader for offline playback
 */
class AudioDownloader {
    
    private val downloadProgress = mutableMapOf<String, MutableStateFlow<Int>>()
    
    suspend fun download(url: String, destination: String): Result<String> {
        val progressFlow = MutableStateFlow(0)
        downloadProgress[url] = progressFlow
        
        // Simulate download progress
        for (progress in 0..100 step 10) {
            progressFlow.value = progress
            kotlinx.coroutines.delay(100)
        }
        
        downloadProgress.remove(url)
        return Result.success(destination)
    }
    
    fun getDownloadProgress(url: String): Flow<Int> {
        return downloadProgress[url]?.asStateFlow() ?: kotlinx.coroutines.flow.flowOf(0)
    }
    
    suspend fun cancelDownload(url: String) {
        downloadProgress.remove(url)
    }
}
