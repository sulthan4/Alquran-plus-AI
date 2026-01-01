package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.platform.local.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


/**
 * ViewModel for DisplaySettingsViewModel
 */
class DisplaySettingsViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    // Existing UI preferences
    private val _animationsEnabled = MutableStateFlow(true)
    val animationsEnabled: StateFlow<Boolean> = _animationsEnabled.asStateFlow()
    
    private val _hapticFeedbackEnabled = MutableStateFlow(true)
    val hapticFeedbackEnabled: StateFlow<Boolean> = _hapticFeedbackEnabled.asStateFlow()
    
    // NEW: Reading settings
    private val _arabicFontSize = MutableStateFlow(24f)
    val arabicFontSize: StateFlow<Float> = _arabicFontSize.asStateFlow()
    
    private val _translationFontSize = MutableStateFlow(16f)
    val translationFontSize: StateFlow<Float> = _translationFontSize.asStateFlow()
    
    private val _lineSpacing = MutableStateFlow(1.5f)
    val lineSpacing: StateFlow<Float> = _lineSpacing.asStateFlow()
    
    private val _showTranslation = MutableStateFlow(true)
    val showTranslation: StateFlow<Boolean> = _showTranslation.asStateFlow()
    
    private val _showTransliteration = MutableStateFlow(false)
    val showTransliteration: StateFlow<Boolean> = _showTransliteration.asStateFlow()
    
    private val _keepScreenOn = MutableStateFlow(false)
    val keepScreenOn: StateFlow<Boolean> = _keepScreenOn.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        // Collect existing preferences
        viewModelScope.launch {
            preferencesManager.animationsEnabled.collect { enabled ->
                _animationsEnabled.value = enabled
            }
        }
        viewModelScope.launch {
            preferencesManager.hapticFeedbackEnabled.collect { enabled ->
                _hapticFeedbackEnabled.value = enabled
            }
        }
        
        // NEW: Collect reading preferences
        viewModelScope.launch {
            preferencesManager.arabicFontSize.collect { size ->
                _arabicFontSize.value = size
            }
        }
        viewModelScope.launch {
            preferencesManager.translationFontSize.collect { size ->
                _translationFontSize.value = size
            }
        }
        viewModelScope.launch {
            preferencesManager.lineSpacing.collect { spacing ->
                _lineSpacing.value = spacing
            }
        }
        viewModelScope.launch {
            preferencesManager.showTranslation.collect { show ->
                _showTranslation.value = show
            }
        }
        viewModelScope.launch {
            preferencesManager.showTransliteration.collect { show ->
                _showTransliteration.value = show
            }
        }
        viewModelScope.launch {
            preferencesManager.keepScreenOn.collect { keep ->
                _keepScreenOn.value = keep
            }
        }
    }
    
    // Existing setters
    fun setAnimationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateAnimationsEnabled(enabled)
        }
    }
    
    fun setHapticFeedbackEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateHapticFeedbackEnabled(enabled)
        }
    }
    
    // NEW: Reading settings setters
    fun setArabicFontSize(size: Float) {
        viewModelScope.launch {
            preferencesManager.updateArabicFontSize(size)
        }
    }
    
    fun setTranslationFontSize(size: Float) {
        viewModelScope.launch {
            preferencesManager.updateTranslationFontSize(size)
        }
    }
    
    fun setLineSpacing(spacing: Float) {
        viewModelScope.launch {
            preferencesManager.updateLineSpacing(spacing)
        }
    }
    
    fun setShowTranslation(show: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateShowTranslation(show)
        }
    }
    
    fun setShowTransliteration(show: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateShowTransliteration(show)
        }
    }
    
    fun setKeepScreenOn(keep: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateKeepScreenOn(keep)
        }
    }
}
