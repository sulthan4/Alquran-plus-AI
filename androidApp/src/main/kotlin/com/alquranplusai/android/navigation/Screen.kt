package com.alquranplusai.android.navigation

/**
 * Navigation routes for the app
 */
sealed class Screen(val route: String) {
    // Main screens
    object Home : Screen("home")
    object SurahList : Screen("surah_list")
    object Reading : Screen("reading/{surahNumber}/{ayahNumber}") {
        fun createRoute(surahNumber: Int, ayahNumber: Int = 1) = "reading/$surahNumber/$ayahNumber"
    }
    
    // Juz navigation
    object JuzList : Screen("juz_list")
    object JuzReading : Screen("juz/{juzNumber}") {
        fun createRoute(juzNumber: Int) = "juz/$juzNumber"
    }
    
    // Audio
    object AudioPlayer : Screen("audio_player")
    object ReciterList : Screen("reciter_list")
    
    // Bookmarks
    object Bookmarks : Screen("bookmarks")
    object BookmarkFolders : Screen("bookmark_folders")
    
    // Search
    object Search : Screen("search")
    object SearchResults : Screen("search_results/{query}") {
        fun createRoute(query: String) = "search_results/$query"
    }
    
    // Quiz
    object QuizList : Screen("quiz_list")
    object QuizPlay : Screen("quiz/{quizId}") {
        fun createRoute(quizId: String) = "quiz/$quizId"
    }
    object QuizResults : Screen("quiz_results/{sessionId}") {
        fun createRoute(sessionId: String) = "quiz_results/$sessionId"
    }
    
    // Profile & Settings
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object SettingsReading : Screen("settings/reading")
    object SettingsAudio : Screen("settings/audio")
    object SettingsNotifications : Screen("settings/notifications")
    object SettingsPrivacy : Screen("settings/privacy")
    
    // Analytics
    object Analytics : Screen("analytics")
    object AnalyticsWeekly : Screen("analytics/weekly")
    object AnalyticsMonthly : Screen("analytics/monthly")
}
