package com.alquranplusai.data.preferences

/**
 * Preferences manager for app settings
 */
class PreferencesManager(
    private val settingsDataStore: SettingsDataStore
) {
    
    val theme: kotlinx.coroutines.flow.Flow<String> = settingsDataStore.getTheme()
    val language: kotlinx.coroutines.flow.Flow<String> = settingsDataStore.getLanguage()
    val fontSize: kotlinx.coroutines.flow.Flow<Int> = settingsDataStore.getFontSize()

    suspend fun updateTheme(theme: String) {
        settingsDataStore.saveTheme(theme)
    }
    
    suspend fun updateLanguage(language: String) {
        settingsDataStore.saveLanguage(language)
    }
    
    suspend fun updateFontSize(size: Int) {
        settingsDataStore.saveFontSize(size)
    }

    val autoPlay: kotlinx.coroutines.flow.Flow<Boolean> = settingsDataStore.getAutoPlay()
    val notificationsEnabled: kotlinx.coroutines.flow.Flow<Boolean> = settingsDataStore.getNotificationsEnabled()

    suspend fun updateAutoPlay(enabled: Boolean) {
        settingsDataStore.saveAutoPlay(enabled)
    }

    suspend fun updateNotificationsEnabled(enabled: Boolean) {
        settingsDataStore.saveNotificationEnabled(enabled)
    }

    val playbackSpeed: kotlinx.coroutines.flow.Flow<Float> = settingsDataStore.getPlaybackSpeed()

    suspend fun updatePlaybackSpeed(speed: Float) {
        settingsDataStore.savePlaybackSpeed(speed)
    }

    val animationsEnabled: kotlinx.coroutines.flow.Flow<Boolean> = settingsDataStore.getAnimationsEnabled()
    val hapticFeedbackEnabled: kotlinx.coroutines.flow.Flow<Boolean> = settingsDataStore.getHapticFeedbackEnabled()

    suspend fun updateAnimationsEnabled(enabled: Boolean) {
        settingsDataStore.saveAnimationsEnabled(enabled)
    }

    suspend fun updateHapticFeedbackEnabled(enabled: Boolean) {
        settingsDataStore.saveHapticFeedbackEnabled(enabled)
    }

    // --- Reading ---
    val readingMode: kotlinx.coroutines.flow.Flow<String> = settingsDataStore.getReadingMode()
    val showWordByWord: kotlinx.coroutines.flow.Flow<Boolean> = settingsDataStore.getShowWordByWord()

    suspend fun updateReadingMode(mode: String) {
        settingsDataStore.saveReadingMode(mode)
    }

    suspend fun updateShowWordByWord(show: Boolean) {
        settingsDataStore.saveShowWordByWord(show)
    }

    // --- Audio ---
    val defaultReciterId: kotlinx.coroutines.flow.Flow<String?> = settingsDataStore.getDefaultReciterId()

    suspend fun updateDefaultReciterId(reciterId: String) {
        settingsDataStore.saveDefaultReciterId(reciterId)
    }

    // --- Notifications ---
    val dailyReminderEnabled: kotlinx.coroutines.flow.Flow<Boolean> = settingsDataStore.getDailyReminderEnabled()
    val dailyReminderTime: kotlinx.coroutines.flow.Flow<String> = settingsDataStore.getDailyReminderTime()

    suspend fun updateDailyReminderEnabled(enabled: Boolean) {
        settingsDataStore.saveDailyReminderEnabled(enabled)
    }

    suspend fun updateDailyReminderTime(time: String) {
        settingsDataStore.saveDailyReminderTime(time)
    }
}
