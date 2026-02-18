package com.alquranplusai.android.ui.components.quran

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Composable for displaying a single Ayah with proper Arabic font, RTL layout,
 * Tajweed coloring option, and translation.
 */
@Composable
fun AyahView(
    ayahNumber: Int,
    arabicText: String,
    translation: String?,
    modifier: Modifier = Modifier,
    showTajweed: Boolean = false,
    fontSize: Float = 26f
) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        // Ayah number badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = ayahNumber.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Arabic text — use TajweedTextView or ArabicTextView based on preference
        if (showTajweed) {
            TajweedTextView(
                text = arabicText,
                modifier = Modifier.fillMaxWidth(),
                fontSize = fontSize.sp
            )
        } else {
            ArabicTextView(
                text = arabicText,
                modifier = Modifier.fillMaxWidth(),
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Normal,
                contentDesc = "Ayah $ayahNumber"
            )
        }

        // Translation
        translation?.let {
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
