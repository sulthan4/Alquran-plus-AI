package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GoalsViewModel(
    private val analyticsRepository: com.alquranplusai.domain.repositories.AnalyticsRepository
) : ViewModel() {
    
    private val _goals = MutableStateFlow<List<com.alquranplusai.domain.models.Goal>>(emptyList())
    val goals: StateFlow<List<com.alquranplusai.domain.models.Goal>> = _goals.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _selectedGoal = MutableStateFlow<com.alquranplusai.domain.models.Goal?>(null)
    val selectedGoal: StateFlow<com.alquranplusai.domain.models.Goal?> = _selectedGoal.asStateFlow()
    
    init {
        loadGoals()
    }
    
    private fun loadGoals() {
        val userId = "user_1"
        viewModelScope.launch {
            _isLoading.value = true
            try {
                analyticsRepository.getUserGoals(userId).collect { goalList ->
                    _goals.value = goalList
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load goals"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun createGoal(title: String, target: Int, type: String) {
        viewModelScope.launch {
            try {
                val goalType = com.alquranplusai.domain.models.GoalType.valueOf(type)
                val goal = com.alquranplusai.domain.models.Goal(
                    id = "goal_${System.currentTimeMillis()}",
                    type = goalType,
                    title = title,
                    target = target,
                    current = 0,
                    startDate = System.currentTimeMillis(),
                    endDate = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000)
                )
                analyticsRepository.createGoal("user_1", goal)
                loadGoals()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to create goal"
            }
        }
    }
    
    fun updateGoalProgress(goalId: String, progress: Int) {
        viewModelScope.launch {
            try {
                analyticsRepository.updateGoalProgress("user_1", goalId, progress)
                loadGoals()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to update progress"
            }
        }
    }
    
    fun deleteGoal(goalId: String) {
        viewModelScope.launch {
            try {
                analyticsRepository.deleteGoal(goalId)
                loadGoals()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to delete goal"
            }
        }
    }
    
    fun calculateProgress(goal: com.alquranplusai.domain.models.Goal): Float {
        return if (goal.target > 0) (goal.current.toFloat() / goal.target) * 100 else 0f
    }
    
    fun selectGoal(goal: com.alquranplusai.domain.models.Goal) {
        _selectedGoal.value = goal
    }
    
    fun clearError() {
        _error.value = null
    }
}

