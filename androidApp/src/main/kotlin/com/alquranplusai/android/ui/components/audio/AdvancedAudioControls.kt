package com.alquranplusai.android.ui.components.audio

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Audio equalizer component
 */
@Composable
fun AudioEqualizer(
    bands: List<Float>,
    onBandChange: (Int, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Equalizer",
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                bands.forEachIndexed { index, value ->
                    EqualizerBand(
                        value = value,
                        onValueChange = { onBandChange(index, it) },
                        label = getBandLabel(index)
                    )
                }
            }
        }
    }
}

@Composable
fun EqualizerBand(
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = -12f..12f,
            modifier = Modifier.height(150.dp)
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

fun getBandLabel(index: Int): String {
    return when (index) {
        0 -> "60Hz"
        1 -> "230Hz"
        2 -> "910Hz"
        3 -> "3.6kHz"
        4 -> "14kHz"
        else -> "${index}Hz"
    }
}

/**
 * Sleep timer component
 */
@Composable
fun SleepTimer(
    isActive: Boolean,
    remainingMinutes: Int,
    onSetTimer: (Int) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Sleep Timer",
                    style = MaterialTheme.typography.titleMedium
                )
                
                if (isActive) {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.Cancel, "Cancel Timer")
                    }
                }
            }
            
            if (isActive) {
                Text(
                    "Stops in $remainingMinutes minutes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(5, 10, 15, 30, 60).forEach { minutes ->
                        FilterChip(
                            selected = false,
                            onClick = { onSetTimer(minutes) },
                            label = { Text("${minutes}m") }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Playback speed control (advanced version)
 */
@Composable
fun AdvancedPlaybackSpeedControl(
    currentSpeed: Float,
    onSpeedChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Playback Speed: ${String.format("%.2fx", currentSpeed)}",
                style = MaterialTheme.typography.titleMedium
            )
            
            Slider(
                value = currentSpeed,
                onValueChange = onSpeedChange,
                valueRange = 0.5f..2.0f,
                steps = 14
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("0.5x", style = MaterialTheme.typography.bodySmall)
                Text("1.0x", style = MaterialTheme.typography.bodySmall)
                Text("2.0x", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
