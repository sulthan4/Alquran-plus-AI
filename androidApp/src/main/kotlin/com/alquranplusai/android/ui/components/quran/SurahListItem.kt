package com.alquranplusai.android.ui.components.quran

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SurahListItem(
    surahNumber: Int,
    surahName: String,
    surahNameArabic: String,
    ayahCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text("$surahNumber. $surahName") },
        supportingContent = { Text("$ayahCount Ayahs") },
        trailingContent = { Text(surahNameArabic, style = MaterialTheme.typography.headlineSmall) },
        modifier = modifier.fillMaxWidth()
    )
}
