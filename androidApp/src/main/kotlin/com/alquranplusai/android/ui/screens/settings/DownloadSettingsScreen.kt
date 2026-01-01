package com.alquranplusai.android.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import com.alquranplusai.android.ui.viewmodels.DownloadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("DEPRECATION")
@Composable
fun DownloadSettingsScreen(
    navController: NavController,
    viewModel: DownloadViewModel = koinViewModel()
) {
    var requiresWifi by remember { mutableStateOf(true) }
    var requiresCharging by remember { mutableStateOf(false) }
    var autoDownload by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Download Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DownloadSettingItem(
                    title = "Require WiFi",
                    description = "Only download over WiFi connection",
                    checked = requiresWifi,
                    onCheckedChange = { requiresWifi = it }
                )
            }
            
            item {
                DownloadSettingItem(
                    title = "Require Charging",
                    description = "Only download while device is charging",
                    checked = requiresCharging,
                    onCheckedChange = { requiresCharging = it }
                )
            }
            
            item {
                DownloadSettingItem(
                    title = "Auto Download",
                    description = "Automatically download new content",
                    checked = autoDownload,
                    onCheckedChange = { autoDownload = it }
                )
            }
            
            item {
                Button(
                    onClick = {
                        viewModel.updateConstraints(
                            com.alquranplusai.domain.models.DownloadConstraints(
                                requiresWifi = requiresWifi,
                                requiresCharging = requiresCharging
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Settings")
                }
            }
        }
    }
}

@Composable
fun DownloadSettingItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}
