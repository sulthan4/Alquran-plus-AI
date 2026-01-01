package com.alquranplusai.android.ui.components.quran

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TranslationView(
    translation: String,
    translatorName: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = translation, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "— $translatorName", style = MaterialTheme.typography.bodySmall)
    }
}
