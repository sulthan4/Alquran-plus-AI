package com.alquranplusai.domain.models

import kotlinx.serialization.Serializable

/**
 * Tajweed rules for Quranic recitation
 */
@Serializable
enum class TajweedRule {
    // Noon and Meem Rules
    IDGHAM,           // Merging
    IDGHAM_GHUNNAH,   // Merging with nasalization
    IKHFA,            // Concealment
    IQLAB,            // Conversion
    IZHAR,            // Clear pronunciation
    
    // Meem Sakinah Rules
    IKHFA_SHAFAWI,    // Labial concealment
    IDGHAM_SHAFAWI,   // Labial merging
    IZHAR_SHAFAWI,    // Labial clear pronunciation
    
    // Madd (Elongation) Rules
    MADD_NORMAL,      // Normal elongation (2 counts)
    MADD_MUNFASIL,    // Separated elongation (4-5 counts)
    MADD_MUTTASIL,    // Connected elongation (4-5 counts)
    MADD_LAZIM,       // Necessary elongation (6 counts)
    MADD_ARID,        // Presented elongation
    MADD_LIN,         // Soft elongation
    
    // Qalqalah (Echo)
    QALQALAH,         // Echo sound
    QALQALAH_SUGHRA,  // Minor echo
    QALQALAH_KUBRA,   // Major echo
    
    // Other Rules
    GHUNNAH,          // Nasalization
    LEEN,             // Softness
    HAMZAT_WASL,      // Connecting hamza
    SILENT,           // Silent letter
    
    // Lam Rules
    LAM_SHAMSIYYAH,   // Sun letter lam
    LAM_QAMARIYYAH,   // Moon letter lam
    
    NONE              // No special rule
}

/**
 * Tajweed color scheme
 */
object TajweedColors {
    // Standard Tajweed colors (commonly used in Mushafs)
    const val IDGHAM = 0xFF808080          // Gray
    const val IDGHAM_GHUNNAH = 0xFF808080  // Gray
    const val IKHFA = 0xFF9C27B0           // Purple
    const val IQLAB = 0xFF9C27B0           // Purple
    const val IZHAR = 0xFF4CAF50           // Green
    
    const val IKHFA_SHAFAWI = 0xFF9C27B0  // Purple
    const val IDGHAM_SHAFAWI = 0xFF808080 // Gray
    const val IZHAR_SHAFAWI = 0xFF4CAF50  // Green
    
    const val MADD = 0xFFFF5722            // Deep Orange/Red
    const val MADD_MUNFASIL = 0xFFFF5722  // Deep Orange
    const val MADD_MUTTASIL = 0xFFFF5722  // Deep Orange
    const val MADD_LAZIM = 0xFFFF5722     // Deep Orange
    
    const val QALQALAH = 0xFF2196F3       // Blue
    const val GHUNNAH = 0xFF808080        // Gray
    const val LEEN = 0xFFFF9800           // Orange
    
    const val LAM_SHAMSIYYAH = 0xFF808080 // Gray
    const val LAM_QAMARIYYAH = 0xFF4CAF50 // Green
    
    const val HAMZAT_WASL = 0xFFFF9800    // Orange
    const val SILENT = 0xFFBDBDBD         // Light Gray
    
    const val NONE = 0xFF000000           // Black (default)
    
    /**
     * Get color for a specific Tajweed rule
     */
    fun getColorForRule(rule: TajweedRule): Long {
        return when (rule) {
            TajweedRule.IDGHAM, TajweedRule.IDGHAM_GHUNNAH, TajweedRule.IDGHAM_SHAFAWI -> IDGHAM
            TajweedRule.IKHFA, TajweedRule.IQLAB, TajweedRule.IKHFA_SHAFAWI -> IKHFA
            TajweedRule.IZHAR, TajweedRule.IZHAR_SHAFAWI, TajweedRule.LAM_QAMARIYYAH -> IZHAR
            TajweedRule.MADD_NORMAL, TajweedRule.MADD_MUNFASIL, 
            TajweedRule.MADD_MUTTASIL, TajweedRule.MADD_LAZIM, 
            TajweedRule.MADD_ARID, TajweedRule.MADD_LIN -> MADD
            TajweedRule.QALQALAH, TajweedRule.QALQALAH_SUGHRA, TajweedRule.QALQALAH_KUBRA -> QALQALAH
            TajweedRule.GHUNNAH -> GHUNNAH
            TajweedRule.LEEN -> LEEN
            TajweedRule.LAM_SHAMSIYYAH -> LAM_SHAMSIYYAH
            TajweedRule.HAMZAT_WASL -> HAMZAT_WASL
            TajweedRule.SILENT -> SILENT
            TajweedRule.NONE -> NONE
        }
    }
}

/**
 * Represents a Tajweed annotation for a word or letter
 */
@Serializable
data class TajweedAnnotation(
    val rule: TajweedRule,
    val startIndex: Int,
    val endIndex: Int,
    val description: String? = null
)

