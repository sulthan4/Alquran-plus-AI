package com.alquranplusai.android.ui.screens.quran

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SurahDetailScreen(
    surahNumber: Int,
    onNavigateBack: () -> Unit = {},
    onNavigateToReading: (Int, Int) -> Unit = { _, _ -> }
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Surah Detail - Surah $surahNumber")
    }
}
