package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StreakViewModel(
    private val analyticsRepository: com.alquranplusai.domain.repositories.AnalyticsRepository
) : ViewModel() {
    
    private val _currentStreak = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()
    
    private val _longestStreak = MutableStateFlow(0)
    val longestStreak: StateFlow<Int> = _longestStreak.asStateFlow()
    
    private val _streakHistory = MutableStateFlow<List<Boolean>>(emptyList())
    val streakHistory: StateFlow<List<Boolean>> = _streakHistory.asStateFlow()
    
    private val _freezeDaysRemaining = MutableStateFlow(0)
    val freezeDaysRemaining: StateFlow<Int> = _freezeDaysRemaining.asStateFlow()
    
    private val _lastActivityDate = MutableStateFlow<String?>(null)
    val lastActivityDate: StateFlow<String?> = _lastActivityDate.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadStreak()
    }
    
    private fun loadStreak() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                analyticsRepository.getCurrentStreak("user_1").collect { streak ->
                    _currentStreak.value = streak
                }
                analyticsRepository.getLongestStreak("user_1").collect { streak ->
                    _longestStreak.value = streak
                }
                
                // Load last 30 days of activity
                loadStreakHistory()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun loadStreakHistory() {
        viewModelScope.launch {
            try {
                // Generate last 30 days history
                val history = mutableListOf<Boolean>()
                for (i in 0 until 30) {
                    // TODO: Get actual activity data from repository
                    history.add(i < _currentStreak.value)
                }
                _streakHistory.value = history.reversed()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun calculateStreakPercentage(): Float {
        return if (_longestStreak.value > 0) {
            (_currentStreak.value.toFloat() / _longestStreak.value) * 100
        } else 0f
    }
    
    fun useFreezeDay() {
        if (_freezeDaysRemaining.value > 0) {
            _freezeDaysRemaining.value--
            // Maintain current streak
        }
    }
    
    fun refresh() {
        loadStreak()
    }
}

