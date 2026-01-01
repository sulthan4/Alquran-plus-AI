package com.alquranplusai.android.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.alquranplusai.android.ui.viewmodels.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateTo: (String) -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showThemeDialog by remember { mutableStateOf(false) }
    
    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = uiState.theme,
            onThemeSelected = { 
                viewModel.updateTheme(it) 
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // General
            item { SectionHeader("General") }
            
            item {
                SettingsNavigationItem(
                    title = "Theme",
                    subtitle = uiState.theme,
                    onClick = { showThemeDialog = true }
                )
            }
             item {
                SettingsNavigationItem(
                    title = "Language",
                    subtitle = "English", // Placeholder for now
                    onClick = { onNavigateTo(com.alquranplusai.android.navigation.NavRoutes.LANGUAGE_SETTINGS) }
                )
            }

            // Reading
            item { SectionHeader("Reading") }
            item {
                SettingsNavigationItem(
                    title = "Reading Preferences",
                    subtitle = "Font size, translation",
                    onClick = { onNavigateTo(com.alquranplusai.android.navigation.NavRoutes.READING_PREFERENCES) }
                )
            }

            // Audio
            item { SectionHeader("Audio") }
            item {
                 SettingsNavigationItem(
                    title = "Audio Settings",
                    subtitle = "Reciter, playback speed",
                    onClick = { onNavigateTo(com.alquranplusai.android.navigation.NavRoutes.AUDIO_SETTINGS) }
                )
            }

            // Notifications
            item { SectionHeader("Notifications") }
            item {
                SettingsNavigationItem(
                    title = "Notifications",
                    subtitle = "Daily reminders, updates",
                    onClick = { onNavigateTo(com.alquranplusai.android.navigation.NavRoutes.NOTIFICATION_SETTINGS) }
                )
            }
            
            // Privacy & Security
            item { SectionHeader("Privacy & Security") }
            item {
                SettingsNavigationItem(
                    title = "Privacy",
                    subtitle = "Data collection, analytics",
                    onClick = { onNavigateTo(com.alquranplusai.android.navigation.NavRoutes.PRIVACY_SETTINGS) }
                )
            }
            item {
                SettingsNavigationItem(
                    title = "Security",
                    subtitle = "Biometric lock, PIN",
                    onClick = { onNavigateTo(com.alquranplusai.android.navigation.NavRoutes.SECURITY_SETTINGS) }
                )
            }
            
            // Data & Storage
            item { SectionHeader("Data & Storage") }
            item {
                SettingsNavigationItem(
                    title = "Backup & Sync",
                    subtitle = "Cloud backup, auto-sync",
                    onClick = { onNavigateTo(com.alquranplusai.android.navigation.NavRoutes.BACKUP_SETTINGS) }
                )
            }
            item {
                SettingsNavigationItem(
                    title = "Downloads",
                    subtitle = "WiFi only, cache",
                    onClick = { onNavigateTo(com.alquranplusai.android.navigation.NavRoutes.DOWNLOAD_SETTINGS) }
                )
            }
            
            // Display
            item { SectionHeader("Display") }
            item {
                SettingsNavigationItem(
                    title = "Display & Reading",
                    subtitle = "Font, size, screen options",
                    onClick = { onNavigateTo(com.alquranplusai.android.navigation.NavRoutes.DISPLAY_SETTINGS) }
                )
            }
            
            // Account
            item { SectionHeader("Account") }
            item {
                SettingsNavigationItem(
                    title = "Account",
                    subtitle = "Profile, preferences",
                    onClick = { onNavigateTo(com.alquranplusai.android.navigation.NavRoutes.ACCOUNT_SETTINGS) }
                )
            }
            
            // About
            item { SectionHeader("About") }
            item {
                 SettingsNavigationItem(
                    title = "About",
                    subtitle = "Version 1.0.0",
                    onClick = { onNavigateTo(com.alquranplusai.android.navigation.NavRoutes.ABOUT) }
                )
            }
        }
    }
}

@Composable
fun SettingsNavigationItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        ListItem(
            headlineContent = { Text(title) },
            supportingContent = { Text(subtitle) },
            trailingContent = { Text("→") } // Simple arrow
        )
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SettingItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
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
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = description,
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

@Composable
fun ThemeSelectionDialog(
    currentTheme: String,
    onThemeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Theme") },
        text = {
            Column(Modifier.selectableGroup()) {
                val themes = listOf("SYSTEM", "LIGHT", "DARK")
                themes.forEach { theme ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (theme == currentTheme),
                                onClick = { onThemeSelected(theme) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (theme == currentTheme),
                            onClick = null 
                        )
                        Text(
                            text = theme,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
