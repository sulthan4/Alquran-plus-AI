package com.alquranplusai.android.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * FormatUtils - Utility class for formatting various data types
 */
object FormatUtils {
    
    /**
     * Format time in milliseconds to MM:SS format
     */
    fun formatTime(millis: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format(Locale.US, "%02d:%02d", minutes, seconds)
    }
    
    /**
     * Format time in milliseconds to HH:MM:SS format
     */
    fun formatTimeWithHours(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(hours)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours)
        return if (hours > 0) {
            String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.US, "%02d:%02d", minutes, seconds)
        }
    }
    
    /**
     * Format date to readable string
     */
    fun formatDate(timestamp: Long, pattern: String = "MMM dd, yyyy"): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    /**
     * Format number with thousands separator
     */
    fun formatNumber(number: Int): String {
        return DecimalFormat("#,###").format(number)
    }
    
    /**
     * Format percentage
     */
    fun formatPercentage(value: Float): String {
        return String.format(Locale.US, "%.1f%%", value * 100)
    }
    
    /**
     * Format file size in bytes to human readable format
     */
    fun formatFileSize(bytes: Long): String {
        val kb = bytes / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0
        
        return when {
            gb >= 1 -> String.format(Locale.US, "%.2f GB", gb)
            mb >= 1 -> String.format(Locale.US, "%.2f MB", mb)
            kb >= 1 -> String.format(Locale.US, "%.2f KB", kb)
            else -> "$bytes B"
        }
    }
    
    /**
     * Format Surah and Ayah reference
     */
    fun formatAyahReference(surahNumber: Int, ayahNumber: Int): String {
        return "$surahNumber:$ayahNumber"
    }
    
    /**
     * Format Juz number
     */
    fun formatJuzNumber(juzNumber: Int): String {
        return "Juz $juzNumber"
    }
    
    /**
     * Format Page number
     */
    fun formatPageNumber(pageNumber: Int): String {
        return "Page $pageNumber"
    }
    
    /**
     * Format Arabic number (convert to Eastern Arabic numerals)
     */
    fun formatArabicNumber(number: Int): String {
        val arabicNumerals = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        return number.toString().map { 
            if (it.isDigit()) arabicNumerals[it.toString().toInt()] else it 
        }.joinToString("")
    }
    
    /**
     * Format duration in seconds to readable format
     */
    fun formatDuration(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        
        return when {
            hours > 0 -> String.format(Locale.US, "%dh %dm", hours, minutes)
            minutes > 0 -> String.format(Locale.US, "%dm %ds", minutes, secs)
            else -> String.format(Locale.US, "%ds", secs)
        }
    }
    
    /**
     * Format reading speed (words per minute)
     */
    fun formatReadingSpeed(wordsPerMinute: Int): String {
        return "$wordsPerMinute WPM"
    }
    
    /**
     * Format streak count
     */
    fun formatStreak(days: Int): String {
        return when (days) {
            1 -> "1 day"
            else -> "$days days"
        }
    }
}
