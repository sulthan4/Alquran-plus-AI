package com.alquranplusai.android.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioSettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: com.alquranplusai.android.ui.viewmodels.AudioSettingsViewModel = koinViewModel()
) {
    val defaultReciter by viewModel.defaultReciter.collectAsState()
    val autoPlay by viewModel.autoPlay.collectAsState()
    val playbackSpeed by viewModel.playbackSpeed.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(com.alquranplusai.android.R.string.settings_audio_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            item {
                Text("${stringResource(com.alquranplusai.android.R.string.audio_default_reciter)}: $defaultReciter")
                OutlinedTextField(
                    value = defaultReciter,
                    onValueChange = { viewModel.setDefaultReciter(it) },
                    label = { Text(stringResource(com.alquranplusai.android.R.string.audio_enter_reciter_id)) },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(com.alquranplusai.android.R.string.audio_auto_play))
                    Switch(
                        checked = autoPlay,
                        onCheckedChange = { viewModel.setAutoPlay(it) }
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("${stringResource(com.alquranplusai.android.R.string.audio_playback_speed)}: ${playbackSpeed}x")
                Slider(
                    value = playbackSpeed,
                    onValueChange = { viewModel.setPlaybackSpeed(it) },
                    valueRange = 0.5f..2.0f
                )
            }
        }
    }
}
