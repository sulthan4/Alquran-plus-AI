package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PageViewModel(
    private val quranRepository: QuranRepository
) : ViewModel() {
    
    private val _pageNumber = MutableStateFlow(1)
    val pageNumber: StateFlow<Int> = _pageNumber.asStateFlow()
    
    private val _ayahs = MutableStateFlow<List<com.alquranplusai.domain.models.Ayah>>(emptyList())
    val ayahs: StateFlow<List<com.alquranplusai.domain.models.Ayah>> = _ayahs.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun loadPage(pageNumber: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _pageNumber.value = pageNumber
            try {
                quranRepository.getAyahsByPage(pageNumber).collect { ayahList ->
                    _ayahs.value = ayahList
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun nextPage() {
        if (_pageNumber.value < 604) {
            loadPage(_pageNumber.value + 1)
        }
    }
    
    fun previousPage() {
        if (_pageNumber.value > 1) {
            loadPage(_pageNumber.value - 1)
        }
    }
    
    fun goToSurahPage(surahNumber: Int) {
        val pageNumber = calculateSurahStartPage(surahNumber)
        loadPage(pageNumber)
    }
    
    private fun calculateSurahStartPage(surahNumber: Int): Int {
        val surahPages = mapOf(
            1 to 1, 2 to 2, 3 to 50, 4 to 77, 5 to 106,
            6 to 128, 7 to 151, 8 to 177, 9 to 187, 10 to 208
        )
        return surahPages[surahNumber] ?: 1
    }
}

