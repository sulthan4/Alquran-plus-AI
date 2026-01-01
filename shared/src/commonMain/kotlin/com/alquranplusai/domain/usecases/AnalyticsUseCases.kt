package com.alquranplusai.domain.usecases

import com.alquranplusai.domain.models.ReadingSession
import com.alquranplusai.domain.models.ReadingMode
import com.alquranplusai.domain.repositories.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackReadingSessionUseCase(
    private val analyticsRepository: AnalyticsRepository
) {
    suspend operator fun invoke(
        surahNumber: Int,
        startAyah: Int,
        endAyah: Int,
        durationMillis: Long
    ): Result<Unit> {
        return try {
            analyticsRepository.startReadingSession(
                userId = "default",
                surahNumber = surahNumber,
                ayahNumber = startAyah,
                mode = ReadingMode.CONTINUOUS
            )
            // TODO: End session with duration
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class GetReadingStatisticsUseCase(
    private val analyticsRepository: AnalyticsRepository
) {
    suspend operator fun invoke(): Flow<ReadingStatistics> {
        return kotlinx.coroutines.flow.combine(
            analyticsRepository.getTotalReadingTime(userId = "default"),
            analyticsRepository.getCurrentStreak(userId = "default"),
            analyticsRepository.getLongestStreak(userId = "default")
        ) { totalTime, currentStreak, longestStreak ->
            ReadingStatistics(
                totalReadingTime = totalTime,
                currentStreak = currentStreak,
                longestStreak = longestStreak,
                averageSessionTime = if (totalTime > 0) totalTime / 30 else 0 // Rough estimate
            )
        }
    }
}

class UpdateStreakUseCase(
    private val analyticsRepository: AnalyticsRepository
) {
    suspend operator fun invoke(): Result<Int> {
        return try {
            // TODO: Check if user read today and update streak
            val currentStreak = 0 // Placeholder
            Result.success(currentStreak)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class GetAchievementsUseCase(
    private val analyticsRepository: AnalyticsRepository
) {
    suspend operator fun invoke(userId: String = "default"): List<AchievementWithProgress> {
        // TODO: Implement actual achievement tracking
        return emptyList()
    }
}

data class ReadingStatistics(
    val totalReadingTime: Long,
    val currentStreak: Int,
    val longestStreak: Int,
    val averageSessionTime: Long
)

data class AchievementWithProgress(
    val achievement: com.alquranplusai.domain.models.Achievement,
    val currentProgress: Int,
    val targetProgress: Int,
    val isUnlocked: Boolean
)
