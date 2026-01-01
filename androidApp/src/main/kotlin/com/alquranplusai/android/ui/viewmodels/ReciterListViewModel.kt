package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReciterListViewModel(
    private val audioRepository: com.alquranplusai.domain.repositories.AudioRepository
) : ViewModel() {
    
    private val _reciters = MutableStateFlow<List<com.alquranplusai.domain.models.Reciter>>(emptyList())
    val reciters: StateFlow<List<com.alquranplusai.domain.models.Reciter>> = _reciters.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _selectedReciter = MutableStateFlow<com.alquranplusai.domain.models.Reciter?>(null)
    val selectedReciter: StateFlow<com.alquranplusai.domain.models.Reciter?> = _selectedReciter.asStateFlow()
    
    init {
        loadReciters()
    }
    
    private fun loadReciters() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                audioRepository.getAllReciters().collect { reciterList ->
                    _reciters.value = reciterList
                }
            } catch (e: Exception) {
                // TODO: Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun selectReciter(reciter: com.alquranplusai.domain.models.Reciter) {
        _selectedReciter.value = reciter
    }
}

