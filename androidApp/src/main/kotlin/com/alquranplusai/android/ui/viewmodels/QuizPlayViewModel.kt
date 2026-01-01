package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.Question
import com.alquranplusai.domain.models.Quiz
import com.alquranplusai.domain.models.QuizResult
import com.alquranplusai.domain.repositories.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class QuizPlayViewModel(
    private val quizRepository: QuizRepository
) : ViewModel() {
    
    private val _quiz = MutableStateFlow<Quiz?>(null)
    val quiz: StateFlow<Quiz?> = _quiz.asStateFlow()
    
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()
    
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()
    
    private val _selectedAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val selectedAnswers: StateFlow<Map<Int, Int>> = _selectedAnswers.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _submissionResult = MutableStateFlow<QuizResult?>(null)
    val submissionResult: StateFlow<QuizResult?> = _submissionResult.asStateFlow()

    private var sessionId: String? = null
    
    private val _timeRemaining = MutableStateFlow<Long?>(null)
    val timeRemaining: StateFlow<Long?> = _timeRemaining.asStateFlow()
    
    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()
    
    private val _timeElapsed = MutableStateFlow(0L)
    val timeElapsed: StateFlow<Long> = _timeElapsed.asStateFlow()
    
    private var startTime: Long = 0
    private var pausedTime: Long = 0
    
    fun loadQuiz(quizId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val quizData = quizRepository.getQuizById(quizId).first()
                _quiz.value = quizData
                
                val questionList = quizRepository.getQuizQuestions(quizId).first()
                _questions.value = questionList
                
                // Start session
                sessionId = quizRepository.startQuizSession(quizId).first()
                
                // Start timer if quiz has time limit
                quizData?.timeLimit?.let { limit ->
                    _timeRemaining.value = limit.toLong() * 60 * 1000 // Convert minutes to ms
                    startTimer()
                } ?: run {
                    startTime = System.currentTimeMillis()
                }
                
            } catch (e: Exception) {
                // Only handle error if strictly needed, otherwise UI stays loading or empty
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun selectAnswer(questionIndex: Int, answerIndex: Int) {
        val current = _selectedAnswers.value.toMutableMap()
        current[questionIndex] = answerIndex
        _selectedAnswers.value = current
        
        // Submit to backend
        viewModelScope.launch {
            val question = _questions.value.getOrNull(questionIndex)
            val answerText = question?.options?.getOrNull(answerIndex)
            
            if (question != null && answerText != null && sessionId != null) {
                quizRepository.submitAnswer(sessionId!!, question.id, answerText)
            }
        }
    }
    
    fun nextQuestion() {
        if (_currentQuestionIndex.value < _questions.value.size - 1) {
            _currentQuestionIndex.value++
        }
    }
    
    fun previousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value--
        }
    }
    
    fun pauseQuiz() {
        _isPaused.value = true
        pausedTime = System.currentTimeMillis()
    }
    
    fun resumeQuiz() {
        _isPaused.value = false
        if (pausedTime > 0) {
            startTime += (System.currentTimeMillis() - pausedTime)
            pausedTime = 0
        }
    }
    
    private fun startTimer() {
        viewModelScope.launch {
            while (_timeRemaining.value != null && _timeRemaining.value!! > 0) {
                kotlinx.coroutines.delay(1000)
                
                if (!_isPaused.value) {
                    val remaining = _timeRemaining.value ?: 0L
                    if (remaining > 0) {
                        _timeRemaining.value = remaining - 1000
                        _timeElapsed.value = System.currentTimeMillis() - startTime
                    }

                    if (_timeRemaining.value!! <= 0) {
                        // Auto-submit when time runs out
                        submitQuiz()
                        break
                    }
                }
            }
        }
    }
    
    fun getProgress(): Float {
        val total = _questions.value.size
        val answered = _selectedAnswers.value.size
        return if (total > 0) answered.toFloat() / total else 0f
    }
    
    fun submitQuiz() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (sessionId != null) {
                    val result = quizRepository.endQuizSession(sessionId!!).first()
                    _submissionResult.value = result
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
