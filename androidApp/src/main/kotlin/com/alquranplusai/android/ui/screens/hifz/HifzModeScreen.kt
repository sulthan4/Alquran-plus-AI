package com.alquranplusai.android.ui.screens.hifz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alquranplusai.android.ui.components.quran.RepeatMode
import com.alquranplusai.android.ui.components.quran.RepeatModeSelector
import com.alquranplusai.android.ui.components.quran.WordByWordDisplay

/**
 * Hifz (Memorization) Mode Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HifzModeScreen(
    navController: NavController,
    surahNumber: Int,
    ayahNumber: Int,
    viewModel: com.alquranplusai.android.ui.viewmodels.AudioPlayerViewModel = org.koin.androidx.compose.koinViewModel()
) {
    var repeatMode by remember { mutableStateOf(RepeatMode.AYAH) }
    var repeatCount by remember { mutableStateOf(3) }
    
    val isPlaying by viewModel.isPlaying.collectAsState()
    val activeAyah by viewModel.activeAyahNumber.collectAsState()
    
    // Ensure context is set to the selected Ayah initially
    LaunchedEffect(surahNumber, ayahNumber) {
        // We might want to tell VM to prepare for this specific Ayah
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hifz Mode") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Settings */ }) {
                        Icon(Icons.Default.Settings, "Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Surah/Ayah Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Surah $surahNumber, Ayah $ayahNumber",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        "Memorization Mode",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
            
            // Repeat Mode Selector
            RepeatModeSelector(
                currentMode = repeatMode,
                onModeChange = { repeatMode = it }
            )
            
            // Repeat Count
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Repeat Count: $repeatCount",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Slider(
                        value = repeatCount.toFloat(),
                        onValueChange = { repeatCount = it.toInt() },
                        valueRange = 1f..10f,
                        steps = 8
                    )
                }
            }
            
            // Playback Controls
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Previous */ }) {
                        Icon(Icons.Default.SkipPrevious, "Previous", modifier = Modifier.size(32.dp))
                    }
                    
                    FilledIconButton(
                        onClick = { viewModel.togglePlayPause(surahNumber) },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            if (isPlaying) "Pause" else "Play",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    
                    IconButton(onClick = { /* Next */ }) {
                        Icon(Icons.Default.SkipNext, "Next", modifier = Modifier.size(32.dp))
                    }
                }
            }
            
            // Progress Indicator
            if (isPlaying) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
