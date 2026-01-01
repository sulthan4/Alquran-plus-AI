package com.alquranplusai.android.ui.components.quran

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WordByWordView(
    arabicWord: String,
    transliteration: String,
    translation: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = arabicWord, style = MaterialTheme.typography.titleLarge)
        Text(text = transliteration, style = MaterialTheme.typography.bodySmall)
        Text(text = translation, style = MaterialTheme.typography.bodyMedium)
    }
}
