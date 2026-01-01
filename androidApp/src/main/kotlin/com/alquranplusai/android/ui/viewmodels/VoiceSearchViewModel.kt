package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VoiceSearchViewModel(
    private val searchRepository: com.alquranplusai.domain.repositories.SearchRepository,
    private val speechRecognitionEngine: com.alquranplusai.data.ai.SpeechRecognitionEngine
) : ViewModel() {
    
    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()
    
    private val _transcription = MutableStateFlow("")
    val transcription: StateFlow<String> = _transcription.asStateFlow()
    
    private val _searchResults = MutableStateFlow<List<com.alquranplusai.domain.models.SearchResult>>(emptyList())
    val searchResults: StateFlow<List<com.alquranplusai.domain.models.SearchResult>> = _searchResults.asStateFlow()
    
    fun startListening() {
        if (!speechRecognitionEngine.isAvailable()) {
            _transcription.value = "Voice recognition not available on this device"
            return
        }

        viewModelScope.launch {
            speechRecognitionEngine.startListening().collect { result ->
                when (result) {
                    is com.alquranplusai.data.ai.SpeechRecognitionEngine.SpeechRecognitionResult.Ready -> {
                        _isListening.value = true
                        _transcription.value = "Listening..."
                    }
                    is com.alquranplusai.data.ai.SpeechRecognitionEngine.SpeechRecognitionResult.Speaking -> {
                         _transcription.value = "Speaking..."
                    }
                    is com.alquranplusai.data.ai.SpeechRecognitionEngine.SpeechRecognitionResult.PartialResult -> {
                        _transcription.value = result.text
                    }
                    is com.alquranplusai.data.ai.SpeechRecognitionEngine.SpeechRecognitionResult.Success -> {
                        val text = result.results.firstOrNull()?.text ?: ""
                        _transcription.value = text
                        _isListening.value = false
                        if (text.isNotEmpty()) {
                            searchWithVoice(text)
                        }
                    }
                    is com.alquranplusai.data.ai.SpeechRecognitionEngine.SpeechRecognitionResult.Error -> {
                        _transcription.value = "Error: ${result.message}"
                        _isListening.value = false
                    }
                    is com.alquranplusai.data.ai.SpeechRecognitionEngine.SpeechRecognitionResult.EndOfSpeech -> {
                        // Wait for results
                    }
                    else -> {}
                }
            }
        }
    }
    
    fun stopListening() {
        speechRecognitionEngine.stopListening()
        _isListening.value = false
    }
    
    fun searchWithVoice(query: String) {
        viewModelScope.launch {
            try {
                searchRepository.searchInQuran(query).collect { results ->
                    _searchResults.value = results
                }
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
}

