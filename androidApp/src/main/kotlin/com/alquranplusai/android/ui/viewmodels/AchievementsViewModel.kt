package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AchievementsViewModel(
    private val analyticsRepository: com.alquranplusai.domain.repositories.AnalyticsRepository
) : ViewModel() {
    
    private val _achievements = MutableStateFlow<List<com.alquranplusai.domain.models.Achievement>>(emptyList())
    val achievements: StateFlow<List<com.alquranplusai.domain.models.Achievement>> = _achievements.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _unlockedAchievements = MutableStateFlow<List<com.alquranplusai.domain.models.Achievement>>(emptyList())
    val unlockedAchievements: StateFlow<List<com.alquranplusai.domain.models.Achievement>> = _unlockedAchievements.asStateFlow()
    
    private val _lockedAchievements = MutableStateFlow<List<com.alquranplusai.domain.models.Achievement>>(emptyList())
    val lockedAchievements: StateFlow<List<com.alquranplusai.domain.models.Achievement>> = _lockedAchievements.asStateFlow()
    
    init {
        loadAchievements()
    }
    
    private fun loadAchievements() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                analyticsRepository.getUserAchievements("user_1").collect { achievementList ->
                    _achievements.value = achievementList
                    _unlockedAchievements.value = achievementList.filter { it.isUnlocked }
                    _lockedAchievements.value = achievementList.filter { !it.isUnlocked }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun checkAndUnlockAchievements(userId: String, progress: Map<String, Int>) {
        viewModelScope.launch {
            try {
                _achievements.value.forEach { achievement ->
                    if (!achievement.isUnlocked) {
                        val currentProgress = progress[achievement.id] ?: 0
                        if (currentProgress >= achievement.requirement) {
                            unlockAchievement(userId, achievement.id)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private suspend fun unlockAchievement(userId: String, achievementId: String) {
        analyticsRepository.unlockAchievement(userId, achievementId)
        loadAchievements()
    }
    
    fun getProgressPercentage(achievement: com.alquranplusai.domain.models.Achievement): Float {
        return if (achievement.requirement > 0) {
            (achievement.progress.toFloat() / achievement.requirement) * 100
        } else 0f
    }
    
    fun getTotalUnlocked(): Int = _unlockedAchievements.value.size
    
    fun getTotalAchievements(): Int = _achievements.value.size
}
