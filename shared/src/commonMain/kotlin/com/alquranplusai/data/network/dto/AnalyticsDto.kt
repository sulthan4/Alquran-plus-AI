package com.alquranplusai.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class AnalyticsDto(
    val id: Int,
    val userId: Int,
    val eventType: String,
    val eventData: Map<String, String> = emptyMap(),
    val timestamp: Long
)

@Serializable
data class ReadingSessionDto(
    val id: Int,
    val userId: Int,
    val surahNumber: Int,
    val ayahNumber: Int,
    val duration: Long,
    val startedAt: Long,
    val endedAt: Long? = null
)

@Serializable
data class AchievementDto(
    val id: Int,
    val userId: Int,
    val achievementType: String,
    val title: String,
    val description: String,
    val unlockedAt: Long
)

@Serializable
data class GoalDto(
    val id: Int,
    val userId: Int,
    val goalType: String,
    val targetValue: Int,
    val currentValue: Int = 0,
    val deadline: Long? = null,
    val createdAt: Long,
    val completedAt: Long? = null
)
