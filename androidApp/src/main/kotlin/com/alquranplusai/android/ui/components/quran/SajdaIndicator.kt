package com.alquranplusai.android.ui.components.quran

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SajdaIndicator(modifier: Modifier = Modifier) {
    Icon(
        imageVector = androidx.compose.material.icons.Icons.Default.Star,
        contentDescription = "Sajda",
        tint = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}
