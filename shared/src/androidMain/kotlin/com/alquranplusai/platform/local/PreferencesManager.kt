package com.alquranplusai.platform.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "alquran_preferences")

/**
 * Android implementation of PreferencesManager using DataStore
 */
actual class PreferencesManager(private val context: Context) {
    
    private val dataStore = context.dataStore
    
    // Preference Keys for Reading Settings
    private object PreferenceKeys {
        val ARABIC_FONT_SIZE = floatPreferencesKey("arabic_font_size")
        val TRANSLATION_FONT_SIZE = floatPreferencesKey("translation_font_size")
        val LINE_SPACING = floatPreferencesKey("line_spacing")
        val SHOW_TRANSLATION = booleanPreferencesKey("show_translation")
        val SHOW_TRANSLITERATION = booleanPreferencesKey("show_transliteration")
        val KEEP_SCREEN_ON = booleanPreferencesKey("keep_screen_on")
        val ANIMATIONS_ENABLED = booleanPreferencesKey("animations_enabled")
        val HAPTIC_FEEDBACK_ENABLED = booleanPreferencesKey("haptic_feedback_enabled")
    }
    
    actual suspend fun putString(key: String, value: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }
    
    actual suspend fun getString(key: String, defaultValue: String): String {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)] ?: defaultValue
        }.first()
    }
    
    actual suspend fun putInt(key: String, value: Int) {
        dataStore.edit { preferences ->
            preferences[intPreferencesKey(key)] = value
        }
    }
    
    actual suspend fun getInt(key: String, defaultValue: Int): Int {
        return dataStore.data.map { preferences ->
            preferences[intPreferencesKey(key)] ?: defaultValue
        }.first()
    }
    
    actual suspend fun putBoolean(key: String, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }
    
    actual suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(key)] ?: defaultValue
        }.first()
    }
    
    actual suspend fun putLong(key: String, value: Long) {
        dataStore.edit { preferences ->
            preferences[longPreferencesKey(key)] = value
        }
    }
    
    actual suspend fun getLong(key: String, defaultValue: Long): Long {
        return dataStore.data.map { preferences ->
            preferences[longPreferencesKey(key)] ?: defaultValue
        }.first()
    }
    
    actual suspend fun putFloat(key: String, value: Float) {
        dataStore.edit { preferences ->
            preferences[floatPreferencesKey(key)] = value
        }
    }
    
    actual suspend fun getFloat(key: String, defaultValue: Float): Float {
        return dataStore.data.map { preferences ->
            preferences[floatPreferencesKey(key)] ?: defaultValue
        }.first()
    }
    
    actual suspend fun remove(key: String) {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(key))
        }
    }
    
    actual suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    
    actual fun observeString(key: String, defaultValue: String): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)] ?: defaultValue
        }
    }
    
    actual fun observeBoolean(key: String, defaultValue: Boolean): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(key)] ?: defaultValue
        }
    }
    
    // Reading Preferences Flows
    val arabicFontSize: Flow<Float> = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.ARABIC_FONT_SIZE] ?: 24f
    }
    
    val translationFontSize: Flow<Float> = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.TRANSLATION_FONT_SIZE] ?: 16f
    }
    
    val lineSpacing: Flow<Float> = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.LINE_SPACING] ?: 1.5f
    }
    
    val showTranslation: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.SHOW_TRANSLATION] ?: true
    }
    
    val showTransliteration: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.SHOW_TRANSLITERATION] ?: false
    }
    
    val keepScreenOn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.KEEP_SCREEN_ON] ?: false
    }
    
    val animationsEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.ANIMATIONS_ENABLED] ?: true
    }
    
    val hapticFeedbackEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.HAPTIC_FEEDBACK_ENABLED] ?: true
    }
    
    // Reading Preferences Update Methods
    suspend fun updateArabicFontSize(size: Float) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.ARABIC_FONT_SIZE] = size
        }
    }
    
    suspend fun updateTranslationFontSize(size: Float) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.TRANSLATION_FONT_SIZE] = size
        }
    }
    
    suspend fun updateLineSpacing(spacing: Float) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.LINE_SPACING] = spacing
        }
    }
    
    suspend fun updateShowTranslation(show: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.SHOW_TRANSLATION] = show
        }
    }
    
    suspend fun updateShowTransliteration(show: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.SHOW_TRANSLITERATION] = show
        }
    }
    
    suspend fun updateKeepScreenOn(keep: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.KEEP_SCREEN_ON] = keep
        }
    }
    
    suspend fun updateAnimationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.ANIMATIONS_ENABLED] = enabled
        }
    }
    
    suspend fun updateHapticFeedbackEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.HAPTIC_FEEDBACK_ENABLED] = enabled
        }
    }
}
