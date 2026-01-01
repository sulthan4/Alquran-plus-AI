package com.alquranplusai.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Android DataStore-based preferences manager
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "alquran_preferences")

class PreferencesManager(private val context: Context) {
    
    private val dataStore = context.dataStore
    
    // Keys
    object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val FONT_SIZE = floatPreferencesKey("font_size")
        val TRANSLATION_LANGUAGE = stringPreferencesKey("translation_language")
        val SELECTED_RECITER = stringPreferencesKey("selected_reciter")
        val AUDIO_QUALITY = stringPreferencesKey("audio_quality")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val REMINDER_TIME = longPreferencesKey("reminder_time")
        val LAST_READ_SURAH = intPreferencesKey("last_read_surah")
        val LAST_READ_AYAH = intPreferencesKey("last_read_ayah")
        val DAILY_GOAL_AYAHS = intPreferencesKey("daily_goal_ayahs")
        val STREAK_COUNT = intPreferencesKey("streak_count")
        val LAST_ACTIVE_DATE = longPreferencesKey("last_active_date")
    }
    
    // Theme
    suspend fun setThemeMode(mode: String) {
        dataStore.edit { it[Keys.THEME_MODE] = mode }
    }
    
    fun getThemeMode(): Flow<String> {
        return dataStore.data.map { it[Keys.THEME_MODE] ?: "system" }
    }
    
    // Font Size
    suspend fun setFontSize(size: Float) {
        dataStore.edit { it[Keys.FONT_SIZE] = size }
    }
    
    fun getFontSize(): Flow<Float> {
        return dataStore.data.map { it[Keys.FONT_SIZE] ?: 28f }
    }
    
    // Translation Language
    suspend fun setTranslationLanguage(language: String) {
        dataStore.edit { it[Keys.TRANSLATION_LANGUAGE] = language }
    }
    
    fun getTranslationLanguage(): Flow<String> {
        return dataStore.data.map { it[Keys.TRANSLATION_LANGUAGE] ?: "en" }
    }
    
    // Selected Reciter
    suspend fun setSelectedReciter(reciterId: String) {
        dataStore.edit { it[Keys.SELECTED_RECITER] = reciterId }
    }
    
    fun getSelectedReciter(): Flow<String?> {
        return dataStore.data.map { it[Keys.SELECTED_RECITER] }
    }
    
    // Audio Quality
    suspend fun setAudioQuality(quality: String) {
        dataStore.edit { it[Keys.AUDIO_QUALITY] = quality }
    }
    
    fun getAudioQuality(): Flow<String> {
        return dataStore.data.map { it[Keys.AUDIO_QUALITY] ?: "high" }
    }
    
    // Notifications
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.NOTIFICATIONS_ENABLED] = enabled }
    }
    
    fun getNotificationsEnabled(): Flow<Boolean> {
        return dataStore.data.map { it[Keys.NOTIFICATIONS_ENABLED] ?: true }
    }
    
    // Reminder Time
    suspend fun setReminderTime(timeMillis: Long) {
        dataStore.edit { it[Keys.REMINDER_TIME] = timeMillis }
    }
    
    fun getReminderTime(): Flow<Long?> {
        return dataStore.data.map { it[Keys.REMINDER_TIME] }
    }
    
    // Reading Progress
    suspend fun setLastReadPosition(surahNumber: Int, ayahNumber: Int) {
        dataStore.edit {
            it[Keys.LAST_READ_SURAH] = surahNumber
            it[Keys.LAST_READ_AYAH] = ayahNumber
        }
    }
    
    fun getLastReadPosition(): Flow<Pair<Int, Int>> {
        return dataStore.data.map {
            Pair(
                it[Keys.LAST_READ_SURAH] ?: 1,
                it[Keys.LAST_READ_AYAH] ?: 1
            )
        }
    }
    
    // Daily Goal
    suspend fun setDailyGoalAyahs(count: Int) {
        dataStore.edit { it[Keys.DAILY_GOAL_AYAHS] = count }
    }
    
    fun getDailyGoalAyahs(): Flow<Int> {
        return dataStore.data.map { it[Keys.DAILY_GOAL_AYAHS] ?: 5 }
    }
    
    // Streak
    suspend fun updateStreak(count: Int) {
        dataStore.edit {
            it[Keys.STREAK_COUNT] = count
            it[Keys.LAST_ACTIVE_DATE] = System.currentTimeMillis()
        }
    }
    
    fun getStreak(): Flow<Int> {
        return dataStore.data.map { it[Keys.STREAK_COUNT] ?: 0 }
    }
    
    // Clear all
    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}
