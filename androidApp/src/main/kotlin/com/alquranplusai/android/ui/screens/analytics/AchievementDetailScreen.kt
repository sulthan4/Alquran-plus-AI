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
fun AchievementDetailScreen(
    achievementId: Long,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Achievement Details") },
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
            Text("Achievement ID: $achievementId")
        }
    }
}
