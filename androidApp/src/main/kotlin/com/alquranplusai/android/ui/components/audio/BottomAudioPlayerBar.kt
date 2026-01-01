package com.alquranplusai.android.ui.components.audio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alquranplusai.android.ui.viewmodels.AudioPlayerViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Persistent bottom audio player bar - shown across all screens when audio is playing
 * Matches the design from reference image with controls: Stop, Previous, Play/Pause, Next, Repeat, Speed, Settings
 */
@Composable
fun BottomAudioPlayerBar(
    modifier: Modifier = Modifier,
    viewModel: AudioPlayerViewModel = koinViewModel(),
    onOpenFullPlayer: () -> Unit = {}
) {
    val isPlaying by viewModel.isPlaying.collectAsState()
    val selectedReciter by viewModel.selectedReciter.collectAsState()
    val currentAyah by viewModel.currentAyah.collectAsState()
    val repeatMode by viewModel.repeatMode.collectAsState()
    val playbackSpeed by viewModel.playbackSpeed.collectAsState()
    val sleepTimerRemaining by viewModel.sleepTimerRemaining.collectAsState()
    
    var showSpeedDialog by remember { mutableStateOf(false) }
    var showSleepTimerDialog by remember { mutableStateOf(false) }
    
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 3.dp
    ) {
        Column {
            // Progress bar (optional - shows playback progress)
            LinearProgressIndicator(
                progress = 0.3f, // TODO: Connect to actual progress
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            
            // Main controls row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stop button
                IconButton(
                    onClick = { 
                        // Stop = pause and reset to beginning
                        viewModel.togglePlayPause()
                        viewModel.seekTo(0)
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = "Stop",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                // Previous button
                IconButton(
                    onClick = { viewModel.skipPrevious() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                // Play/Pause button (larger, primary)
                FilledIconButton(
                    onClick = { viewModel.togglePlayPause() },
                    modifier = Modifier.size(56.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                
                // Next button
                IconButton(
                    onClick = { viewModel.skipNext() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                // Repeat mode button
                IconButton(
                    onClick = { viewModel.cycleRepeatMode() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = when (repeatMode) {
                            AudioPlayerViewModel.RepeatMode.OFF -> Icons.Default.Repeat
                            AudioPlayerViewModel.RepeatMode.AYAH -> Icons.Default.RepeatOne
                            AudioPlayerViewModel.RepeatMode.SURAH,
                            AudioPlayerViewModel.RepeatMode.ALL -> Icons.Default.Repeat
                            AudioPlayerViewModel.RepeatMode.RANGE -> Icons.Default.RepeatOn
                        },
                        contentDescription = "Repeat: ${repeatMode.name}",
                        tint = if (repeatMode != AudioPlayerViewModel.RepeatMode.OFF)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Speed/Timer button (combined)
                IconButton(
                    onClick = { showSpeedDialog = true },
                    modifier = Modifier.size(40.dp)
                ) {
                    Badge(
                        containerColor = if (sleepTimerRemaining != null)
                            MaterialTheme.colorScheme.tertiary
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Icon(
                            imageVector = if (sleepTimerRemaining != null)
                                Icons.Default.Timer
                            else
                                Icons.Default.Speed,
                            contentDescription = "Speed & Timer",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                
                // Settings/More button
                IconButton(
                    onClick = onOpenFullPlayer,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "More Options",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            // Info text (reciter + surah)
            if (selectedReciter != null || currentAyah != null) {
                Text(
                    text = buildString {
                        selectedReciter?.let { append(it.name) }
                        currentAyah?.let { 
                            if (isNotEmpty()) append(" • ")
                            append("Surah ${it.surahNumber}, Ayah ${it.ayahNumber}")
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    maxLines = 1
                )
            }
        }
    }
    
    // Speed & Timer Dialog
    if (showSpeedDialog) {
        AlertDialog(
            onDismissRequest = { showSpeedDialog = false },
            title = { Text("Playback Speed & Timer") },
            text = {
                Column {
                    Text("Speed: ${String.format("%.1fx", playbackSpeed)}")
                    Slider(
                        value = playbackSpeed,
                        onValueChange = { viewModel.setPlaybackSpeed(it) },
                        valueRange = 0.5f..2.0f,
                        steps = 14
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    TextButton(
                        onClick = {
                            showSpeedDialog = false
                            showSleepTimerDialog = true
                        }
                    ) {
                        Text("Set Sleep Timer")
                    }
                    
                    sleepTimerRemaining?.let { remaining ->
                        Text(
                            "Timer: ${remaining / 60000}m ${(remaining % 60000) / 1000}s",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSpeedDialog = false }) {
                    Text("Done")
                }
            }
        )
    }
    
    if (showSleepTimerDialog) {
        SleepTimerDialog(
            onDismiss = { showSleepTimerDialog = false },
            onTimeSelected = { minutes ->
                viewModel.setSleepTimer(minutes)
                showSleepTimerDialog = false
            }
        )
    }
}
