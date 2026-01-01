package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Using shared preferences
import com.alquranplusai.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AudioSettingsViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _defaultReciter = MutableStateFlow("abdul_basit")
    val defaultReciter: StateFlow<String> = _defaultReciter.asStateFlow()
    
    private val _autoPlay = MutableStateFlow(false)
    val autoPlay: StateFlow<Boolean> = _autoPlay.asStateFlow()
    
    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()
    
    init {
        viewModelScope.launch {
            preferencesManager.autoPlay.collect { _autoPlay.value = it }
        }
        viewModelScope.launch {
            preferencesManager.playbackSpeed.collect { _playbackSpeed.value = it }
        }
        viewModelScope.launch {
            preferencesManager.defaultReciterId.collect { id ->
                id?.let { _defaultReciter.value = it }
            }
        }
    }
    
    fun setDefaultReciter(reciter: String) {
        viewModelScope.launch {
            preferencesManager.updateDefaultReciterId(reciter)
            _defaultReciter.value = reciter
        }
    }
    
    fun setAutoPlay(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateAutoPlay(enabled)
            _autoPlay.value = enabled
        }
    }
    
    fun setPlaybackSpeed(speed: Float) {
         viewModelScope.launch {
            preferencesManager.updatePlaybackSpeed(speed)
             _playbackSpeed.value = speed
         }
    }
}

