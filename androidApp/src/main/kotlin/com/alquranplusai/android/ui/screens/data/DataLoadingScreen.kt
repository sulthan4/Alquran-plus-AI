package com.alquranplusai.android.ui.screens.data

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alquranplusai.android.ui.viewmodels.DataLoadingViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Screen shown during initial Quran data loading from Quran.com API
 */
@Composable
fun DataLoadingScreen(
    onLoadingComplete: () -> Unit,
    onLoadingError: (String) -> Unit,
    viewModel: DataLoadingViewModel = koinViewModel()
) {
    val loadingState by viewModel.loadingState.collectAsState()
    val progress by viewModel.progress.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadQuranData()
    }
    
    LaunchedEffect(loadingState) {
        when (val state = loadingState) {
            is com.alquranplusai.data.services.DataLoadingResult.Success -> {
                onLoadingComplete()
            }
            is com.alquranplusai.data.services.DataLoadingResult.Error -> {
                onLoadingError(state.message)
            }
            else -> {
                // Still loading
            }
        }
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (val state = loadingState) {
                is com.alquranplusai.data.services.DataLoadingResult.Loading -> {
                    // Title
                    Text(
                        text = "Loading Quran Data",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Subtitle
                    Text(
                        text = "Downloading complete Quran with metadata from Quran.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    // Progress indicator
                    CircularProgressIndicator(
                        progress = progress.percentage / 100f,
                        modifier = Modifier.size(80.dp),
                        strokeWidth = 6.dp
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Progress text
                    Text(
                        text = "${progress.percentage}%",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Loading Surah ${state.current} of ${state.total}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "${progress.loadedVersesCount} / ${progress.totalVersesCount} verses",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                is com.alquranplusai.data.services.DataLoadingResult.Success -> {
                    // Success state (will transition away)
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Success",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(80.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Loading Complete!",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                
                is com.alquranplusai.data.services.DataLoadingResult.Error -> {
                    // Error state
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(80.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Loading Failed",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(onClick = { viewModel.loadQuranData() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}
