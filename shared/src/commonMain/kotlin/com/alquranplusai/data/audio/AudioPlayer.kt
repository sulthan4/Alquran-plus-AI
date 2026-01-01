package com.alquranplusai.data.audio

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Audio player for Quran recitation
 */
interface AudioPlayer {
    
    fun getPlaybackState(): Flow<PlaybackState>
    
    fun getCurrentPosition(): Flow<Long>
    
    suspend fun play(url: String)
    
    suspend fun pause()
    
    suspend fun resume()
    
    suspend fun stop()
    
    suspend fun seekTo(position: Long)
    
    suspend fun setSpeed(speed: Float)
    
    suspend fun setVolume(volume: Float)
}

enum class PlaybackState {
    IDLE, PLAYING, PAUSED, STOPPED
}
