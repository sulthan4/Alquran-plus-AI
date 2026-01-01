package com.alquranplusai.utils

object ArabicTextUtils {
    
    fun removeDiacritics(text: String): String {
        return text.replace(Regex("[\u064B-\u065F\u0670]"), "")
    }
    
    fun normalizeTashkeel(text: String): String {
        return text
            .replace("\u0671", "\u0627") // Alif wasla to Alif
            .replace("\u0649", "\u064A") // Alif maqsura to Ya
    }
    
    fun isArabic(char: Char): Boolean {
        return char in '\u0600'..'\u06FF' || char in '\u0750'..'\u077F'
    }
    
    fun getWordCount(text: String): Int {
        return text.trim().split(Regex("\\s+")).size
    }
    
    fun highlightSearchTerm(text: String, searchTerm: String): String {
        if (searchTerm.isBlank()) return text
        val normalized = removeDiacritics(text)
        val normalizedSearch = removeDiacritics(searchTerm)
        return text.replace(normalizedSearch, "<mark>$normalizedSearch</mark>", ignoreCase = true)
    }
}

object QuranUtils {
    
    fun getSurahName(number: Int): String {
        return "Surah $number" // Should use Repository for localized name, but this is a safe fallback
    }

    val SURAH_VERSE_COUNTS = intArrayOf(
        7, 286, 200, 176, 120, 165, 206, 75, 129, 109,
        123, 111, 43, 52, 99, 128, 111, 110, 98, 135,
        112, 78, 118, 64, 77, 227, 93, 88, 69, 60,
        34, 30, 73, 54, 45, 83, 182, 88, 75, 85,
        54, 53, 89, 59, 37, 35, 38, 29, 18, 45,
        60, 49, 62, 55, 78, 96, 29, 22, 24, 13,
        14, 11, 11, 18, 12, 12, 30, 52, 52, 44,
        28, 28, 20, 56, 40, 31, 50, 40, 46, 42,
        29, 19, 36, 25, 22, 17, 19, 26, 30, 20,
        15, 21, 11, 8, 8, 19, 5, 8, 8, 11,
        11, 8, 3, 9, 5, 4, 7, 3, 6, 3,
        5, 4, 5, 6
    )
    
    fun getJuzNumber(surahNumber: Int, ayahNumber: Int): Int {
        // Simplified mapping - TODO: Implement accurate juz mapping
        return when {
            surahNumber == 1 -> 1
            surahNumber == 2 && ayahNumber <= 141 -> 1
            surahNumber == 2 && ayahNumber <= 252 -> 2
            surahNumber == 2 -> 3
            else -> ((surahNumber - 1) / 4) + 1
        }.coerceIn(1, 30)
    }
    
    fun getPageNumber(surahNumber: Int, ayahNumber: Int): Int {
        // Simplified mapping - TODO: Implement accurate page mapping
        return when (surahNumber) {
            1 -> 1
            2 -> 2 + (ayahNumber / 10)
            else -> ((surahNumber - 1) * 20) + (ayahNumber / 10)
        }.coerceIn(1, 604)
    }
    
    fun formatAyahReference(surahNumber: Int, ayahNumber: Int): String {
        return "$surahNumber:$ayahNumber"
    }
    
    fun parseAyahReference(reference: String): Pair<Int, Int>? {
        val parts = reference.split(":")
        return if (parts.size == 2) {
            val surah = parts[0].toIntOrNull()
            val ayah = parts[1].toIntOrNull()
            if (surah != null && ayah != null) Pair(surah, ayah) else null
        } else null
    }
}

object TimeUtils {
    
    fun formatDuration(milliseconds: Long): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / (1000 * 60)) % 60
        val hours = milliseconds / (1000 * 60 * 60)
        
        return when {
            hours > 0 -> "${hours}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
            minutes > 0 -> "${minutes}:${seconds.toString().padStart(2, '0')}"
            else -> "0:${seconds.toString().padStart(2, '0')}"
        }
    }
    
    fun formatReadingTime(milliseconds: Long): String {
        val hours = milliseconds / (1000 * 60 * 60)
        val minutes = (milliseconds % (1000 * 60 * 60)) / (1000 * 60)
        
        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            minutes > 0 -> "${minutes}m"
            else -> "< 1m"
        }
    }
    
    fun getRelativeTime(timestamp: Long): String {
        val now = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
        val diff = now - timestamp
        
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        
        return when {
            days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
            hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
            minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
            else -> "Just now"
        }
    }
}
