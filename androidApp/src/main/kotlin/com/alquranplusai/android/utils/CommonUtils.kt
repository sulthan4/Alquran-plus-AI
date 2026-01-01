package com.alquranplusai.android.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build



object DateTimeUtils {
    
    fun formatReadingTime(milliseconds: Long): String {
        val hours = milliseconds / (1000 * 60 * 60)
        val minutes = (milliseconds % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = (milliseconds % (1000 * 60)) / 1000
        
        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            minutes > 0 -> "${minutes}m ${seconds}s"
            else -> "${seconds}s"
        }
    }
    
    fun formatTimestamp(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        
        return when {
            days > 0 -> "$days days ago"
            hours > 0 -> "$hours hours ago"
            minutes > 0 -> "$minutes minutes ago"
            else -> "Just now"
        }
    }
}

object TextUtils {
    
    fun highlightSearchQuery(text: String, query: String): String {
        if (query.isBlank()) return text
        return text.replace(query, "<b>$query</b>", ignoreCase = true)
    }
    
    fun truncate(text: String, maxLength: Int): String {
        return if (text.length > maxLength) {
            "${text.substring(0, maxLength)}..."
        } else {
            text
        }
    }
    
    fun removeArabicDiacritics(text: String): String {
        return text.replace(Regex("[\\u064B-\\u065F\\u0670]"), "")
    }
}

object ValidationUtils {
    
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    fun isValidSurahNumber(number: Int): Boolean {
        return number in 1..114
    }
    
    fun isValidAyahNumber(surahNumber: Int, ayahNumber: Int): Boolean {
        if (!isValidSurahNumber(surahNumber)) return false
        // Ensure shared module is accessible
        val maxAyahs = com.alquranplusai.utils.QuranUtils.SURAH_VERSE_COUNTS.getOrNull(surahNumber - 1) ?: 0
        return ayahNumber in 1..maxAyahs
    }
}
