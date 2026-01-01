package com.alquranplusai.android.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.navigation.NavController

/**
 * Deep linking utility for navigating to specific content
 */
object DeepLinkHandler {
    
    private const val SCHEME = "alquran"
    private const val HOST = "app"
    
    /**
     * Handle deep link intent
     * Examples:
     * - alquran://app/surah/1
     * - alquran://app/ayah/1/1
     * - alquran://app/juz/1
     * - alquran://app/search?q=patience
     */
    fun handleDeepLink(intent: Intent, navController: NavController): Boolean {
        val data = intent.data ?: return false
        
        if (data.scheme != SCHEME || data.host != HOST) {
            return false
        }
        
        val pathSegments = data.pathSegments
        if (pathSegments.isEmpty()) {
            return false
        }
        
        return when (pathSegments[0]) {
            "surah" -> {
                val surahNumber = pathSegments.getOrNull(1)?.toIntOrNull() ?: return false
                navController.navigate("surah/$surahNumber")
                true
            }
            "ayah" -> {
                val surahNumber = pathSegments.getOrNull(1)?.toIntOrNull() ?: return false
                val ayahNumber = pathSegments.getOrNull(2)?.toIntOrNull() ?: return false
                navController.navigate("ayah/$surahNumber/$ayahNumber")
                true
            }
            "juz" -> {
                val juzNumber = pathSegments.getOrNull(1)?.toIntOrNull() ?: return false
                navController.navigate("juz/$juzNumber")
                true
            }
            "search" -> {
                val query = data.getQueryParameter("q") ?: return false
                navController.navigate("search?q=$query")
                true
            }
            "tafsir" -> {
                val tafsirId = pathSegments.getOrNull(1) ?: return false
                navController.navigate("tafsir/$tafsirId")
                true
            }
            else -> false
        }
    }
    
    /**
     * Create deep link for sharing
     */
    fun createDeepLink(type: String, vararg params: String): Uri {
        val path = (listOf(type) + params).joinToString("/")
        return Uri.parse("$SCHEME://$HOST/$path")
    }
    
    /**
     * Create share intent for ayah
     */
    fun createShareIntent(context: Context, surahNumber: Int, ayahNumber: Int, text: String): Intent {
        val deepLink = createDeepLink("ayah", surahNumber.toString(), ayahNumber.toString())
        return Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "$text\n\n$deepLink")
            putExtra(Intent.EXTRA_SUBJECT, "Quran $surahNumber:$ayahNumber")
        }
    }
}
