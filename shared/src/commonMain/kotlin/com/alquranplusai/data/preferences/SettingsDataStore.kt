package com.alquranplusai.data.preferences

import com.alquranplusai.data.database.AlQuranDatabaseWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull

/**
 * Settings data store for user preferences backed by SQLDelight
 */
class SettingsDataStore(
    private val database: AlQuranDatabaseWrapper
) {
    // TODO: integrate with AuthRepository to get real userId
    private val currentUserId = "default_user" 

    init {
        // Ensure default settings exist for this user
        try {
            val exists = database.settingsQueries.selectUserSettings(currentUserId).executeAsOneOrNull()
            if (exists == null) {
                // Insert default settings
                database.settingsQueries.insertSettings(
                    userId = currentUserId,
                    defaultReadingMode = "CONTINUOUS",
                    defaultTextType = "UTHMANI",
                    arabicFontFamily = "uthmanic_hafs",
                    arabicFontSize = 24,
                    translationFontSize = 16,
                    lineSpacing = 1.5,
                    showTajweed = 1,
                    showTransliteration = 0,
                    showWordByWord = 0,
                    defaultReciterId = null,
                    autoPlayAudio = 0,
                    audioPlaybackSpeed = 1.0,
                    wordByWordAudio = 0,
                    theme = "SYSTEM",
                    language = "en",
                    enableAnimations = 1,
                    enableHapticFeedback = 1,
                    enableNotifications = 1,
                    dailyReminderEnabled = 0,
                    dailyReminderTime = "09:00",
                    quizReminderEnabled = 0,
                    achievementNotifications = 1,
                    shareStatistics = 0,
                    showOnLeaderboard = 1,
                    biometricLockEnabled = 0,
                    autoDownloadAudio = 0,
                    downloadOnWifiOnly = 1,
                    autoBackup = 1,
                    backupFrequency = "WEEKLY"
                )
            }
        } catch (e: Exception) {
            // Log error but don't crash
            println("Error initializing default settings: ${e.message}")
        }
    }
    
    suspend fun saveTheme(theme: String) {
        val current = database.settingsQueries.selectUserSettings(currentUserId).executeAsOneOrNull()
        if (current != null) {
            database.settingsQueries.updateUIPreferences(
                theme = theme,
                language = current.language,
                enableAnimations = current.enableAnimations,
                enableHapticFeedback = current.enableHapticFeedback,
                userId = currentUserId
            )
        }
    }
    
    fun getTheme(): Flow<String> {
        return database.settingsQueries.selectUserSettings(currentUserId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { it?.theme ?: "SYSTEM" }
    }
    
    suspend fun saveLanguage(language: String) {
        val current = database.settingsQueries.selectUserSettings(currentUserId).executeAsOneOrNull()
        if (current != null) {
            database.settingsQueries.updateUIPreferences(
                theme = current.theme,
                language = language,
                enableAnimations = current.enableAnimations,
                enableHapticFeedback = current.enableHapticFeedback,
                userId = currentUserId
            )
        }
    }
    
    fun getLanguage(): Flow<String> {
        return database.settingsQueries.selectUserSettings(currentUserId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { it?.language ?: "en" }
    }
    
    suspend fun saveFontSize(size: Int) {
       val current = database.settingsQueries.selectUserSettings(currentUserId).executeAsOneOrNull()
       if (current != null) {
           database.settingsQueries.updateReadingPreferences(
               defaultReadingMode = current.defaultReadingMode,
               defaultTextType = current.defaultTextType,
               arabicFontFamily = current.arabicFontFamily,
               arabicFontSize = size.toLong(),
               translationFontSize = current.translationFontSize,
               lineSpacing = current.lineSpacing,
               showTajweed = current.showTajweed,
               showTransliteration = current.showTransliteration,
               showWordByWord = current.showWordByWord,
               userId = currentUserId
           )
       }
    }
    
    fun getFontSize(): Flow<Int> {
        return database.settingsQueries.selectUserSettings(currentUserId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { it?.arabicFontSize?.toInt() ?: 24 }
    }

    suspend fun saveAutoPlay(enabled: Boolean) {
        val current = database.settingsQueries.selectUserSettings(currentUserId).executeAsOneOrNull()
        if (current != null) {
            database.settingsQueries.updateAudioPreferences(
                defaultReciterId = current.defaultReciterId,
                autoPlayAudio = if (enabled) 1L else 0L,
                audioPlaybackSpeed = current.audioPlaybackSpeed,
                wordByWordAudio = current.wordByWordAudio,
                userId = currentUserId
            )
        }
    }

    fun getAutoPlay(): Flow<Boolean> {
        return database.settingsQueries.selectUserSettings(currentUserId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { (it?.autoPlayAudio ?: 0L) == 1L }
    }

    suspend fun saveNotificationEnabled(enabled: Boolean) {
        val current = database.settingsQueries.selectUserSettings(currentUserId).executeAsOneOrNull()
        if (current != null) {
            database.settingsQueries.updateNotificationPreferences(
                enableNotifications = if (enabled) 1L else 0L,
                dailyReminderEnabled = current.dailyReminderEnabled,
                dailyReminderTime = current.dailyReminderTime,
                quizReminderEnabled = current.quizReminderEnabled,
                achievementNotifications = current.achievementNotifications,
                userId = currentUserId
            )
        }
    }

    fun getNotificationsEnabled(): Flow<Boolean> {
        return database.settingsQueries.selectUserSettings(currentUserId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { (it?.enableNotifications ?: 1L) == 1L }
    }

    fun getPlaybackSpeed(): Flow<Float> {
        return database.settingsQueries.selectUserSettings(currentUserId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { it?.audioPlaybackSpeed?.toFloat() ?: 1.0f }
    }

    suspend fun savePlaybackSpeed(speed: Float) {
        val current = database.settingsQueries.selectUserSettings(currentUserId).executeAsOneOrNull()
        if (current != null) {
            database.settingsQueries.updateAudioPreferences(
                defaultReciterId = current.defaultReciterId,
                autoPlayAudio = current.autoPlayAudio,
                audioPlaybackSpeed = speed.toDouble(),
                wordByWordAudio = current.wordByWordAudio,
                userId = currentUserId
            )
        }
    }
    suspend fun saveAnimationsEnabled(enabled: Boolean) {
        val current = database.settingsQueries.selectUserSettings(currentUserId).executeAsOneOrNull()
        if (current != null) {
            database.settingsQueries.updateUIPreferences(
                theme = current.theme,
                language = current.language,
                enableAnimations = if (enabled) 1L else 0L,
                enableHapticFeedback = current.enableHapticFeedback,
                userId = currentUserId
            )
        }
    }

    fun getAnimationsEnabled(): Flow<Boolean> {
        return database.settingsQueries.selectUserSettings(currentUserId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { (it?.enableAnimations ?: 1L) == 1L }
    }

    suspend fun saveHapticFeedbackEnabled(enabled: Boolean) {
        val current = database.settingsQueries.selectUserSettings(currentUserId).executeAsOneOrNull()
        if (current != null) {
            database.settingsQueries.updateUIPreferences(
                theme = current.theme,
                language = current.language,
                enableAnimations = current.enableAnimations,
                enableHapticFeedback = if (enabled) 1L else 0L,
                userId = currentUserId
            )
        }
    }

    fun getHapticFeedbackEnabled(): Flow<Boolean> {
        return database.settingsQueries.selectUserSettings(currentUserId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { (it?.enableHapticFeedback ?: 1L) == 1L }
    }

    // --- Reading Advanced ---
    suspend fun saveReadingMode(mode: String) {
        val current = database.settingsQueries.selectUserSettings(currentUserId).executeAsOneOrNull() ?: return
        database.settingsQueries.updateReadingPreferences(
            defaultReadingMode = mode,
            defaultTextType = current.defaultTextType,
            arabicFontFamily = current.arabicFontFamily,
            arabicFontSize = current.arabicFontSize,
            translationFontSize = current.translationFontSize,
            lineSpacing = current.lineSpacing,
            showTajweed = current.showTajweed,
            showTransliteration = current.showTransliteration,
            showWordByWord = current.showWordByWord,
            userId = currentUserId
        )
    }

    fun getReadingMode(): Flow<String> {
        return database.settingsQueries.selectUserSettings(currentUserId).asFlow().mapToOneOrNull(Dispatchers.IO).map { it?.defaultReadingMode ?: "CONTINUOUS" }
    }

    suspend fun saveShowWordByWord(show: Boolean) {
        val current = database.settingsQueries.selectUserSettings(currentUserId).executeAsOneOrNull() ?: return
        database.settingsQueries.updateReadingPreferences(
            defaultReadingMode = current.defaultReadingMode,
            defaultTextType = current.defaultTextType,
            arabicFontFamily = current.arabicFontFamily,
            arabicFontSize = current.arabicFontSize,
            translationFontSize = current.translationFontSize,
            lineSpacing = current.lineSpacing,
            showTajweed = current.showTajweed,
            showTransliteration = current.showTransliteration,
            showWordByWord = if (show) 1L else 0L,
            userId = currentUserId
        )
    }

    fun getShowWordByWord(): Flow<Boolean> {
        return database.settingsQueries.selectUserSettings(currentUserId).asFlow().mapToOneOrNull(Dispatchers.IO).map { (it?.showWordByWord ?: 0L) == 1L }
    }

    // --- Audio Details ---
    suspend fun saveDefaultReciterId(reciterId: String) {
        val current = database.settingsQueries.selectUserSettings(currentUserId).executeAsOneOrNull() ?: return
        database.settingsQueries.updateAudioPreferences(
            defaultReciterId = reciterId,
            autoPlayAudio = current.autoPlayAudio,
            audioPlaybackSpeed = current.audioPlaybackSpeed,
            wordByWordAudio = current.wordByWordAudio,
            userId = currentUserId
        )
    }

    fun getDefaultReciterId(): Flow<String?> {
        return database.settingsQueries.selectUserSettings(currentUserId).asFlow().mapToOneOrNull(Dispatchers.IO).map { it?.defaultReciterId }
    }

    // --- Notifications Details ---
    suspend fun saveDailyReminderTime(time: String) {
        val current = database.settingsQueries.selectUserSettings(currentUserId).executeAsOneOrNull() ?: return
        database.settingsQueries.updateNotificationPreferences(
            enableNotifications = current.enableNotifications,
            dailyReminderEnabled = current.dailyReminderEnabled,
            dailyReminderTime = time,
            quizReminderEnabled = current.quizReminderEnabled,
            achievementNotifications = current.achievementNotifications,
            userId = currentUserId
        )
    }

    fun getDailyReminderTime(): Flow<String> {
        return database.settingsQueries.selectUserSettings(currentUserId).asFlow().mapToOneOrNull(Dispatchers.IO).map { it?.dailyReminderTime ?: "09:00" }
    }

    suspend fun saveDailyReminderEnabled(enabled: Boolean) {
        val current = database.settingsQueries.selectUserSettings(currentUserId).executeAsOneOrNull() ?: return
        database.settingsQueries.updateNotificationPreferences(
            enableNotifications = current.enableNotifications,
            dailyReminderEnabled = if (enabled) 1L else 0L,
            dailyReminderTime = current.dailyReminderTime,
            quizReminderEnabled = current.quizReminderEnabled,
            achievementNotifications = current.achievementNotifications,
            userId = currentUserId
        )
    }

    fun getDailyReminderEnabled(): Flow<Boolean> {
        return database.settingsQueries.selectUserSettings(currentUserId).asFlow().mapToOneOrNull(Dispatchers.IO).map { (it?.dailyReminderEnabled ?: 0L) == 1L }
    }
}
