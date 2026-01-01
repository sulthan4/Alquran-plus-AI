package com.alquranplusai.android.ui.components.audio

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PlayerControls(
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onRepeat: () -> Unit,
    onShuffle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onShuffle) {
                Icon(androidx.compose.material.icons.Icons.Default.Shuffle, "Shuffle")
            }
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
            IconButton(onClick = onRepeat) {
                Icon(androidx.compose.material.icons.Icons.Default.Repeat, "Repeat")
            }
        }
    }
}
