package com.alquranplusai.android.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.alquranplusai.domain.models.TajweedAnnotation
import com.alquranplusai.domain.models.TajweedColors
import com.alquranplusai.domain.models.TajweedRule

/**
 * Helper class for applying Tajweed colors to Quranic text
 */
object TajweedHelper {
    
    /**
     * Apply Tajweed colors to Arabic text based on annotations
     */
    fun applyTajweedColors(
        text: String,
        annotations: List<TajweedAnnotation>,
        enabled: Boolean = true
    ): AnnotatedString {
        if (!enabled || annotations.isEmpty()) {
            return AnnotatedString(text)
        }
        
        return buildAnnotatedString {
            append(text)
            
            // Apply color spans for each annotation
            annotations.forEach { annotation ->
                val color = Color(TajweedColors.getColorForRule(annotation.rule))
                
                addStyle(
                    style = SpanStyle(color = color),
                    start = annotation.startIndex,
                    end = annotation.endIndex.coerceAtMost(text.length)
                )
            }
        }
    }
    
    /**
     * Detect basic Tajweed rules in Arabic text (simplified detection)
     * Note: This is a basic implementation. Full Tajweed requires linguistic analysis.
     */
    fun detectBasicTajweed(arabicText: String): List<TajweedAnnotation> {
        val annotations = mutableListOf<TajweedAnnotation>()
        
        // Detect Tanween (double diacritics) - often indicates Idgham/Ikhfa
        val tanweenPattern = Regex("[ًٌٍ]")
        tanweenPattern.findAll(arabicText).forEach { match ->
            annotations.add(
                TajweedAnnotation(
                    rule = TajweedRule.GHUNNAH,
                    startIndex = match.range.first,
                    endIndex = match.range.last + 1
                )
            )
        }
        
        // Detect Madd letters (ا و ي with specific diacritics)
        val maddPattern = Regex("[اوي][ًٌٍَُِّْ]")
        maddPattern.findAll(arabicText).forEach { match ->
            annotations.add(
                TajweedAnnotation(
                    rule = TajweedRule.MADD_NORMAL,
                    startIndex = match.range.first,
                    endIndex = match.range.last + 1
                )
            )
        }
        
        // Detect Qalqalah letters (ق ط ب ج د)
        val qalqalahLetters = setOf('ق', 'ط', 'ب', 'ج', 'د')
        arabicText.forEachIndexed { index, char ->
            if (char in qalqalahLetters) {
                // Check if it has sukoon (ْ) - indicates Qalqalah
                if (index + 1 < arabicText.length && arabicText[index + 1] == 'ْ') {
                    annotations.add(
                        TajweedAnnotation(
                            rule = TajweedRule.QALQALAH,
                            startIndex = index,
                            endIndex = index + 2
                        )
                    )
                }
            }
        }
        
        // Detect Noon Sakinah (نْ) and Tanween for Idgham/Ikhfa/Iqlab/Izhar
        val noonSakinahPattern = Regex("نْ")
        noonSakinahPattern.findAll(arabicText).forEach { match ->
            val nextCharIndex = match.range.last + 1
            if (nextCharIndex < arabicText.length) {
                val nextChar = arabicText[nextCharIndex]
                val rule = when (nextChar) {
                    in setOf('ي', 'ر', 'م', 'ل', 'و', 'ن') -> TajweedRule.IDGHAM
                    'ب' -> TajweedRule.IQLAB
                    in setOf('ص', 'ذ', 'ث', 'ك', 'ج', 'ش', 'ق', 'س', 'د', 'ط', 'ز', 'ف', 'ت', 'ض', 'ظ') -> TajweedRule.IKHFA
                    else -> TajweedRule.IZHAR
                }
                
                annotations.add(
                    TajweedAnnotation(
                        rule = rule,
                        startIndex = match.range.first,
                        endIndex = match.range.last + 1
                    )
                )
            }
        }
        
        return annotations
    }
    
    /**
     * Get description for a Tajweed rule
     */
    fun getRuleDescription(rule: TajweedRule): String {
        return when (rule) {
            TajweedRule.IDGHAM -> "Idgham - Merge the sound"
            TajweedRule.IDGHAM_GHUNNAH -> "Idgham with Ghunnah - Merge with nasalization"
            TajweedRule.IKHFA -> "Ikhfa - Conceal the sound"
            TajweedRule.IQLAB -> "Iqlab - Convert to 'b' sound"
            TajweedRule.IZHAR -> "Izhar - Pronounce clearly"
            TajweedRule.QALQALAH -> "Qalqalah - Echo sound"
            TajweedRule.MADD_NORMAL -> "Madd - Elongate 2 counts"
            TajweedRule.MADD_MUNFASIL -> "Madd Munfasil - Elongate 4-5 counts"
            TajweedRule.MADD_MUTTASIL -> "Madd Muttasil - Elongate 4-5 counts"
            TajweedRule.MADD_LAZIM -> "Madd Lazim - Elongate 6 counts"
            TajweedRule.GHUNNAH -> "Ghunnah - Nasalization"
            TajweedRule.LAM_SHAMSIYYAH -> "Lam Shamsiyyah - Silent lam"
            TajweedRule.LAM_QAMARIYYAH -> "Lam Qamariyyah - Pronounced lam"
            else -> rule.name.replace('_', ' ').lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() }
        }
    }
}

