package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.repositories.AnalyticsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnalyticsViewModel(
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {
    
    private val _totalReadingTime = MutableStateFlow(0L)
    val totalReadingTime: StateFlow<Long> = _totalReadingTime.asStateFlow()
    
    private val _currentStreak = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()
    
    private val _longestStreak = MutableStateFlow(0)
    val longestStreak: StateFlow<Int> = _longestStreak.asStateFlow()
    
    private val _completedSurahs = MutableStateFlow(0)
    val completedSurahs: StateFlow<Int> = _completedSurahs.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _weeklyReadingData = MutableStateFlow<List<Long>>(emptyList())
    val weeklyReadingData: StateFlow<List<Long>> = _weeklyReadingData.asStateFlow()
    
    private val _readingSessions = MutableStateFlow<List<com.alquranplusai.domain.models.ReadingSession>>(emptyList())
    val readingSessions: StateFlow<List<com.alquranplusai.domain.models.ReadingSession>> = _readingSessions.asStateFlow()
    
    init {
        loadAnalytics()
    }
    
    private fun loadAnalytics() {
        val userId = "user_1"
        viewModelScope.launch {
            _isLoading.value = true
            try {
                try {
                    analyticsRepository.getTotalReadingTime(userId).collect { time ->
                        _totalReadingTime.value = time
                    }
                } catch (e: Exception) {
                    // Ignore
                }
                
                try {
                    analyticsRepository.getCurrentStreak(userId).collect { streak ->
                        _currentStreak.value = streak
                    }
                } catch (e: Exception) {
                    // Ignore
                }
                
                try {
                    analyticsRepository.getLongestStreak(userId).collect { streak ->
                        _longestStreak.value = streak
                    }
                } catch (e: Exception) {
                    // Ignore
                }
                
                try {
                    // Load last 7 days of reading time
                    analyticsRepository.getReadingTimeChart(userId, com.alquranplusai.domain.models.AnalyticsTimeRange.WEEK).collect { points ->
                        // Map ChartDataPoint to list of values (y-axis)
                        _weeklyReadingData.value = points.map { it.value.toLong() }
                    }
                } catch (e: Exception) {
                    println("Error loading chart data: ${e.message}")
                }
                
                try {
                     analyticsRepository.getUserReadingSessions(userId, 20).collect { sessions ->
                         _readingSessions.value = sessions
                     }
                } catch (e: Exception) {
                     // Ignore
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun refresh() {
        loadAnalytics()
    }
}
