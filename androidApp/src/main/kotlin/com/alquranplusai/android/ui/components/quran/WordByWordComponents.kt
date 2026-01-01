package com.alquranplusai.android.ui.components.quran

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

/**
 * Component for displaying Quran text with word-by-word highlighting
 */
@Composable
fun WordByWordHighlightedText(
    text: String,
    words: List<String>,
    currentWordIndex: Int,
    modifier: Modifier = Modifier,
    onWordClick: (Int) -> Unit = {}
) {
    val annotatedString = buildAnnotatedString {
        words.forEachIndexed { index, word ->
            withStyle(
                style = SpanStyle(
                    background = if (index == currentWordIndex) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    } else {
                        Color.Transparent
                    },
                    color = if (index == currentWordIndex) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            ) {
                append(word)
            }
            if (index < words.size - 1) {
                append(" ")
            }
        }
    }
    
    Text(
        text = annotatedString,
        modifier = modifier,
        style = MaterialTheme.typography.headlineMedium
    )
}

/**
 * Word-by-word display with translation
 */
@Composable
fun WordByWordDisplay(
    arabicWord: String,
    transliteration: String,
    translation: String,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlighted) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                arabicWord,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                transliteration,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                translation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Repeat mode selector
 */
@Composable
fun RepeatModeSelector(
    currentMode: RepeatMode,
    onModeChange: (RepeatMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Repeat Mode",
                style = MaterialTheme.typography.titleMedium
            )
            
            RepeatMode.values().forEach { mode ->
                FilterChip(
                    selected = currentMode == mode,
                    onClick = { onModeChange(mode) },
                    label = { Text(mode.displayName) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

enum class RepeatMode(val displayName: String) {
    NONE("No Repeat"),
    AYAH("Repeat Ayah"),
    SURAH("Repeat Surah"),
    RANGE("Repeat Range")
}
