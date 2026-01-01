package com.alquranplusai.android.ui.screens.audio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alquranplusai.android.ui.viewmodels.AudioPlayerViewModel
import com.alquranplusai.domain.models.Reciter
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayerScreen(
    viewModel: AudioPlayerViewModel = koinViewModel()
) {
    val reciters by viewModel.reciters.collectAsState()
    val selectedReciter by viewModel.selectedReciter.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    // NEW: Advanced features
    val sleepTimerRemaining by viewModel.sleepTimerRemaining.collectAsState()
    val repeatMode by viewModel.repeatMode.collectAsState()
    val hifzMode by viewModel.hifzMode.collectAsState()
    val hifzRepeatCount by viewModel.hifzRepeatCount.collectAsState()
    val hifzCurrentRepeat by viewModel.hifzCurrentRepeat.collectAsState()
    val activeSurahNumber by viewModel.activeSurahNumber.collectAsState()
    
    // Dialog states
    var showSleepTimerDialog by remember { mutableStateOf(false) }
    var showRepeatRangeDialog by remember { mutableStateOf(false) }
    var showHifzModeDialog by remember { mutableStateOf(false) } // Add Hifz Dialog state
    
    if (showSleepTimerDialog) {
        com.alquranplusai.android.ui.components.audio.SleepTimerDialog(
            onDismiss = { showSleepTimerDialog = false },
            onTimeSelected = { minutes ->
                viewModel.setSleepTimer(minutes)
                showSleepTimerDialog = false
            }
        )
    }

    if (showRepeatRangeDialog) {
        com.alquranplusai.android.ui.components.audio.RepeatRangeDialog(
            currentSurahTotalAyahs = 286, // TODO: Get actual total ayahs from VM
            onDismiss = { showRepeatRangeDialog = false },
            onRangeSelected = { start, end ->
                viewModel.setRepeatRange(start, end)
                showRepeatRangeDialog = false
            }
        )
    }
    
    if (showHifzModeDialog) {
        com.alquranplusai.android.ui.components.audio.HifzModeDialog(
            isHifzModeEnabled = hifzMode,
            currentRepeatCount = hifzRepeatCount,
            onDismiss = { showHifzModeDialog = false },
            onToggleHifzMode = { viewModel.toggleHifzMode() },
            onRepeatCountChange = { viewModel.setHifzRepeatCount(it) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Audio Player") },
                actions = {
                    // Hifz Mode Button
                    IconButton(onClick = { showHifzModeDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.School, // Using School icon for Memorization
                            contentDescription = "Hifz Mode",
                            tint = if (hifzMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    // Sleep Timer Button
                    IconButton(onClick = { showSleepTimerDialog = true }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Timer,
                            contentDescription = "Sleep Timer",
                            tint = if (sleepTimerRemaining != null) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    // Repeat Mode Button
                    IconButton(onClick = { 
                        if (repeatMode == com.alquranplusai.android.ui.viewmodels.AudioPlayerViewModel.RepeatMode.ALL) {
                             // If currently ALL, next is RANGE (custom logic) or just cycle through basic
                             // If we want to open Range dialog:
                             showRepeatRangeDialog = true
                        } else {
                             viewModel.cycleRepeatMode() 
                        }
                    }) {
                        Icon(
                            imageVector = when (repeatMode) {
                                com.alquranplusai.android.ui.viewmodels.AudioPlayerViewModel.RepeatMode.OFF -> 
                                    androidx.compose.material.icons.Icons.Default.Repeat
                                com.alquranplusai.android.ui.viewmodels.AudioPlayerViewModel.RepeatMode.AYAH -> 
                                    androidx.compose.material.icons.Icons.Default.RepeatOne
                                com.alquranplusai.android.ui.viewmodels.AudioPlayerViewModel.RepeatMode.SURAH,
                                com.alquranplusai.android.ui.viewmodels.AudioPlayerViewModel.RepeatMode.ALL -> 
                                    androidx.compose.material.icons.Icons.Default.Repeat
                                com.alquranplusai.android.ui.viewmodels.AudioPlayerViewModel.RepeatMode.RANGE -> 
                                    androidx.compose.material.icons.Icons.Default.RepeatOn
                            },
                            contentDescription = "Repeat: ${repeatMode.name}",
                            tint = if (repeatMode != com.alquranplusai.android.ui.viewmodels.AudioPlayerViewModel.RepeatMode.OFF) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Premium Player UI
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.65f), // Give Player majority of space (65%)
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly // Changed to SpaceEvenly for better distribution
            ) {
                 // Album Art / Reciter Image
                 Box(
                     modifier = Modifier
                         .size(200.dp) // Reduced slightly to fit better
                         .padding(16.dp)
                 ) {
                     Card(
                         modifier = Modifier.fillMaxSize(),
                         shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                         elevation = CardDefaults.cardElevation(8.dp)
                     ) {
                         Box(
                             modifier = Modifier
                                 .fillMaxSize()
                                 .background(
                                     androidx.compose.ui.graphics.Brush.linearGradient(
                                         listOf(androidx.compose.ui.graphics.Color(0xFF5E35B1), androidx.compose.ui.graphics.Color(0xFF9575CD))
                                     )
                                 ),
                             contentAlignment = Alignment.Center
                         ) {
                             Icon(
                                 imageVector = Icons.Default.Mic, 
                                 contentDescription = null, 
                                 modifier = Modifier.size(80.dp), 
                                 tint = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                             )
                         }
                     }
                 }
                 
                 // Reciter Info
                 Column(horizontalAlignment = Alignment.CenterHorizontally) {
                     Text(
                         text = selectedReciter?.name ?: "Select a Reciter",
                         style = MaterialTheme.typography.headlineSmall,
                         textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                         maxLines = 1,
                         overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                         modifier = Modifier.padding(horizontal = 16.dp)
                     )
                     Text(
                         text = selectedReciter?.style?.name ?: "Style",
                         style = MaterialTheme.typography.bodyMedium,
                         color = MaterialTheme.colorScheme.onSurfaceVariant
                     )
                 }
                 
                 // Waveform / Progress
                 Row(
                     modifier = Modifier.fillMaxWidth().height(32.dp).padding(horizontal = 32.dp),
                     horizontalArrangement = Arrangement.SpaceEvenly,
                     verticalAlignment = Alignment.CenterVertically
                 ) {
                     repeat(20) {
                         Box(
                             modifier = Modifier
                                 .width(4.dp)
                                 .height((10..30).random().dp)
                                 .background(
                                     if (isPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                     androidx.compose.foundation.shape.CircleShape
                                 )
                         )
                     }
                 }
                 
                 // Controls
                 Row(
                     modifier = Modifier.fillMaxWidth(),
                     horizontalArrangement = Arrangement.Center,
                     verticalAlignment = Alignment.CenterVertically
                 ) {
                     IconButton(onClick = { viewModel.skipPrevious() }, modifier = Modifier.size(48.dp)) {
                         Icon(Icons.Default.SkipPrevious, null, modifier = Modifier.size(32.dp))
                     }
                     Spacer(modifier = Modifier.width(24.dp))
                     FilledIconButton(
                         onClick = { viewModel.togglePlayPause() },
                         modifier = Modifier.size(64.dp)
                     ) {
                         Icon(
                             imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                             contentDescription = if (isPlaying) "Pause" else "Play",
                             modifier = Modifier.size(32.dp)
                         )
                     }
                     Spacer(modifier = Modifier.width(24.dp))
                     IconButton(onClick = { viewModel.skipNext() }, modifier = Modifier.size(48.dp)) {
                         Icon(Icons.Default.SkipNext, null, modifier = Modifier.size(32.dp))
                     }
                 }
            }
            
            Divider()

            // Reciter list
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.35f) // Remaining 35% for list
            ) {
                Text(
                    text = "Select Reciter",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(reciters) { reciter ->
                        ReciterCard(
                            reciter = reciter,
                            isSelected = reciter.id == selectedReciter?.id,
                            onClick = { viewModel.selectReciter(reciter) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReciterCard(
    reciter: Reciter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = if (isSelected) {
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        } else {
            CardDefaults.cardColors()
        },
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reciter.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = reciter.style.name,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
