package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Privacy Settings
 */
class PrivacySettingsViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _analyticsEnabled = MutableStateFlow(true)
    val analyticsEnabled: StateFlow<Boolean> = _analyticsEnabled.asStateFlow()
    
    private val _crashReportsEnabled = MutableStateFlow(true)
    val crashReportsEnabled: StateFlow<Boolean> = _crashReportsEnabled.asStateFlow()
    
    private val _dataCollectionEnabled = MutableStateFlow(true)
    val dataCollectionEnabled: StateFlow<Boolean> = _dataCollectionEnabled.asStateFlow()
    
    init {
        // Load settings from preferences
        viewModelScope.launch {
            preferencesManager.animationsEnabled.collect { enabled ->
                _analyticsEnabled.value = enabled
            }
        }
        viewModelScope.launch {
            preferencesManager.hapticFeedbackEnabled.collect { enabled ->
                _crashReportsEnabled.value = enabled
            }
        }
    }
    
    fun setAnalyticsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _analyticsEnabled.value = enabled
            preferencesManager.updateAnimationsEnabled(enabled)
        }
    }
    
    fun setCrashReportsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _crashReportsEnabled.value = enabled
            preferencesManager.updateHapticFeedbackEnabled(enabled)
        }
    }
    
    fun setDataCollectionEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _dataCollectionEnabled.value = enabled
            // Using existing preference for now
        }
    }
}

