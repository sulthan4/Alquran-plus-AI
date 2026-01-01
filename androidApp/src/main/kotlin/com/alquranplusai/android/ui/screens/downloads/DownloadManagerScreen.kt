package com.alquranplusai.android.ui.screens.downloads

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
import com.alquranplusai.android.ui.viewmodels.DownloadViewModel
import com.alquranplusai.domain.models.*
import org.koin.androidx.compose.koinViewModel

/**
 * Download Manager Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadManagerScreen(
    navController: NavController,
    viewModel: DownloadViewModel = koinViewModel()
) {
    val downloads by viewModel.downloads.collectAsState()
    val statistics by viewModel.statistics.collectAsState()
    val currentProgress by viewModel.currentProgress.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    var showSettings by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Download Manager") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showSettings = true }) {
                        Icon(Icons.Default.Settings, "Settings")
                    }
                    IconButton(onClick = { viewModel.loadDownloads() }) {
                        Icon(Icons.Default.Refresh, "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Statistics Card
            DownloadStatisticsCard(
                statistics = statistics,
                modifier = Modifier.padding(16.dp)
            )
            
            // Filter Tabs
            var selectedTab by remember { mutableStateOf(0) }
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("All") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Downloading") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Completed") }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("Failed") }
                )
            }
            
            // Downloads List
            val filteredDownloads = when (selectedTab) {
                1 -> downloads.filter { it.status == DownloadStatus.DOWNLOADING || it.status == DownloadStatus.PENDING }
                2 -> downloads.filter { it.status == DownloadStatus.COMPLETED }
                3 -> downloads.filter { it.status == DownloadStatus.FAILED }
                else -> downloads
            }
            
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (filteredDownloads.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            "No downloads",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredDownloads) { download ->
                        DownloadItemCard(
                            download = download,
                            progress = currentProgress[download.id],
                            onPause = { viewModel.pauseDownload(download.id) },
                            onResume = { viewModel.resumeDownload(download.id) },
                            onCancel = { viewModel.cancelDownload(download.id) },
                            onRetry = { viewModel.retryDownload(download.id) },
                            onDelete = { viewModel.deleteDownload(download.id) }
                        )
                    }
                }
            }
        }
    }
    
    // Settings Dialog
    if (showSettings) {
        DownloadSettingsDialog(
            onDismiss = { showSettings = false },
            viewModel = viewModel
        )
    }
}

@Composable
fun DownloadStatisticsCard(
    statistics: DownloadStatistics,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem("Total", statistics.totalDownloads.toString())
            StatItem("Completed", statistics.completedDownloads.toString())
            StatItem("Failed", statistics.failedDownloads.toString())
            StatItem("Downloaded", formatBytes(statistics.totalBytesDownloaded))
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun DownloadItemCard(
    download: DownloadItem,
    progress: DownloadProgress?,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onCancel: () -> Unit,
    onRetry: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        download.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        getDownloadTypeLabel(download.type),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                
                DownloadStatusBadge(download.status)
            }
            
            // Progress
            if (download.status == DownloadStatus.DOWNLOADING || download.status == DownloadStatus.PENDING) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    LinearProgressIndicator(
                        progress = { download.progress },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${(download.progress * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            "${formatBytes(download.downloadedBytes)} / ${formatBytes(download.size)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            
            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (download.status) {
                    DownloadStatus.DOWNLOADING -> {
                        IconButton(onClick = onPause) {
                            Icon(Icons.Default.Pause, "Pause")
                        }
                        IconButton(onClick = onCancel) {
                            Icon(Icons.Default.Cancel, "Cancel")
                        }
                    }
                    DownloadStatus.PAUSED -> {
                        IconButton(onClick = onResume) {
                            Icon(Icons.Default.PlayArrow, "Resume")
                        }
                        IconButton(onClick = onCancel) {
                            Icon(Icons.Default.Cancel, "Cancel")
                        }
                    }
                    DownloadStatus.FAILED -> {
                        IconButton(onClick = onRetry) {
                            Icon(Icons.Default.Refresh, "Retry")
                        }
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, "Delete")
                        }
                    }
                    DownloadStatus.COMPLETED -> {
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, "Delete")
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun DownloadStatusBadge(status: DownloadStatus) {
    val (text, color) = when (status) {
        DownloadStatus.PENDING -> "Pending" to MaterialTheme.colorScheme.outline
        DownloadStatus.DOWNLOADING -> "Downloading" to MaterialTheme.colorScheme.primary
        DownloadStatus.PAUSED -> "Paused" to MaterialTheme.colorScheme.tertiary
        DownloadStatus.COMPLETED -> "Completed" to MaterialTheme.colorScheme.primary
        DownloadStatus.FAILED -> "Failed" to MaterialTheme.colorScheme.error
        DownloadStatus.CANCELLED -> "Cancelled" to MaterialTheme.colorScheme.outline
        else -> "Unknown" to MaterialTheme.colorScheme.outline
    }
    
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
fun DownloadSettingsDialog(
    onDismiss: () -> Unit,
    viewModel: DownloadViewModel
) {
    var requiresWifi by remember { mutableStateOf(true) }
    var requiresCharging by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Download Settings") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Require WiFi")
                    Switch(
                        checked = requiresWifi,
                        onCheckedChange = { requiresWifi = it }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Require Charging")
                    Switch(
                        checked = requiresCharging,
                        onCheckedChange = { requiresCharging = it }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                viewModel.updateConstraints(
                    DownloadConstraints(
                        requiresWifi = requiresWifi,
                        requiresCharging = requiresCharging
                    )
                )
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun getDownloadTypeLabel(type: DownloadType): String {
    return when (type) {
        DownloadType.AUDIO -> "Audio Recitation"
        DownloadType.TRANSLATION -> "Translation"
        DownloadType.TAFSIR -> "Tafsir (Commentary)"
        DownloadType.QURAN_TEXT -> "Quran Text"
        DownloadType.FONT -> "Font"
        DownloadType.OTHER -> "Other"
    }
}

fun formatBytes(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
        else -> "${bytes / (1024 * 1024 * 1024)} GB"
    }
}
