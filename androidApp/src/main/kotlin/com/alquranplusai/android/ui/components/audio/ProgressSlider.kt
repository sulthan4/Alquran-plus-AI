package com.alquranplusai.android.ui.components.audio

import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProgressSlider(
    progress: Float,
    onProgressChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Slider(value = progress, onValueChange = onProgressChange, modifier = modifier)
}
