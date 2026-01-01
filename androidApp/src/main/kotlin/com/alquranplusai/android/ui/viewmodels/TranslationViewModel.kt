package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.Translation
import com.alquranplusai.domain.repositories.TranslationRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for translation management
 */
class TranslationViewModel(
    private val translationRepository: TranslationRepository
) : ViewModel() {
    
    private val _allTranslations = MutableStateFlow<List<Translation>>(emptyList())
    val allTranslations: StateFlow<List<Translation>> = _allTranslations.asStateFlow()
    
    private val _downloadedTranslations = MutableStateFlow<List<Translation>>(emptyList())
    val downloadedTranslations: StateFlow<List<Translation>> = _downloadedTranslations.asStateFlow()
    
    private val _selectedTranslationIds = MutableStateFlow<Set<String>>(emptySet())
    val selectedTranslationIds: StateFlow<Set<String>> = _selectedTranslationIds.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadAllTranslations()
        loadDownloadedTranslations()
        loadSelectedTranslations()
    }
    
    fun loadAllTranslations() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                translationRepository.getAllTranslations().collect { translations ->
                    _allTranslations.value = translations
                }
            } catch (e: Exception) {
                _error.value = "Failed to load translations: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadDownloadedTranslations() {
        viewModelScope.launch {
            try {
                translationRepository.getDownloadedTranslations().collect { translations ->
                    _downloadedTranslations.value = translations
                }
            } catch (e: Exception) {
                _error.value = "Failed to load downloaded translations: ${e.message}"
            }
        }
    }
    
    private fun loadSelectedTranslations() {
        // Selected translations are loaded with downloaded translations
        // This is a placeholder for future enhancement
    }
    
    fun toggleTranslationSelection(translationId: String) {
        val currentSelected = _selectedTranslationIds.value.toMutableSet()
        if (currentSelected.contains(translationId)) {
            currentSelected.remove(translationId)
        } else {
            currentSelected.add(translationId)
        }
        _selectedTranslationIds.value = currentSelected
    }
    
    fun downloadTranslation(translationId: String) {
        viewModelScope.launch {
            try {
                translationRepository.downloadTranslation(translationId).collect { progress ->
                    // Progress handled by download manager
                }
            } catch (e: Exception) {
                _error.value = "Failed to download translation: ${e.message}"
            }
        }
    }
    
    fun deleteTranslation(translationId: String) {
        viewModelScope.launch {
            try {
                translationRepository.deleteTranslation(translationId)
                loadDownloadedTranslations()
            } catch (e: Exception) {
                _error.value = "Failed to delete translation: ${e.message}"
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
