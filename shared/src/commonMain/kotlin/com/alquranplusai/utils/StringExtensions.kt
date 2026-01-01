package com.alquranplusai.utils

/**
 * String extension functions
 */

/**
 * Check if string is a valid email
 */
fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return this.matches(emailRegex)
}

/**
 * Check if string is a valid password (min 8 chars, 1 uppercase, 1 lowercase, 1 digit)
 */
fun String.isValidPassword(): Boolean {
    return this.length >= 8 &&
            this.any { it.isUpperCase() } &&
            this.any { it.isLowerCase() } &&
            this.any { it.isDigit() }
}

/**
 * Capitalize first letter
 */
fun String.capitalizeFirst(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

/**
 * Truncate string to max length with ellipsis
 */
fun String.truncate(maxLength: Int, ellipsis: String = "..."): String {
    return if (this.length <= maxLength) this
    else this.take(maxLength - ellipsis.length) + ellipsis
}

/**
 * Remove Arabic diacritics
 */
fun String.removeArabicDiacritics(): String {
    val diacritics = charArrayOf(
        '\u064B', '\u064C', '\u064D', '\u064E', '\u064F',
        '\u0650', '\u0651', '\u0652', '\u0653', '\u0654',
        '\u0655', '\u0656', '\u0657', '\u0658', '\u0670'
    )
    return this.filterNot { it in diacritics }
}

/**
 * Check if string contains Arabic characters
 */
fun String.containsArabic(): Boolean {
    return this.any { it in '\u0600'..'\u06FF' || it in '\u0750'..'\u077F' }
}

/**
 * Convert to Arabic-Indic numerals
 */
fun String.toArabicNumerals(): String {
    val arabicNumerals = arrayOf("٠", "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩")
    var result = this
    for (i in 0..9) {
        result = result.replace(i.toString(), arabicNumerals[i])
    }
    return result
}

/**
 * Format Surah and Ayah reference
 */
fun formatAyahReference(surahNumber: Int, ayahNumber: Int): String {
    return "$surahNumber:$ayahNumber"
}

/**
 * Parse Ayah reference (e.g., "2:255" -> Pair(2, 255))
 */
fun String.parseAyahReference(): Pair<Int, Int>? {
    val parts = this.split(":")
    return if (parts.size == 2) {
        val surah = parts[0].toIntOrNull()
        val ayah = parts[1].toIntOrNull()
        if (surah != null && ayah != null) Pair(surah, ayah) else null
    } else null
}
