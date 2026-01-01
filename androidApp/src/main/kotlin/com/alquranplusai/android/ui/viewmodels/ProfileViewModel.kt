package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.User
import com.alquranplusai.domain.models.Achievement
import com.alquranplusai.domain.models.Goal
import com.alquranplusai.domain.repositories.UserRepository
import com.alquranplusai.domain.repositories.AnalyticsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {
    
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()
    
    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()
    
    private val _goals = MutableStateFlow<List<Goal>>(emptyList())
    val goals: StateFlow<List<Goal>> = _goals.asStateFlow()
    
    private val _totalReadingTime = MutableStateFlow(0L)
    val totalReadingTime: StateFlow<Long> = _totalReadingTime.asStateFlow()
    
    private val _currentStreak = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()
    
    private val _completedSurahs = MutableStateFlow(0)
    val completedSurahs: StateFlow<Int> = _completedSurahs.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadProfileData()
    }
    
    private fun loadProfileData() {
        val userId = "user_1"
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Load user data
                userRepository.getCurrentUser().collect { currentUser ->
                    _user.value = currentUser
                }
                
                // Load achievements
                analyticsRepository.getUserAchievements(userId).collect { achievementList ->
                    _achievements.value = achievementList
                }
                
                // Load goals
                analyticsRepository.getUserGoals(userId).collect { goalList ->
                    _goals.value = goalList
                }
                
                // Load statistics
                analyticsRepository.getTotalReadingTime(userId).collect { time ->
                    _totalReadingTime.value = time
                }
                
                analyticsRepository.getCurrentStreak(userId).collect { streak ->
                    _currentStreak.value = streak
                }
            } catch (e: Exception) {
                // TODO: Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateProfile(name: String, email: String) {
        viewModelScope.launch {
            try {
                val currentUser = _user.value
                // For now, assuming direct update isn't available, we just reload
                // In real app, we would map User to UserProfile and call updateProfile
                // userRepository.updateProfile(UserProfile(name, email, ...))
                loadProfileData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun createGoal(description: String, targetValue: Int) {
        viewModelScope.launch {
            try {
                val goal = com.alquranplusai.domain.models.Goal(
                    id = "goal_${System.currentTimeMillis()}",
                    type = com.alquranplusai.domain.models.GoalType.CUSTOM,
                    title = description,
                    target = targetValue,
                    current = 0,
                    startDate = System.currentTimeMillis(),
                    endDate = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000)
                )
                analyticsRepository.createGoal("user_1", goal)
                loadProfileData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun deleteGoal(goalId: String) {
        viewModelScope.launch {
            try {
                analyticsRepository.deleteGoal(goalId)
                loadProfileData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
