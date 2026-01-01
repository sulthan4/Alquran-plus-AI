package com.alquranplusai.android.utils

object Constants {
    const val APP_NAME = "AlQuran Plus AI"
    const val DATABASE_NAME = "alquran.db"
    
    // Preferences
    const val PREF_FONT_SIZE = "font_size"
    const val PREF_THEME = "theme"
    const val PREF_LANGUAGE = "language"
    const val PREF_LAST_READ_SURAH = "last_read_surah"
    const val PREF_LAST_READ_AYAH = "last_read_ayah"
    
    // Default values
    const val DEFAULT_FONT_SIZE = 18
    const val MIN_FONT_SIZE = 12
    const val MAX_FONT_SIZE = 32
    
    // Notification channels
    const val CHANNEL_ID_REMINDERS = "reminders"
    const val CHANNEL_ID_DOWNLOADS = "downloads"
    
    // Intent extras
    const val EXTRA_SURAH_NUMBER = "surah_number"
    const val EXTRA_AYAH_NUMBER = "ayah_number"
    const val EXTRA_JUZ_NUMBER = "juz_number"
}
