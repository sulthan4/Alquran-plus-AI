package com.alquranplusai.data.network.api

import com.alquranplusai.data.network.dto.AnalyticsDto
import com.alquranplusai.data.network.dto.ReadingSessionDto
import com.alquranplusai.data.network.dto.AchievementDto
import com.alquranplusai.data.network.dto.GoalDto

/**
 * API service for analytics and tracking
 */
interface AnalyticsApiService {
    
    suspend fun trackEvent(userId: Int, event: AnalyticsDto)
    
    suspend fun getReadingSessions(userId: Int): List<ReadingSessionDto>
    
    suspend fun createReadingSession(userId: Int, session: ReadingSessionDto): ReadingSessionDto
    
    suspend fun getUserAchievements(userId: Int): List<AchievementDto>
    
    suspend fun getUserGoals(userId: Int): List<GoalDto>
    
    suspend fun createGoal(userId: Int, goal: GoalDto): GoalDto
    
    suspend fun updateGoal(goalId: Int, goal: GoalDto): GoalDto
}
