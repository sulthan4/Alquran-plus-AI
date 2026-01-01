package com.alquranplusai.utils

/**
 * Validation utilities
 */
object ValidationUtils {
    
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }
    
    fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }
    
    fun isValidUsername(username: String): Boolean {
        return username.length >= 3 && username.matches("[a-zA-Z0-9_]+".toRegex())
    }
    
    fun isValidSurahNumber(number: Int): Boolean {
        return number in 1..114
    }
    
    fun isValidAyahNumber(surahNumber: Int, ayahNumber: Int): Boolean {
        // Simplified validation - in real app, check against actual surah ayah counts
        return ayahNumber > 0
    }
    
    fun isValidJuzNumber(number: Int): Boolean {
        return number in 1..30
    }
    
    fun isValidPageNumber(number: Int): Boolean {
        return number in 1..604
    }
}
