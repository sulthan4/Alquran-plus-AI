package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.Juz
import com.alquranplusai.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JuzListViewModel(
    private val quranRepository: QuranRepository
) : ViewModel() {
    
    private val _juzList = MutableStateFlow<List<Juz>>(emptyList())
    val juzList: StateFlow<List<Juz>> = _juzList.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _completedJuz = MutableStateFlow<List<Int>>(emptyList())
    val completedJuz: StateFlow<List<Int>> = _completedJuz.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadJuzList()
        loadProgress()
    }
    
    private fun loadJuzList() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                quranRepository.getAllJuz().collect { juzzes ->
                    _juzList.value = juzzes
                }
            } catch (e: Exception) {
                _error.value = "Failed to load Juz list: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun loadProgress() {
        viewModelScope.launch {
            try {
                quranRepository.getCompletedJuz().collect { completed ->
                    _completedJuz.value = completed
                }
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
}
