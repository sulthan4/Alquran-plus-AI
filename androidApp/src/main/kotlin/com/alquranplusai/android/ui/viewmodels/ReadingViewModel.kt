package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.*
import com.alquranplusai.domain.repositories.QuranRepository
import com.alquranplusai.domain.repositories.TranslationRepository
import com.alquranplusai.domain.repositories.BookmarkRepository
import com.alquranplusai.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ReadingViewModel(
    private val quranRepository: QuranRepository,
    private val translationRepository: TranslationRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _ayahs = MutableStateFlow<List<Ayah>>(emptyList())
    val ayahs: StateFlow<List<Ayah>> = _ayahs.asStateFlow()
    
    private val _selectedTranslations = MutableStateFlow<List<String>>(listOf("131")) // Default: Clear Quran
    val selectedTranslations: StateFlow<List<String>> = _selectedTranslations.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _fontSize = MutableStateFlow(18)
    val fontSize: StateFlow<Int> = _fontSize.asStateFlow()
    
    private val _bookmarksMap = MutableStateFlow<Map<String, String>>(emptyMap())
    val bookmarksMap: StateFlow<Map<String, String>> = _bookmarksMap.asStateFlow()
    
    // Translation metadata map (translationId -> Translation object)
    private val _translationsMap = MutableStateFlow<Map<String, Translation>>(emptyMap())
    val translationsMap: StateFlow<Map<String, Translation>> = _translationsMap.asStateFlow()
    
    // New Preferences
    private val _readingMode = MutableStateFlow(ReadingMode.CONTINUOUS)
    val readingMode: StateFlow<ReadingMode> = _readingMode.asStateFlow()
    
    private val _isWordByWordEnabled = MutableStateFlow(true)
    val isWordByWordEnabled: StateFlow<Boolean> = _isWordByWordEnabled.asStateFlow()
    
    init {
        observeBookmarks()
        observePreferences()
        loadTranslationMetadata()
    }
    
    private fun observeBookmarks() {
        viewModelScope.launch {
            bookmarkRepository.getAllBookmarks().collect { list ->
                _bookmarksMap.value = list.associate { "${it.surahNumber}:${it.ayahNumber}" to it.id }
            }
        }
    }
    
    private fun observePreferences() {
        viewModelScope.launch {
            preferencesManager.fontSize.collect { size ->
                _fontSize.value = size
            }
        }
        
        viewModelScope.launch {
            preferencesManager.readingMode.collect { mode ->
                 _readingMode.value = try {
                     ReadingMode.valueOf(mode)
                 } catch (e: Exception) {
                     ReadingMode.CONTINUOUS
                 }
            }
        }
        
        viewModelScope.launch {
            preferencesManager.showWordByWord.collect { enabled ->
                _isWordByWordEnabled.value = enabled
            }
        }
    }
    
    fun loadAyahs(surahNumber: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                quranRepository.getAyahsBySurah(surahNumber).collect { ayahList ->
                    _ayahs.value = ayahList
                    fetchTranslationsForAyahs(surahNumber, ayahList, _selectedTranslations.value)
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchTranslationsForAyahs(surahNumber: Int, ayahs: List<Ayah>, translationIds: List<String>) {
        if (translationIds.isEmpty()) return
        
        viewModelScope.launch {
            ayahs.forEach { ayah ->
                translationRepository.getAyahTranslations(surahNumber, ayah.ayahNumber, translationIds)
                    .collect { translations ->
                        if (translations.isNotEmpty()) {
                            _ayahs.update { currentAyahs ->
                                currentAyahs.map { a ->
                                    if (a.ayahNumber == ayah.ayahNumber) {
                                        a.copy(translations = translations)
                                    } else a
                                }
                            }
                        }
                    }
            }
        }
    }
    
    private fun loadTranslationMetadata() {
        viewModelScope.launch {
            _selectedTranslations.value.forEach { translationId ->
                translationRepository.getTranslationById(translationId).collect { translation ->
                    translation?.let {
                        _translationsMap.update { current ->
                            current + (translationId to it)
                        }
                    }
                }
            }
        }
    }
    
    fun increaseFontSize() {
        val newSize = (_fontSize.value + 2).coerceAtMost(48)
        viewModelScope.launch { preferencesManager.updateFontSize(newSize) }
    }
    
    fun decreaseFontSize() {
         val newSize = (_fontSize.value - 2).coerceAtLeast(14)
         viewModelScope.launch { preferencesManager.updateFontSize(newSize) }
    }
    
    fun setFontSize(size: Int) {
        viewModelScope.launch {
            preferencesManager.updateFontSize(size)
        }
    }
    
    fun toggleBookmark(surah: Int, ayah: Int) {
        val key = "$surah:$ayah"
        val existingId = _bookmarksMap.value[key]
        viewModelScope.launch {
            try {
                if (existingId != null) {
                    bookmarkRepository.deleteBookmark(existingId)
                } else {
                    bookmarkRepository.createBookmark(surah, ayah, null, null).collect { }
                }
            } catch (e: Exception) {
                // Ignore
            }
        }
    }
    fun toggleWordByWord() {
        val newValue = !_isWordByWordEnabled.value
        viewModelScope.launch {
            preferencesManager.updateShowWordByWord(newValue)
        }
    }
}
