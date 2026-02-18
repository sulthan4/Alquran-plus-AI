package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val theme: String = "SYSTEM",
    val language: String = "en",
    val fontSize: Int = 24,
    val readingMode: String = "CONTINUOUS",
    val showWordByWord: Boolean = true,
    val autoPlay: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val languageDisplayName: String
        get() = com.alquranplusai.domain.models.SupportedLanguages.getByCode(language)?.name ?: language
    
    val readingSummary: String
        get() = "Font: ${fontSize}sp, Mode: ${readingMode.lowercase()}"
    
    val audioSummary: String
        get() = "${if (autoPlay) "Auto-play: On" else "Auto-play: Off"}"
}

class SettingsViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            preferencesManager.theme.collect { theme ->
                _uiState.update { it.copy(theme = theme) }
            }
        }
        viewModelScope.launch {
            preferencesManager.language.collect { language ->
                _uiState.update { it.copy(language = language) }
            }
        }
        viewModelScope.launch {
            preferencesManager.fontSize.collect { size ->
                _uiState.update { it.copy(fontSize = size) }
            }
        }
        viewModelScope.launch {
            preferencesManager.readingMode.collect { mode ->
                _uiState.update { it.copy(readingMode = mode) }
            }
        }
        viewModelScope.launch {
            preferencesManager.showWordByWord.collect { show ->
                _uiState.update { it.copy(showWordByWord = show) }
            }
        }
        viewModelScope.launch {
            preferencesManager.autoPlay.collect { enabled ->
                _uiState.update { it.copy(autoPlay = enabled) }
            }
        }
        viewModelScope.launch {
            preferencesManager.notificationsEnabled.collect { enabled ->
                _uiState.update { it.copy(notificationsEnabled = enabled) }
            }
        }
    }
    
    fun updateTheme(theme: String) {
        viewModelScope.launch {
            try {
                preferencesManager.updateTheme(theme)
                _uiState.update { it.copy(theme = theme) }
            } catch (e: Exception) {
                // handle error
            }
        }
    }
    
    fun updateFontSize(size: Int) {
        viewModelScope.launch {
            preferencesManager.updateFontSize(size)
             _uiState.update { it.copy(fontSize = size) }
        }
    }

    fun updateAutoPlay(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateAutoPlay(enabled)
            // No need to update state manually if we observe flow, but uiState doesn't have autoPlay yet.
            // I should update uiState class too.
        }
    }

    fun updateNotificationsEnabled(enabled: Boolean) {
         viewModelScope.launch {
             preferencesManager.updateNotificationsEnabled(enabled)
         }
    }
}
