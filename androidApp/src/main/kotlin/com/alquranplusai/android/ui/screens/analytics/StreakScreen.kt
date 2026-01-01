package com.alquranplusai.android.ui.screens.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alquranplusai.android.ui.viewmodels.AnalyticsViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Main Analytics Screen showing overview statistics
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreakScreen(
    onNavigateBack: () -> Unit,
    viewModel: com.alquranplusai.android.ui.viewmodels.StreakViewModel = koinViewModel()
) {
    val currentStreak by viewModel.currentStreak.collectAsState()
    val longestStreak by viewModel.longestStreak.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reading Streak") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Current Streak",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "$currentStreak days",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Longest Streak",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "$longestStreak days",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
        }
    }
}

