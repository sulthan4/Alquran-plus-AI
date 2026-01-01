package com.alquranplusai.data.network

/**
 * Network configuration constants
 */
object NetworkConfig {
    const val API_BASE_URL = "https://api.alquranplus.com/v1/"
    const val QURAN_API_URL = "https://api.quran.com/api/v4/"
    const val TIMEOUT_SECONDS = 30L
    const val MAX_RETRIES = 3
    
    object Endpoints {
        const val SURAHS = "surahs"
        const val AYAHS = "ayahs"
        const val TRANSLATIONS = "translations"
        const val RECITERS = "reciters"
        const val AUDIO = "audio"
        const val BOOKMARKS = "bookmarks"
        const val QUIZZES = "quizzes"
        const val USER = "user"
        const val AUTH = "auth"
        const val ANALYTICS = "analytics"
    }
}
