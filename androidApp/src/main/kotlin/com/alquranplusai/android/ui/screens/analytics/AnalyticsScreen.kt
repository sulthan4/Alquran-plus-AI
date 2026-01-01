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
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = koinViewModel()
) {
    val totalReadingTime by viewModel.totalReadingTime.collectAsState()
    val currentStreak by viewModel.currentStreak.collectAsState()
    val longestStreak by viewModel.longestStreak.collectAsState()
    val completedSurahs by viewModel.completedSurahs.collectAsState()
    val weeklyReadingData by viewModel.weeklyReadingData.collectAsState()
    val readingSessions by viewModel.readingSessions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Analytics") })
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Reading Time Card
                item {
                    StatisticsCard(
                        title = "Total Reading Time",
                        value = formatTime(totalReadingTime),
                        description = "Time spent reading Quran"
                    )
                }
                
                // Streak Cards
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatisticsCard(
                            title = "Current Streak",
                            value = "$currentStreak days",
                            modifier = Modifier.weight(1f)
                        )
                        StatisticsCard(
                            title = "Longest Streak",
                            value = "$longestStreak days",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                // Completed Surahs
                item {
                    StatisticsCard(
                        title = "Completed Surahs",
                        value = "$completedSurahs / 114",
                        description = "${(completedSurahs * 100 / 114)}% of Quran"
                    )
                }
                
                // Progress Chart Placeholder
                item {
                            // Weekly Chart
                            if (weeklyReadingData.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No data available", style = MaterialTheme.typography.bodyMedium)
                                }
                            } else {
                                SimpleBarChart(
                                    data = weeklyReadingData,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .padding(top = 8.dp)
                                )
                            }
                        }

                // Reading History
                item {
                    Text(
                        "Reading History",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(readingSessions) { session ->
                    ReadingSessionCard(session)
                }
             }
        }
    }
}

@Composable
fun ReadingSessionCard(session: com.alquranplusai.domain.models.ReadingSession) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Surah ${session.surahNumber}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = formatTime(session.duration.toLong() * 1000), // duration in seconds
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ayahs ${session.startAyah} - ${session.endAyah ?: "?"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
             Text(
                text = java.text.SimpleDateFormat("MMM dd, HH:mm", java.util.Locale.getDefault()).format(java.util.Date(session.startTime)),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
fun SimpleBarChart(
    data: List<Long>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return
    
    val max = data.maxOrNull()?.toFloat() ?: 1f
    val safeMax = if (max == 0f) 1f else max
    
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val barWidth = size.width / (data.size * 2f)
        val spacing = size.width / (data.size * 2f)
        
        data.forEachIndexed { index, value ->
            val barHeight = (value / safeMax) * size.height
            val x = (index * (barWidth + spacing)) + spacing / 2
            val y = size.height - barHeight
            
            drawRect(
                color = androidx.compose.ui.graphics.Color(0xFF1ABC9C), // Primary color
                topLeft = androidx.compose.ui.geometry.Offset(x, y),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
            )
        }
    }
}


@Composable
private fun StatisticsCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            description?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatTime(milliseconds: Long): String {
    val hours = milliseconds / (1000 * 60 * 60)
    val minutes = (milliseconds % (1000 * 60 * 60)) / (1000 * 60)
    return "${hours}h ${minutes}m"
}

