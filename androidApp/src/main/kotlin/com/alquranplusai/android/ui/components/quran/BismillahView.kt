package com.alquranplusai.android.ui.components.quran

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BismillahView(modifier: Modifier = Modifier) {
    Text(
        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier.fillMaxWidth().padding(16.dp)
    )
}
