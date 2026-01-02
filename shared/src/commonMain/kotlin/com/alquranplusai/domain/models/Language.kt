package com.alquranplusai.domain.models

/**
 * Supported UI languages for the app
 */
data class AppLanguage(
    val code: String,        // ISO 639-1 code (e.g., "en", "ar")
    val name: String,        // Native name
    val englishName: String, // English name
    val isRtl: Boolean = false // Right-to-left language
)

object SupportedLanguages {
    // Top 15 most spoken languages in the world, prioritizing Muslim-majority regions
    val ENGLISH = AppLanguage("en", "English", "English")
    val ARABIC = AppLanguage("ar", "العربية", "Arabic", isRtl = true)
    val URDU = AppLanguage("ur", "اردو", "Urdu", isRtl = true)
    val INDONESIAN = AppLanguage("id", "Bahasa Indonesia", "Indonesian")
    val BENGALI = AppLanguage("bn", "বাংলা", "Bengali")
    val TURKISH = AppLanguage("tr", "Türkçe", "Turkish")
    val PERSIAN = AppLanguage("fa", "فارسی", "Persian", isRtl = true)
    val FRENCH = AppLanguage("fr", "Français", "French")
    val GERMAN = AppLanguage("de", "Deutsch", "German")
    val SPANISH = AppLanguage("es", "Español", "Spanish")
    val RUSSIAN = AppLanguage("ru", "Русский", "Russian")
    val CHINESE = AppLanguage("zh", "中文", "Chinese")
    val HINDI = AppLanguage("hi", "हिन्दी", "Hindi")
    val PORTUGUESE = AppLanguage("pt", "Português", "Portuguese")
    val MALAY = AppLanguage("ms", "Bahasa Melayu", "Malay")
    val PASHTO = AppLanguage("ps", "پښتو", "Pashto", isRtl = true)
    val HAUSA = AppLanguage("ha", "Hausa", "Hausa")
    val SWAHILI = AppLanguage("sw", "Kiswahili", "Swahili")
    val DUTCH = AppLanguage("nl", "Nederlands", "Dutch")
    val JAPANESE = AppLanguage("ja", "日本語", "Japanese")
    val KOREAN = AppLanguage("ko", "한국어", "Korean")
    
    /**
     * All supported languages ordered by relevance for Muslim users
     */
    val ALL = listOf(
        ENGLISH,      // 1. Global lingua franca
        ARABIC,       // 2. Language of Quran
        URDU,         // 3. Pakistan, India
        INDONESIAN,   // 4. Indonesia (largest Muslim population)
        BENGALI,      // 5. Bangladesh
        TURKISH,      // 6. Turkey
        PERSIAN,      // 7. Iran, Afghanistan
        FRENCH,       // 8. North/West Africa
        MALAY,        // 9. Malaysia, Singapore
        GERMAN,       // 10. Germany (large Muslim community)
        SPANISH,      // 11. Growing Muslim population
        RUSSIAN,      // 12. Central Asia
        HINDI,        // 13. India
        PASHTO,       // 14. Afghanistan, Pakistan
        HAUSA,        // 15. Nigeria, West Africa
        SWAHILI,      // 16. East Africa
        PORTUGUESE,   // 17. Brazil, Portugal
        CHINESE,      // 18. China
        DUTCH,        // 19. Netherlands
        JAPANESE,     // 20. Japan
        KOREAN        // 21. South Korea
    )
    
    /**
     * Get language by code
     */
    fun getByCode(code: String): AppLanguage? = ALL.find { it.code == code }
    
    /**
     * Check if language is RTL
     */
    fun isRtl(code: String): Boolean = getByCode(code)?.isRtl ?: false
}

