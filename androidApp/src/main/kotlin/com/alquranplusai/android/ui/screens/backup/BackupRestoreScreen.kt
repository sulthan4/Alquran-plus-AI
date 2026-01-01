package com.alquranplusai.android.ui.screens.backup

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Backup and Restore Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupRestoreScreen(
    navController: NavController
) {
    var isBackingUp by remember { mutableStateOf(false) }
    var lastBackupTime by remember { mutableStateOf("Never") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Backup & Restore") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CloudDone, null)
                        Text(
                            "Last Backup",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Text(
                        lastBackupTime,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            
            Button(
                onClick = { isBackingUp = true },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isBackingUp
            ) {
                Icon(Icons.Default.Backup, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isBackingUp) "Backing up..." else "Backup Now")
            }
            
            OutlinedButton(
                onClick = { /* Restore */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Restore, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Restore from Backup")
            }
            
            if (isBackingUp) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
