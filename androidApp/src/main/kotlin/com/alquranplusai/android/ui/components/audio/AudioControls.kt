package com.alquranplusai.android.ui.components.audio

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AudioControls(
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        IconButton(onClick = onPrevious) {
            Icon(androidx.compose.material.icons.Icons.Default.SkipPrevious, "Previous")
        }
        IconButton(onClick = onPlayPause) {
            Icon(
                if (isPlaying) androidx.compose.material.icons.Icons.Default.Pause 
                else androidx.compose.material.icons.Icons.Default.PlayArrow,
                if (isPlaying) "Pause" else "Play"
            )
        }
        IconButton(onClick = onNext) {
            Icon(androidx.compose.material.icons.Icons.Default.SkipNext, "Next")
        }
    }
}
