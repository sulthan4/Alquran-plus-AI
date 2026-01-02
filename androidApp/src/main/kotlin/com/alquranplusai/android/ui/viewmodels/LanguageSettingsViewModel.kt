package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.data.preferences.PreferencesManager
import com.alquranplusai.domain.models.AppLanguage
import com.alquranplusai.domain.models.SupportedLanguages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for language selection settings
 */
class LanguageSettingsViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _selectedLanguage = MutableStateFlow("en")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()
    
    private val _availableLanguages = MutableStateFlow(SupportedLanguages.ALL)
    val availableLanguages: StateFlow<List<AppLanguage>> = _availableLanguages.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadCurrentLanguage()
    }
    
    private fun loadCurrentLanguage() {
        viewModelScope.launch {
            preferencesManager.language.collect { languageCode ->
                _selectedLanguage.value = languageCode
            }
        }
    }
    
    fun selectLanguage(languageCode: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                preferencesManager.updateLanguage(languageCode)
                _selectedLanguage.value = languageCode
                
                // Note: In a real app, this would trigger:
                // 1. Update Android locale configuration
                // 2. Restart activity to apply new language
                // 3. Download language-specific resources if needed
            } catch (e: Exception) {
                // Handle error
                println("Error updating language: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun getCurrentLanguage(): AppLanguage? {
        return SupportedLanguages.getByCode(_selectedLanguage.value)
    }
    
    fun isRtlLanguage(): Boolean {
        return SupportedLanguages.isRtl(_selectedLanguage.value)
    }
}
