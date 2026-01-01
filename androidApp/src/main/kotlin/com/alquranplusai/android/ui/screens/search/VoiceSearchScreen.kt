package com.alquranplusai.android.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

/**
 * Voice Search Screen with speech recognition
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceSearchScreen(
    navController: NavController,
    viewModel: com.alquranplusai.android.ui.viewmodels.VoiceSearchViewModel = org.koin.androidx.compose.koinViewModel()
) {
    val isListening by viewModel.isListening.collectAsState()
    val transcription by viewModel.transcription.collectAsState()
    // val error by viewModel.error.collectAsState() // Not exposed
    // val volume by viewModel.rmsDb.collectAsState() // Not exposed
    val volume = 0.5f
    val error: String? = if (transcription.startsWith("Error:")) transcription else null


    // Auto-navigate on success (optional, or just show results locally)
    // For now, we display the transcription.

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Voice Search") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Voice animation
            VoiceAnimationCard(
                isListening = isListening,
                volumeLevel = volume
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Status text
            Text(
                if (isListening) "Listening..." else "Tap to speak",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Transcription
            if (transcription.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        transcription,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Microphone button
            FloatingActionButton(
                onClick = {
                    if (isListening) {
                        viewModel.stopListening()
                    } else {
                        viewModel.startListening()
                    }
                },
                modifier = Modifier.size(80.dp),
                containerColor = if (isListening) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    if (isListening) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = if (isListening) "Stop" else "Start",
                    modifier = Modifier.size(40.dp)
                )
            }
            
            // Error message
            if (error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Language hint
            Text(
                "Supports Arabic and English",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun VoiceAnimationCard(
    isListening: Boolean,
    volumeLevel: Float
) {
    Card(
        modifier = Modifier.size(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isListening) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Mic,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = if (isListening) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
