package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JuzViewModel(
    private val quranRepository: QuranRepository
) : ViewModel() {
    
    private val _juzNumber = MutableStateFlow(1)
    val juzNumber: StateFlow<Int> = _juzNumber.asStateFlow()
    
    private val _ayahs = MutableStateFlow<List<com.alquranplusai.domain.models.Ayah>>(emptyList())
    val ayahs: StateFlow<List<com.alquranplusai.domain.models.Ayah>> = _ayahs.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun loadJuz(juzNumber: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _juzNumber.value = juzNumber
            try {
                quranRepository.getAyahsByJuz(juzNumber).collect { ayahList ->
                    _ayahs.value = ayahList
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

