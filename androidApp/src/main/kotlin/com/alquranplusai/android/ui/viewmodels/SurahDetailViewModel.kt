package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.Surah
import com.alquranplusai.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SurahDetailViewModel(
    private val quranRepository: QuranRepository
) : ViewModel() {
    
    private val _surah = MutableStateFlow<Surah?>(null)
    val surah: StateFlow<Surah?> = _surah.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun loadSurah(surahNumber: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                quranRepository.getSurahByNumber(surahNumber).collect { surahData ->
                    _surah.value = surahData
                }
                
                quranRepository.getCompletedSurahs().collect { completed ->
                    _isCompleted.value = completed.contains(surahNumber)
                }
            } catch (e: Exception) {
                _error.value = "Failed to load Surah details: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun markAsCompleted(surahNumber: Int) {
        viewModelScope.launch {
            try {
                quranRepository.markSurahAsCompleted(surahNumber)
            } catch (e: Exception) {
                _error.value = "Failed to mark Surah as completed: ${e.message}"
                e.printStackTrace()
            }
        }
    }
}
