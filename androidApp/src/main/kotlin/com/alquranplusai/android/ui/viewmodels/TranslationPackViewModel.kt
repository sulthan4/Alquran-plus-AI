package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.*
import com.alquranplusai.domain.repositories.TranslationRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for managing translation packs
 */
class TranslationPackViewModel(
    private val translationRepository: TranslationRepository
) : ViewModel() {
    
    private val _translationPacks = MutableStateFlow<List<TranslationPack>>(emptyList())
    val translationPacks: StateFlow<List<TranslationPack>> = _translationPacks.asStateFlow()
    
    private val _selectedPacks = MutableStateFlow<Set<String>>(emptySet())
    val selectedPacks: StateFlow<Set<String>> = _selectedPacks.asStateFlow()
    
    private val _displayMode = MutableStateFlow(TranslationDisplayMode.SINGLE)
    val displayMode: StateFlow<TranslationDisplayMode> = _displayMode.asStateFlow()
    
    private val _downloadProgress = MutableStateFlow<Map<String, Float>>(emptyMap())
    val downloadProgress: StateFlow<Map<String, Float>> = _downloadProgress.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadTranslationPacks()
        loadSelectedPacks()
    }
    
    fun loadTranslationPacks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                translationRepository.getAllTranslations().collect { translations ->
                    // For now, treat each translation as its own "pack"
                    // In a real implementation, you'd group them properly
                    _translationPacks.value = emptyList() // Placeholder
                }
            } catch (e: Exception) {
                _error.value = "Failed to load translation packs: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun loadSelectedPacks() {
        viewModelScope.launch {
            try {
                translationRepository.getDownloadedTranslations().collect { translations ->
                    _selectedPacks.value = translations.map { it.id }.toSet()
                }
            } catch (e: Exception) {
                _error.value = "Failed to load selected packs: ${e.message}"
            }
        }
    }
    
    fun togglePackSelection(packId: String) {
        val current = _selectedPacks.value.toMutableSet()
        if (current.contains(packId)) {
            current.remove(packId)
        } else {
            current.add(packId)
        }
        _selectedPacks.value = current
    }
    
    fun setDisplayMode(mode: TranslationDisplayMode) {
        _displayMode.value = mode
    }
    
    fun downloadPack(packId: String) {
        viewModelScope.launch {
            try {
                translationRepository.downloadTranslation(packId).collect { progress ->
                    val currentProgress = _downloadProgress.value.toMutableMap()
                    currentProgress[packId] = progress.progress
                    _downloadProgress.value = currentProgress
                }
                
                loadTranslationPacks()
            } catch (e: Exception) {
                _error.value = "Failed to download pack: ${e.message}"
            }
        }
    }
    
    fun deletePack(packId: String) {
        viewModelScope.launch {
            try {
                translationRepository.deleteTranslation(packId)
                loadTranslationPacks()
            } catch (e: Exception) {
                _error.value = "Failed to delete pack: ${e.message}"
            }
        }
    }
    
    fun compareTranslations(ayahId: String): Flow<List<Pair<Translation, String>>> {
        return flow {
            val selectedTranslationIds = _selectedPacks.value.toList()
            val comparisons = mutableListOf<Pair<Translation, String>>()
            
            selectedTranslationIds.forEach { translationId ->
                // This would fetch the actual translation text for the ayah
                // For now, using placeholder
                val translation = Translation(
                    id = translationId,
                    name = "Translation $translationId",
                    author = "Author",
                    language = "English",
                    languageCode = "en"
                )
                comparisons.add(translation to "Translation text for $ayahId")
            }
            
            emit(comparisons)
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
