package com.alquranplusai.android.ui.components.quran

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SurahHeader(
    surahName: String,
    surahNameArabic: String,
    revelationType: String,
    ayahCount: Int,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = surahNameArabic, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = surahName, style = MaterialTheme.typography.titleLarge)
            Text(text = "$revelationType • $ayahCount Ayahs", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
