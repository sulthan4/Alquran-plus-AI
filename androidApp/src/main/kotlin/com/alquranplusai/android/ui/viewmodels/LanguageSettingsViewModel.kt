package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * ViewModel for LanguageSettingsViewModel
 */
class LanguageSettingsViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _currentLanguage = MutableStateFlow("en")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        viewModelScope.launch {
            preferencesManager.language.collect { language ->
                _currentLanguage.value = language
            }
        }
    }
    
    fun setLanguage(language: String) {
        viewModelScope.launch {
            preferencesManager.updateLanguage(language)
        }
    }
}
