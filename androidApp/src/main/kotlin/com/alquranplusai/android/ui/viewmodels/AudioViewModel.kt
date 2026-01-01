package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.Reciter
import com.alquranplusai.domain.repositories.AudioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for audio playback and reciter management
 */
class AudioViewModel(
    private val audioRepository: AudioRepository
) : ViewModel() {
    
    private val _reciters = MutableStateFlow<List<Reciter>>(emptyList())
    val reciters: StateFlow<List<Reciter>> = _reciters.asStateFlow()
    
    private val _selectedReciter = MutableStateFlow<Reciter?>(null)
    val selectedReciter: StateFlow<Reciter?> = _selectedReciter.asStateFlow()
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()
    
    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()
    
    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadReciters()
    }
    
    fun loadReciters() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                audioRepository.getAllReciters().collect { reciters ->
                    _reciters.value = reciters
                    if (_selectedReciter.value == null && reciters.isNotEmpty()) {
                        _selectedReciter.value = reciters.first()
                    }
                }
            } catch (e: Exception) {
                _error.value = "Failed to load reciters: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun selectReciter(reciter: Reciter) {
        _selectedReciter.value = reciter
    }
    
    fun play() {
        _isPlaying.value = true
    }
    
    fun pause() {
        _isPlaying.value = false
    }
    
    fun seekTo(position: Long) {
        _currentPosition.value = position
    }
    
    fun setPlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed
    }
    
    fun updatePosition(position: Long) {
        _currentPosition.value = position
    }
    
    fun updateDuration(duration: Long) {
        _duration.value = duration
    }
    
    fun clearError() {
        _error.value = null
    }
}
