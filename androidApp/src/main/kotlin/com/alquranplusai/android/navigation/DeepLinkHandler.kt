package com.alquranplusai.android.navigation

import android.content.Intent
import android.net.Uri
import androidx.navigation.NavController

/**
 * Handles deep links for the app
 */
class DeepLinkHandler {
    
    fun handleDeepLink(intent: Intent, navController: NavController) {
        val data: Uri? = intent.data
        
        data?.let { uri ->
            when (uri.host) {
                "surah" -> {
                    val surahId = uri.lastPathSegment?.toIntOrNull()
                    surahId?.let {
                        navController.navigate("surah_detail/$it")
                    }
                }
                "ayah" -> {
                    val pathSegments = uri.pathSegments
                    if (pathSegments.size >= 2) {
                        val surahId = pathSegments[0].toIntOrNull()
                        val ayahId = pathSegments[1].toIntOrNull()
                        if (surahId != null && ayahId != null) {
                            navController.navigate("ayah_detail/$surahId/$ayahId")
                        }
                    }
                }
                "juz" -> {
                    val juzNumber = uri.lastPathSegment?.toIntOrNull()
                    juzNumber?.let {
                        navController.navigate("juz_view/$it")
                    }
                }
                "bookmark" -> {
                    navController.navigate(NavRoutes.BOOKMARKS)
                }
                "quiz" -> {
                    val quizId = uri.lastPathSegment
                    quizId?.let {
                        navController.navigate("quiz_play/$it")
                    }
                }
                else -> {
                    // Default to home
                    navController.navigate(NavRoutes.HOME)
                }
            }
        }
    }
    
    companion object {
        const val SCHEME = "alquranplusai"
        const val HOST_SURAH = "surah"
        const val HOST_AYAH = "ayah"
        const val HOST_JUZ = "juz"
        const val HOST_BOOKMARK = "bookmark"
        const val HOST_QUIZ = "quiz"
    }
}
