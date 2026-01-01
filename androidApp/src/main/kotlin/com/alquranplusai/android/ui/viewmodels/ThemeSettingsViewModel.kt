package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThemeSettingsViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _theme = MutableStateFlow("SYSTEM")
    val theme: StateFlow<String> = _theme.asStateFlow()
    
    private val _fontSize = MutableStateFlow(24)
    val fontSize: StateFlow<Int> = _fontSize.asStateFlow()
    
    init {
        loadPreferences()
    }
    
    private fun loadPreferences() {
        viewModelScope.launch {
            preferencesManager.theme.collect { theme ->
                _theme.value = theme
            }
        }
        viewModelScope.launch {
            preferencesManager.fontSize.collect { size ->
                _fontSize.value = size
            }
        }
    }
    
    fun setTheme(theme: String) {
        viewModelScope.launch {
            preferencesManager.updateTheme(theme)
            _theme.value = theme
        }
    }
    
    fun setFontSize(size: Int) {
        viewModelScope.launch {
            preferencesManager.updateFontSize(size)
            _fontSize.value = size
        }
    }
}

