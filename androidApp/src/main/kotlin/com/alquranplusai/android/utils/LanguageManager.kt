package com.alquranplusai.android.utils

import android.content.Context

class LanguageManager(private val context: Context) {
    
    private val prefs = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
    
    fun getCurrentLanguage(): String {
        return prefs.getString("language", "en") ?: "en"
    }
    
    fun setLanguage(languageCode: String) {
        prefs.edit().putString("language", languageCode).apply()
    }
    
    fun getAvailableLanguages(): List<Pair<String, String>> {
        return listOf(
            "en" to "English",
            "ar" to "العربية",
            "ur" to "اردو",
            "ta" to "தமிழ்",
            "tr" to "Türkçe",
            "id" to "Bahasa Indonesia"
        )
    }
}
