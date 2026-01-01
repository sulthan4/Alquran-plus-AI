package com.alquranplusai.android.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferencesHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "alquran_prefs",
        Context.MODE_PRIVATE
    )
    
    fun setFontSize(size: Int) {
        prefs.edit { putInt(Constants.PREF_FONT_SIZE, size) }
    }
    
    fun getFontSize(): Int {
        return prefs.getInt(Constants.PREF_FONT_SIZE, Constants.DEFAULT_FONT_SIZE)
    }
    
    fun setLastReadPosition(surahNumber: Int, ayahNumber: Int) {
        prefs.edit {
            putInt(Constants.PREF_LAST_READ_SURAH, surahNumber)
            putInt(Constants.PREF_LAST_READ_AYAH, ayahNumber)
        }
    }
    
    fun getLastReadSurah(): Int {
        return prefs.getInt(Constants.PREF_LAST_READ_SURAH, 1)
    }
    
    fun getLastReadAyah(): Int {
        return prefs.getInt(Constants.PREF_LAST_READ_AYAH, 1)
    }
    
    fun setTheme(theme: String) {
        prefs.edit { putString(Constants.PREF_THEME, theme) }
    }
    
    fun getTheme(): String {
        return prefs.getString(Constants.PREF_THEME, "system") ?: "system"
    }
    
    fun setLanguage(language: String) {
        prefs.edit { putString(Constants.PREF_LANGUAGE, language) }
    }
    
    fun getLanguage(): String {
        return prefs.getString(Constants.PREF_LANGUAGE, "en") ?: "en"
    }
}
