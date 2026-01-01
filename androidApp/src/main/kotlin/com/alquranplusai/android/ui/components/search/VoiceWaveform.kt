package com.alquranplusai.android.ui.components.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VoiceWaveform(
    amplitude: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height((amplitude * 100).dp)
            .width(4.dp)
            .background(MaterialTheme.colorScheme.primary)
    )
}
