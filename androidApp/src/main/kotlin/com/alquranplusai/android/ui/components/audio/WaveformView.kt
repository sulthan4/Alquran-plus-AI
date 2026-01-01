package com.alquranplusai.android.ui.components.audio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WaveformView(
    waveformData: List<Float>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        waveformData.forEach { value ->
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height((value * 100).dp)
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
    }
}
