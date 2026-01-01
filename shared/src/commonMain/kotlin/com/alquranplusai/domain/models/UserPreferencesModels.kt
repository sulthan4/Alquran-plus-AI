package com.alquranplusai.domain.models

import kotlinx.serialization.Serializable

// ... (existing User, UserProfile, UserPreferences, etc. remain the same)

/**
 * Reading preferences
 */
@Serializable
data class ReadingPreferences(
    val fontSize: Int = 18,
    val fontFamily: String = "Amiri",
    val textType: TextType = TextType.UTHMANI,
    val showTranslation: Boolean = true,
    val showTransliteration: Boolean = false,
    val showWordByWord: Boolean = false,
    val enableTajweed: Boolean = true,
    val nightMode: Boolean = false
)

/**
 * Audio preferences
 */
@Serializable
data class AudioPreferences(
    val defaultReciter: String? = null,
    val autoPlay: Boolean = false,
    val playbackSpeed: Float = 1.0f,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val showWordTiming: Boolean = true
)

/**
 * UI preferences
 */
@Serializable
data class UIPreferences(
    val theme: AppTheme = AppTheme.SYSTEM,
    val language: String = "en",
    val enableAnimations: Boolean = true,
    val enableHapticFeedback: Boolean = true
)

/**
 * Notification preferences
 */
@Serializable
data class NotificationPreferences(
    val enableNotifications: Boolean = true,
    val dailyReminderEnabled: Boolean = false,
    val dailyReminderTime: String? = null,
    val quizReminderEnabled: Boolean = false,
    val achievementNotifications: Boolean = true
)

/**
 * Privacy preferences
 */
@Serializable
data class PrivacyPreferences(
    val shareStatistics: Boolean = false,
    val showOnLeaderboard: Boolean = true,
    val biometricLockEnabled: Boolean = false
)
