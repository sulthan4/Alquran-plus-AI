package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.Tafsir
import com.alquranplusai.domain.models.TafsirText
import com.alquranplusai.domain.models.TafsirMetadata
import com.alquranplusai.domain.repositories.TafsirRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for managing Tafsir (Quranic commentary) data
 */
class TafsirViewModel(
    private val tafsirRepository: TafsirRepository
) : ViewModel() {

    // All available tafsirs
    private val _allTafsirs = MutableStateFlow<List<Tafsir>>(emptyList())
    val allTafsirs: StateFlow<List<Tafsir>> = _allTafsirs.asStateFlow()

    // Downloaded tafsirs only
    private val _downloadedTafsirs = MutableStateFlow<List<Tafsir>>(emptyList())
    val downloadedTafsirs: StateFlow<List<Tafsir>> = _downloadedTafsirs.asStateFlow()

    // User's preferred tafsirs
    private val _preferredTafsirs = MutableStateFlow<List<String>>(emptyList())
    val preferredTafsirs: StateFlow<List<String>> = _preferredTafsirs.asStateFlow()

    // Current tafsir text being viewed
    private val _currentTafsirTexts = MutableStateFlow<List<TafsirText>>(emptyList())
    val currentTafsirTexts: StateFlow<List<TafsirText>> = _currentTafsirTexts.asStateFlow()

    // Download progress
    private val _downloadProgress = MutableStateFlow<Map<String, Float>>(emptyMap())
    val downloadProgress: StateFlow<Map<String, Float>> = _downloadProgress.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadAllTafsirs()
        loadDownloadedTafsirs()
        loadPreferredTafsirs()
    }

    /**
     * Load all available tafsirs
     */
    fun loadAllTafsirs() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                tafsirRepository.getAllTafsirs().collect { tafsirs ->
                    _allTafsirs.value = tafsirs
                }
            } catch (e: Exception) {
                _error.value = "Failed to load tafsirs: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Load downloaded tafsirs
     */
    fun loadDownloadedTafsirs() {
        viewModelScope.launch {
            try {
                tafsirRepository.getDownloadedTafsirs().collect { tafsirs ->
                    _downloadedTafsirs.value = tafsirs
                }
            } catch (e: Exception) {
                _error.value = "Failed to load downloaded tafsirs: ${e.message}"
            }
        }
    }

    /**
     * Load user's preferred tafsirs
     */
    fun loadPreferredTafsirs() {
        viewModelScope.launch {
            try {
                tafsirRepository.getPreferredTafsirs().collect { ids ->
                    _preferredTafsirs.value = ids
                }
            } catch (e: Exception) {
                _error.value = "Failed to load preferred tafsirs: ${e.message}"
            }
        }
    }


    /**
     * Load tafsir for a specific ayah
     */
    fun loadTafsirForAyah(surahNumber: Int, ayahNumber: Int, tafsirIds: List<String> = _preferredTafsirs.value) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // If no preferred tafsirs, use all downloaded tafsirs
                val idsToUse = if (tafsirIds.isEmpty()) {
                    _downloadedTafsirs.value.map { it.id }
                } else {
                    tafsirIds
                }
                
                val texts = mutableListOf<TafsirText>()
                idsToUse.forEach { tafsirId ->
                    tafsirRepository.getTafsirForAyah(tafsirId, surahNumber, ayahNumber).collect { text ->
                        text?.let { texts.add(it) }
                    }
                }
                _currentTafsirTexts.value = texts
            } catch (e: Exception) {
                _error.value = "Failed to load tafsir: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Load tafsir for a range of ayahs
     */
    fun loadTafsirForAyahRange(
        surahNumber: Int,
        fromAyah: Int,
        toAyah: Int,
        tafsirId: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                tafsirRepository.getTafsirForAyahRange(tafsirId, surahNumber, fromAyah, toAyah).collect { texts ->
                    _currentTafsirTexts.value = texts
                }
            } catch (e: Exception) {
                _error.value = "Failed to load tafsir range: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Download a tafsir
     */
    fun downloadTafsir(tafsirId: String) {
        viewModelScope.launch {
            try {
                tafsirRepository.downloadTafsir(tafsirId).collect { progress ->
                    if (progress >= 0) {
                        _downloadProgress.value = _downloadProgress.value + (tafsirId to progress)
                        
                        // Refresh downloaded tafsirs when complete
                        if (progress >= 1.0f) {
                            loadDownloadedTafsirs()
                            loadAllTafsirs()
                        }
                    } else {
                        _error.value = "Failed to download tafsir: $tafsirId"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Download error: ${e.message}"
            }
        }
    }

    /**
     * Delete a downloaded tafsir
     */
    fun deleteTafsir(tafsirId: String) {
        viewModelScope.launch {
            try {
                val success = tafsirRepository.deleteTafsir(tafsirId)
                if (success) {
                    loadDownloadedTafsirs()
                    loadAllTafsirs()
                } else {
                    _error.value = "Failed to delete tafsir: $tafsirId"
                }
            } catch (e: Exception) {
                _error.value = "Delete error: ${e.message}"
            }
        }
    }

    /**
     * Set user's preferred tafsirs
     */
    fun setPreferredTafsirs(tafsirIds: List<String>) {
        viewModelScope.launch {
            try {
                tafsirRepository.setPreferredTafsirs(tafsirIds)
                _preferredTafsirs.value = tafsirIds
            } catch (e: Exception) {
                _error.value = "Failed to save preferences: ${e.message}"
            }
        }
    }

    /**
     * Toggle a tafsir in preferred list
     */
    fun togglePreferredTafsir(tafsirId: String) {
        val current = _preferredTafsirs.value.toMutableList()
        if (current.contains(tafsirId)) {
            current.remove(tafsirId)
        } else {
            current.add(tafsirId)
        }
        setPreferredTafsirs(current)
    }

    /**
     * Search within tafsir
     */
    fun searchTafsir(tafsirId: String, query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                tafsirRepository.searchTafsir(tafsirId, query).collect { results ->
                    _currentTafsirTexts.value = results
                }
            } catch (e: Exception) {
                _error.value = "Search error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Get metadata for a tafsir
     */
    fun getTafsirMetadata(tafsirId: String): Flow<TafsirMetadata?> {
        return flow {
            tafsirRepository.getTafsirMetadata(tafsirId).collect { metadata ->
                emit(metadata)
            }
        }
    }
    
    fun loadTafsirForSurah(surahNumber: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val downloadedIds = _downloadedTafsirs.value.map { it.id }
                if (downloadedIds.isEmpty()) {
                    _currentTafsirTexts.value = emptyList()
                    return@launch
                }
                
                // Load tafsir from all downloaded sources
                val allTexts = mutableListOf<TafsirText>()
                downloadedIds.forEach { tafsirId ->
                    tafsirRepository.getTafsirForSurah(
                        tafsirId = tafsirId,
                        surahNumber = surahNumber
                    ).collect { texts ->
                        allTexts.addAll(texts)
                    }
                }
                _currentTafsirTexts.value = allTexts
            } catch (e: Exception) {
                _error.value = "Failed to load tafsir for surah: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
