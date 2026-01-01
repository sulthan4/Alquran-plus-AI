package com.alquranplusai.android.ui.screens.storage

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
import com.alquranplusai.android.ui.viewmodels.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Storage Management Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("DEPRECATION")
@Composable
fun StorageManagementScreen(
    navController: NavController,
    viewModel: SettingsViewModel = koinViewModel()
) {
    var totalStorage by remember { mutableStateOf(0L) }
    var usedStorage by remember { mutableStateOf(0L) }
    
    val storageItems = remember {
        listOf(
            StorageItem("Audio Files", 450_000_000L, "Recitations"),
            StorageItem("Translations", 120_000_000L, "Downloaded translations"),
            StorageItem("Tafsir", 80_000_000L, "Tafsir texts"),
            StorageItem("Cache", 25_000_000L, "Temporary files"),
            StorageItem("Database", 15_000_000L, "App data")
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Storage Management") },
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
                StorageOverviewCard(
                    totalStorage = storageItems.sumOf { it.size },
                    usedStorage = storageItems.sumOf { it.size }
                )
            }
            
            item {
                Text(
                    "Storage Breakdown",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(storageItems) { item ->
                StorageItemCard(item)
            }
            
            item {
                Button(
                    onClick = { /* Clear cache */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Delete, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Clear Cache")
                }
            }
        }
    }
}

@Composable
fun StorageOverviewCard(totalStorage: Long, usedStorage: Long) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Storage Used",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                "${formatBytes(usedStorage)} / ${formatBytes(totalStorage)}",
                style = MaterialTheme.typography.headlineSmall
            )
            
            LinearProgressIndicator(
                progress = { (usedStorage.toFloat() / totalStorage.toFloat()) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun StorageItemCard(item: StorageItem) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Text(
                formatBytes(item.size),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

data class StorageItem(
    val name: String,
    val size: Long,
    val description: String
)

fun formatBytes(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
        else -> String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0))
    }
}
