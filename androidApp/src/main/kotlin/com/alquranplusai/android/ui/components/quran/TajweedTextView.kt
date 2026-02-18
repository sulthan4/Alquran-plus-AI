package com.alquranplusai.android.ui.components.quran

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.alquranplusai.android.ui.theme.ScheherazadeFontFamily

/**
 * Tajweed color definitions based on standard Tajweed coloring conventions.
 */
object TajweedColors {
    val Ghunnah = Color(0xFF00AA00)   // Green  - nasal sounds (ن م with shaddah)
    val Ikhfa = Color(0xFF0055CC)     // Blue   - concealment
    val Idgham = Color(0xFFCC6600)    // Orange - merging
    val Qalqalah = Color(0xFF990099)  // Purple - echoing letters (ق ط ب ج د)
    val Madd = Color(0xFFCC0000)      // Red    - elongation (ا و ي after vowel)
    val Lam = Color(0xFF006666)       // Teal   - lam in Allah (الله)
}

// Tajweed letter sets
private val QALQALAH_LETTERS = setOf('\u0642', '\u0637', '\u0628', '\u062C', '\u062F') // ق ط ب ج د
private val GHUNNAH_LETTERS = setOf('\u0646', '\u0645')  // ن م
private val MADD_LETTERS = setOf('\u0627', '\u0648', '\u064A')  // ا و ي
private const val SHADDAH = '\u0651'   // ّ
private const val SUKUN = '\u0652'     // ْ
private val SHORT_VOWELS = setOf('\u064E', '\u064F', '\u0650') // fatha, damma, kasra
private const val ALLAH_PATTERN = "\u0627\u0644\u0644\u0647" // الله

/**
 * Builds an AnnotatedString with Tajweed coloring applied to Arabic Quranic text.
 */
fun buildTajweedAnnotatedString(text: String): AnnotatedString {
    return buildAnnotatedString {
        var i = 0
        while (i < text.length) {
            val char = text[i]
            val nextChar = if (i + 1 < text.length) text[i + 1] else null
            val prevChar = if (i > 0) text[i - 1] else null

            when {
                // Allah (الله) — special lam coloring
                i + 3 < text.length && text.substring(i, minOf(i + 4, text.length)) == ALLAH_PATTERN -> {
                    withStyle(SpanStyle(color = TajweedColors.Lam)) {
                        append(ALLAH_PATTERN)
                    }
                    i += 4
                }

                // Ghunnah: ن or م followed by shaddah
                char in GHUNNAH_LETTERS && nextChar == SHADDAH -> {
                    withStyle(SpanStyle(color = TajweedColors.Ghunnah)) {
                        append(char)
                        append(nextChar)
                    }
                    i += 2
                }

                // Qalqalah: ق ط ب ج د with sukun or at end of word
                char in QALQALAH_LETTERS -> {
                    val hasSukun = nextChar == SUKUN
                    val isEndOfWord = nextChar == null || nextChar == ' '
                    if (hasSukun || isEndOfWord) {
                        withStyle(SpanStyle(color = TajweedColors.Qalqalah)) {
                            append(char)
                        }
                    } else {
                        append(char)
                    }
                    i++
                }

                // Madd: ا و ي after a short vowel
                char in MADD_LETTERS && prevChar in SHORT_VOWELS -> {
                    withStyle(SpanStyle(color = TajweedColors.Madd)) {
                        append(char)
                    }
                    i++
                }

                else -> {
                    append(char)
                    i++
                }
            }
        }
    }
}

/**
 * A composable that renders Arabic Quranic text with Tajweed coloring.
 * Colors specific letters/patterns according to standard Tajweed rules.
 *
 * @param text The Arabic Quranic text to display
 * @param enabled Whether Tajweed coloring is active (defaults to true)
 * @param fontSize Font size for the text
 */
@Composable
fun TajweedTextView(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 24.sp,
    enabled: Boolean = true
) {
    val annotatedText = remember(text, enabled) {
        if (enabled) buildTajweedAnnotatedString(text) else AnnotatedString(text)
    }

    Text(
        text = annotatedText,
        style = TextStyle(
            fontFamily = ScheherazadeFontFamily,
            fontSize = fontSize,
            textAlign = TextAlign.End,
            textDirection = TextDirection.Rtl
        ),
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Quranic verse with Tajweed coloring" }
    )
}
