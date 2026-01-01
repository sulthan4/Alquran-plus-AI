package com.alquranplusai.android.ui.components.audio

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FullScreenPlayer(
    title: String,
    artist: String,
    isPlaying: Boolean,
    progress: Float,
    currentTime: String,
    totalTime: String,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        Text(text = artist, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Slider(value = progress, onValueChange = onSeek)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(currentTime)
            Text(totalTime)
        }
        Spacer(modifier = Modifier.height(16.dp))
        PlayerControls(isPlaying, onPlayPause, onNext, onPrevious, {}, {})
    }
}
