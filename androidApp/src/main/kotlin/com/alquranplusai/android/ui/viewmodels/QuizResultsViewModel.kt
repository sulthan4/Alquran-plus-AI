package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.repositories.QuizRepository
import com.alquranplusai.domain.models.QuizResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizResultsViewModel(
    private val quizRepository: QuizRepository
) : ViewModel() {
    
    private val _results = MutableStateFlow<List<QuizResult>>(emptyList())
    val results: StateFlow<List<QuizResult>> = _results.asStateFlow()
    
    private val _currentResult = MutableStateFlow<QuizResult?>(null)
    val currentResult: StateFlow<QuizResult?> = _currentResult.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _statistics = MutableStateFlow<QuizStatistics?>(null)
    val statistics: StateFlow<QuizStatistics?> = _statistics.asStateFlow()
    
    data class QuizStatistics(
        val totalAttempts: Int,
        val averageScore: Float,
        val bestScore: Int,
        val totalTimeSpent: Long,
        val improvementRate: Float
    )
    
    fun loadResults(quizId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                quizRepository.getQuizHistory().collect { resultList ->
                    _results.value = resultList.filter { it.quizId == quizId }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadResult(attemptId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // If the repository returns Flow<QuizResult?>
                quizRepository.getQuizResultById(attemptId).collect {
                    _currentResult.value = it
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun calculateStatistics() {
        viewModelScope.launch {
            try {
                val results = _results.value
                if (results.isNotEmpty()) {
                    val stats = QuizStatistics(
                        totalAttempts = results.size,
                        averageScore = results.map { it.score }.average().toFloat(),
                        bestScore = results.maxOfOrNull { it.score } ?: 0,
                        totalTimeSpent = results.sumOf { it.timeSpent },
                        improvementRate = calculateImprovementRate(results)
                    )
                    _statistics.value = stats
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun calculateImprovementRate(results: List<QuizResult>): Float {
        if (results.size < 2) return 0f
        val first = results.first().score
        val last = results.last().score
        return if (first > 0) ((last - first).toFloat() / first) * 100 else 0f
    }
    
    fun getAverageAccuracy(): Float {
        val results = _results.value
        return if (results.isNotEmpty()) {
            results.map { it.percentage }.average().toFloat()
        } else 0f
    }
    
    fun deleteResult(attemptId: String) {
        viewModelScope.launch {
            try {
                quizRepository.deleteQuizResult(attemptId)
                _results.value = _results.value.filter { it.attemptId != attemptId }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
