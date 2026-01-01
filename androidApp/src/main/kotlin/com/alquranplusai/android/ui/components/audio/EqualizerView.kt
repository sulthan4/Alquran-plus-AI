package com.alquranplusai.android.ui.components.audio

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EqualizerView(
    bands: List<Float>,
    onBandChange: (Int, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("Equalizer", style = MaterialTheme.typography.titleMedium)
        bands.forEachIndexed { index, value ->
            EqualizerBand(
                bandNumber = index + 1,
                value = value,
                onValueChange = { onBandChange(index, it) }
            )
        }
    }
}
