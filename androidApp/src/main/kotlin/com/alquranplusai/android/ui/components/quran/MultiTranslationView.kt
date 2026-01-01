package com.alquranplusai.android.ui.components.quran

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alquranplusai.domain.models.AyahTranslation

/**
 * Component to display multiple translations side-by-side or stacked
 */
@Composable
fun MultiTranslationView(
    translations: List<AyahTranslation>,
    modifier: Modifier = Modifier,
    displayMode: TranslationDisplayMode = TranslationDisplayMode.STACKED
) {
    when (displayMode) {
        TranslationDisplayMode.SINGLE -> {
            if (translations.isNotEmpty()) {
                TranslationItem(translations.first())
            }
        }
        TranslationDisplayMode.STACKED -> {
            StackedTranslations(translations, modifier)
        }
        TranslationDisplayMode.SIDE_BY_SIDE -> {
            SideBySideTranslations(translations, modifier)
        }
    }
}

@Composable
private fun StackedTranslations(
    translations: List<AyahTranslation>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        translations.forEach { translation ->
            TranslationItem(translation)
        }
    }
}

@Composable
private fun SideBySideTranslations(
    translations: List<AyahTranslation>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        translations.forEach { translation ->
            Card(
                modifier = Modifier.width(300.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                TranslationItem(translation)
            }
        }
    }
}

@Composable
private fun TranslationItem(
    translation: AyahTranslation,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Translation name/author
        Text(
            text = getTranslationDisplayName(translation.translationId),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        
        // Translation text
        Text(
            text = translation.text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.5
        )
    }
}

/**
 * Get display name for translation ID
 */
private fun getTranslationDisplayName(translationId: String): String {
    return when (translationId) {
        "en_sahih" -> "Sahih International"
        "en_pickthall" -> "Pickthall"
        "en_yusufali" -> "Yusuf Ali"
        "en_shakir" -> "Shakir"
        "ur_jalandhry" -> "Jalandhry (Urdu)"
        "ar_muyassar" -> "Al-Muyassar (Arabic)"
        else -> translationId.replaceFirstChar { it.uppercase() }
    }
}

/**
 * Translation display mode
 */
enum class TranslationDisplayMode {
    SINGLE,
    STACKED,
    SIDE_BY_SIDE
}

/**
 * Translation selector chip
 */
@Composable
fun TranslationSelectorChip(
    translationId: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(getTranslationDisplayName(translationId)) },
        modifier = modifier
    )
}
