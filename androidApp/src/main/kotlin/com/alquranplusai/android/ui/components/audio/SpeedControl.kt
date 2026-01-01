package com.alquranplusai.android.ui.components.audio

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SpeedControl(
    speed: Float,
    onSpeedChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("Playback Speed: ${speed}x")
        Slider(value = speed, onValueChange = onSpeedChange, valueRange = 0.5f..2.0f)
    }
}
