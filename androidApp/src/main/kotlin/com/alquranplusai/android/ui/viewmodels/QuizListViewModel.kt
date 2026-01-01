package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.*
import com.alquranplusai.domain.repositories.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Quiz List Screen
 */
class QuizListViewModel(
    private val quizRepository: QuizRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<QuizListUiState>(QuizListUiState.Loading)
    val uiState: StateFlow<QuizListUiState> = _uiState.asStateFlow()
    
    private val _dailyChallenge = MutableStateFlow<DailyChallenge?>(null)
    val dailyChallenge: StateFlow<DailyChallenge?> = _dailyChallenge.asStateFlow()
    
    init {
        loadQuizzes()
        loadDailyChallenge()
    }
    
    private fun loadQuizzes() {
        viewModelScope.launch {
            _uiState.value = QuizListUiState.Loading
            
            quizRepository.getAllQuizzes().collect { quizzes ->
                _uiState.value = QuizListUiState.Success(quizzes)
            }
        }
    }
    
    private fun loadDailyChallenge() {
        viewModelScope.launch {
            quizRepository.getDailyChallenge().collect { challenge ->
                _dailyChallenge.value = challenge
            }
        }
    }
    
    fun filterByCategory(category: QuizCategory) {
        viewModelScope.launch {
            quizRepository.getQuizzesByCategory(category).collect { quizzes ->
                _uiState.value = QuizListUiState.Success(quizzes)
            }
        }
    }
    
    fun filterByDifficulty(difficulty: QuizDifficulty) {
        viewModelScope.launch {
            quizRepository.getQuizzesByDifficulty(difficulty).collect { quizzes ->
                _uiState.value = QuizListUiState.Success(quizzes)
            }
        }
    }
}

sealed class QuizListUiState {
    object Loading : QuizListUiState()
    data class Success(val quizzes: List<Quiz>) : QuizListUiState()
    data class Error(val message: String) : QuizListUiState()
}
