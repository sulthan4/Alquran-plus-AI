package com.alquranplusai.android.ui.screens.audio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Main Audio Screen - Hub for all audio features
 */
@Composable
fun AudioScreen(
    onNavigateToPlayer: () -> Unit,
    onNavigateToReciters: () -> Unit,
    onNavigateToPlaylists: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Audio") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onNavigateToPlayer
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Audio Player",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Listen to Quran recitations",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onNavigateToReciters
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Reciters",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Browse and select reciters",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onNavigateToPlaylists
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Playlists",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Manage your playlists",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
