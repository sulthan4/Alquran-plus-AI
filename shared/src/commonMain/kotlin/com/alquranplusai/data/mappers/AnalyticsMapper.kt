package com.alquranplusai.data.mappers

import com.alquranplusai.data.network.dto.*
import com.alquranplusai.domain.models.*

/** Mapper for Analytics data */
class AnalyticsMapper {

    fun mapReadingSessionDtoToDomain(dto: ReadingSessionDto): ReadingSession {
        return ReadingSession(
                id = dto.id.toString(),
                userId = dto.userId.toString(),
                surahNumber = dto.surahNumber,
                startAyah = dto.ayahNumber,
                endAyah = null,
                mode = ReadingMode.CONTINUOUS,
                startTime = dto.startedAt,
                endTime = dto.endedAt,
                duration = dto.duration.toInt()
        )
    }

    fun mapAchievementDtoToDomain(dto: AchievementDto): Achievement {
        return Achievement(
                id = dto.id.toString(),
                title = dto.title,
                description = dto.description,
                icon = "trophy",
                category = AchievementCategory.READING,
                requirement = 1,
                isUnlocked = true,
                unlockedAt = dto.unlockedAt
        )
    }

    fun mapGoalDtoToDomain(dto: GoalDto): Goal {
        return Goal(
                id = dto.id.toString(),
                type =
                        when (dto.goalType.uppercase()) {
                            "DAILY" -> GoalType.DAILY_READING
                            "WEEKLY" -> GoalType.WEEKLY_READING
                            "MONTHLY" -> GoalType.MONTHLY_READING
                            else -> GoalType.CUSTOM
                        },
                title = "Goal",
                target = dto.targetValue,
                current = dto.currentValue,
                startDate = dto.createdAt,
                endDate = dto.deadline
                                ?: (dto.createdAt + 30L * 24 * 60 * 60 * 1000) // Default 30 days
        )
    }
}
