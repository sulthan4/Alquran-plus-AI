package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.Quiz
import com.alquranplusai.domain.models.QuizCategory
import com.alquranplusai.domain.repositories.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel(
    private val quizRepository: QuizRepository
) : ViewModel() {
    
    private val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())
    val quizzes: StateFlow<List<Quiz>> = _quizzes.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<QuizCategory?>(null)
    val selectedCategory: StateFlow<QuizCategory?> = _selectedCategory.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _userScore = MutableStateFlow(0)
    val userScore: StateFlow<Int> = _userScore.asStateFlow()
    
    private val _completedQuizzes = MutableStateFlow<List<String>>(emptyList())
    val completedQuizzes: StateFlow<List<String>> = _completedQuizzes.asStateFlow()
    
    init {
        loadQuizzes()
        loadUserProgress()
    }
    
    private fun loadQuizzes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                quizRepository.getAllQuizzes().collect { quizList ->
                    _quizzes.value = quizList
                }
            } catch (e: Exception) {
                // TODO: Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun loadUserProgress() {
        viewModelScope.launch {
            try {
                quizRepository.getCompletedQuizzes().collect { completed ->
                    _completedQuizzes.value = completed
                }
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
    
    fun selectCategory(category: QuizCategory?) {
        _selectedCategory.value = category
        if (category != null) {
            loadQuizzesByCategory(category)
        } else {
            loadQuizzes()
        }
    }
    
    private fun loadQuizzesByCategory(category: QuizCategory) {
        viewModelScope.launch {
            try {
                quizRepository.getQuizzesByCategory(category).collect { quizList ->
                    _quizzes.value = quizList
                }
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
    
    fun startQuiz(quizId: String) {
        viewModelScope.launch {
            try {
                quizRepository.startQuizSession(quizId)
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
    
    fun submitQuizResult(quizId: String, score: Int, totalQuestions: Int) {
        viewModelScope.launch {
            try {
                quizRepository.submitQuizResult(quizId, score, totalQuestions)
                _userScore.value += score
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
}
