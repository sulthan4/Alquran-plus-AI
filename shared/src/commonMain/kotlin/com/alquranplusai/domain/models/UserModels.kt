package com.alquranplusai.domain.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * User account
 */
@Serializable
data class User(
    val id: String,
    val email: String,
    val username: String,
    val profile: UserProfile,
    val preferences: UserPreferences,
    @Contextual val statistics: UserStatisticsSummary? = null,
    val subscription: Subscription? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val lastLoginAt: Long? = null,
    val isEmailVerified: Boolean = false,
    val isActive: Boolean = true
)

/**
 * User profile information
 */
@Serializable
data class UserProfile(
    val displayName: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val avatarUrl: String? = null,
    val bio: String? = null,
    val country: String? = null,
    val language: String = "en",
    val timezone: String? = null,
    val dateOfBirth: String? = null,
    val gender: Gender? = null
)

/**
 * User preferences
 */
@Serializable
data class UserPreferences(
    // Reading preferences
    val defaultReadingMode: ReadingMode = ReadingMode.CONTINUOUS,
    val defaultTextType: TextType = TextType.UTHMANI,
    val arabicFontFamily: String = "uthmanic_hafs",
    val arabicFontSize: Int = 24,
    val translationFontSize: Int = 16,
    val lineSpacing: Float = 1.5f,
    val showTajweed: Boolean = true,
    val showTransliteration: Boolean = false,
    val defaultTranslationIds: List<String> = emptyList(),
    val showWordByWord: Boolean = false,
    
    // Audio preferences
    val defaultReciterId: String? = null,
    val autoPlayAudio: Boolean = false,
    val audioPlaybackSpeed: Float = 1.0f,
    val wordByWordAudio: Boolean = false,
    
    // UI preferences
    val theme: AppTheme = AppTheme.SYSTEM,
    val language: String = "en",
    val enableAnimations: Boolean = true,
    val enableHapticFeedback: Boolean = true,
    
    // Notification preferences
    val enableNotifications: Boolean = true,
    val dailyReminderEnabled: Boolean = false,
    val dailyReminderTime: String? = null,
    val quizReminderEnabled: Boolean = false,
    val achievementNotifications: Boolean = true,
    
    // Privacy preferences
    val shareStatistics: Boolean = false,
    val showOnLeaderboard: Boolean = true,
    val biometricLockEnabled: Boolean = false,
    
    // Data preferences
    val autoDownloadAudio: Boolean = false,
    val downloadOnWifiOnly: Boolean = true,
    val autoBackup: Boolean = true,
    val backupFrequency: BackupFrequency = BackupFrequency.WEEKLY
)

/**
 * Subscription information
 */
@Serializable
data class Subscription(
    val id: String,
    val userId: String,
    val plan: SubscriptionPlan,
    val status: SubscriptionStatus,
    val startDate: Long,
    val endDate: Long? = null,
    val autoRenew: Boolean = true,
    val paymentMethod: String? = null,
    val features: List<String> = emptyList()
)

/**
 * Subscription plan
 */
@Serializable
data class Plan(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val currency: String,
    val duration: PlanDuration,
    val features: List<String> = emptyList(),
    val isPopular: Boolean = false,
    val trialDays: Int = 0
)

/**
 * Authentication credentials
 */
@Serializable
data class AuthCredentials(
    val email: String,
    val password: String
)

/**
 * Authentication token
 */
@Serializable
data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val tokenType: String = "Bearer"
)

/**
 * Social auth provider
 */
@Serializable
data class SocialAuthProvider(
    val provider: AuthProvider,
    val token: String,
    val userId: String? = null
)

/**
 * Gender
 */
@Serializable
enum class Gender {
    MALE,
    FEMALE,
    OTHER,
    PREFER_NOT_TO_SAY
}

/**
 * App theme
 */
@Serializable
enum class AppTheme {
    LIGHT,
    DARK,
    SYSTEM,
    AMOLED
}

/**
 * Backup frequency
 */
@Serializable
enum class BackupFrequency {
    DAILY,
    WEEKLY,
    MONTHLY,
    MANUAL
}

/**
 * Subscription plan type
 */
@Serializable
enum class SubscriptionPlan {
    FREE,
    BASIC,
    PREMIUM,
    LIFETIME
}

/**
 * Subscription status
 */
@Serializable
enum class SubscriptionStatus {
    ACTIVE,
    EXPIRED,
    CANCELLED,
    TRIAL,
    SUSPENDED
}

/**
 * Plan duration
 */
@Serializable
enum class PlanDuration {
    MONTHLY,
    YEARLY,
    LIFETIME
}

/**
 * Auth provider
 */
@Serializable
enum class AuthProvider {
    EMAIL,
    GOOGLE,
    FACEBOOK,
    APPLE
}
