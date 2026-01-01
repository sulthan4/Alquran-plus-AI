package com.alquranplusai.android.navigation

object NavRoutes {
    // Main Navigation
    const val HOME = "home"
    const val READING = "reading/{surahNumber}/{ayahNumber}"
    const val SURAH_LIST = "surah_list"
    const val SURAH_DETAIL = "surah_detail/{surahNumber}"
    const val JUZ_LIST = "juz_list"
    const val BOOKMARKS = "bookmarks"
    const val SEARCH = "search"
    const val AUDIO = "audio"
    const val AUDIO_PLAYER = "audio_player"
    const val QUIZ_LIST = "quiz_list"
    const val QUIZ_PLAY = "quiz_play/{quizId}"
    const val QUIZ_RESULT = "quiz_result/{quizId}"
    const val TAFSIR_SELECTION = "tafsir_selection"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    const val ANALYTICS = "analytics"
    
    // Onboarding
    const val SPLASH = "splash"
    const val Splash = SPLASH  // Alias for compatibility
    const val ONBOARDING = "onboarding"
    const val WELCOME = "welcome"
    const val PERMISSIONS = "permissions"
    const val SETUP_COMPLETE = "setup_complete"
    
    // Auth
    const val AUTH = "auth"
    const val LOGIN = "login"
    const val REGISTER = "register"
    
    // Quran Views
    const val JUZ_VIEW = "juz_view/{juzNumber}"
    const val PAGE_VIEW = "page_view/{pageNumber}"
    const val MANZIL_VIEW = "manzil_view/{manzilNumber}"
    const val AYAH_DETAIL = "ayah_detail/{surahNumber}/{ayahNumber}"
    
    // Audio
    const val RECITER_LIST = "reciter_list"
    const val RECITER_DETAIL = "reciter_detail/{reciterId}"
    const val PLAYLIST = "playlist"
    const val CREATE_PLAYLIST = "create_playlist"
    
    // Search
    const val SEARCH_RESULTS = "search_results/{query}"
    const val VOICE_SEARCH = "voice_search"
    const val ADVANCED_SEARCH = "advanced_search"
    
    // Bookmarks
    const val BOOKMARK_DETAIL = "bookmark_detail/{bookmarkId}"
    const val BOOKMARK_FOLDERS = "bookmark_folders"
    const val CREATE_BOOKMARK = "create_bookmark/{surahNumber}/{ayahNumber}"
    const val EDIT_BOOKMARK = "edit_bookmark/{bookmarkId}"
    
    // Quiz
    const val QUIZ_CATEGORY = "quiz_category"
    const val QUIZ_DIFFICULTY = "quiz_difficulty"
    const val QUIZ_STATISTICS = "quiz_statistics"
    const val DAILY_CHALLENGE = "daily_challenge"
    const val LEADERBOARD = "leaderboard"
    
    // Analytics
    const val STREAK = "streak"
    const val GOALS = "goals"
    const val CREATE_GOAL = "create_goal"
    const val ACHIEVEMENTS = "achievements"
    const val ACHIEVEMENT_DETAIL = "achievement_detail/{achievementId}"
    
    // Profile
    const val EDIT_PROFILE = "edit_profile"
    const val SUBSCRIPTION = "subscription"
    
    // Settings
    const val READING_PREFERENCES = "reading_preferences"
    const val TRANSLATION_SELECTION = "translation_selection"
    const val FONT_SELECTION = "font_selection"
    const val AUDIO_SETTINGS = "audio_settings"
    const val RECITER_SELECTION = "reciter_selection"
    const val EQUALIZER = "equalizer"
    const val NOTIFICATION_SETTINGS = "notification_settings"
    const val REMINDER_SETTINGS = "reminder_settings"
    const val PRIVACY_SETTINGS = "privacy_settings"
    const val SECURITY_SETTINGS = "security_settings"
    const val BACKUP_SETTINGS = "backup_settings"
    const val CLOUD_SYNC = "cloud_sync"
    const val LANGUAGE_SETTINGS = "language_settings"
    const val THEME_SETTINGS = "theme_settings"
    const val DISPLAY_SETTINGS = "display_settings"
    const val DOWNLOAD_SETTINGS = "download_settings"
    const val DATA_USAGE = "data_usage"
    const val STORAGE_MANAGEMENT = "storage_management"
    const val CACHE_MANAGEMENT = "cache_management"
    const val EXPORT_DATA = "export_data"
    const val IMPORT_DATA = "import_data"
    const val ACCOUNT_SETTINGS = "account_settings"
    const val LINKED_ACCOUNTS = "linked_accounts"
    const val ABOUT = "about"
    const val HELP = "help"
    const val FAQ = "faq"
    
    fun reading(surahNumber: Int, ayahNumber: Int = 1) = "reading/$surahNumber/$ayahNumber"
    fun surahDetail(surahNumber: Int) = "surah_detail/$surahNumber"
    fun quizPlay(quizId: Long) = "quiz_play/$quizId"
    fun quizResult(quizId: Long) = "quiz_result/$quizId"
}

object NavArguments {
    const val SURAH_NUMBER = "surahNumber"
    const val AYAH_NUMBER = "ayahNumber"
    const val QUIZ_ID = "quizId"
    const val JUZ_NUMBER = "juzNumber"
    const val PAGE_NUMBER = "pageNumber"
    const val MANZIL_NUMBER = "manzilNumber"
    const val RECITER_ID = "reciterId"
    const val BOOKMARK_ID = "bookmarkId"
    const val ACHIEVEMENT_ID = "achievementId"
    const val QUERY = "query"
}

object DeepLinks {
    const val SCHEME = "alquran"
    const val HOST = "app"
    
    const val READING = "$SCHEME://$HOST/reading"
    const val SURAH = "$SCHEME://$HOST/surah"
    const val BOOKMARK = "$SCHEME://$HOST/bookmark"
    const val QUIZ = "$SCHEME://$HOST/quiz"
}
