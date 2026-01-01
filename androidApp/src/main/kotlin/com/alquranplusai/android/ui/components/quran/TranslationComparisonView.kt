package com.alquranplusai.android.ui.components.quran

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alquranplusai.domain.models.TranslationDisplayMode

/**
 * Translation comparison view
 */
@Composable
fun TranslationComparisonView(
    translations: List<Pair<String, String>>, // Pair of (translationName, text)
    displayMode: TranslationDisplayMode,
    modifier: Modifier = Modifier
) {
    when (displayMode) {
        TranslationDisplayMode.SINGLE -> {
            // Show only first translation
            translations.firstOrNull()?.let { (name, text) ->
                TranslationCard(name, text)
            }
        }
        TranslationDisplayMode.STACKED -> {
            // Show all translations stacked vertically
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                translations.forEach { (name, text) ->
                    TranslationCard(name, text)
                }
            }
        }
        TranslationDisplayMode.SIDE_BY_SIDE -> {
            // Show translations side by side (2 columns)
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                translations.chunked(2).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { (name, text) ->
                            TranslationCard(
                                name, text,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TranslationCard(
    name: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
