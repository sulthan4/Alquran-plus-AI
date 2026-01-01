package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReadingPreferencesViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _fontSize = MutableStateFlow(24f)
    val fontSize: StateFlow<Float> = _fontSize.asStateFlow()
    
    private val _readingMode = MutableStateFlow("CONTINUOUS")
    val readingMode: StateFlow<String> = _readingMode.asStateFlow()

    private val _showWordByWord = MutableStateFlow(false)
    val showWordByWord: StateFlow<Boolean> = _showWordByWord.asStateFlow()
    
    init {
        viewModelScope.launch {
            preferencesManager.fontSize.collect { size ->
                _fontSize.value = size.toFloat()
            }
        }
        viewModelScope.launch {
            preferencesManager.readingMode.collect { _readingMode.value = it }
        }
        viewModelScope.launch {
            preferencesManager.showWordByWord.collect { _showWordByWord.value = it }
        }
    }
    
    fun setFontSize(size: Int) {
        viewModelScope.launch {
            preferencesManager.updateFontSize(size)
        }
    }

    fun setReadingMode(mode: String) {
        viewModelScope.launch {
            preferencesManager.updateReadingMode(mode)
        }
    }

    fun setShowWordByWord(show: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateShowWordByWord(show)
        }
    }
}

