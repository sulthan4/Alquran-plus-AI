package com.alquranplusai.android.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alquranplusai.android.ui.viewmodels.DisplaySettingsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplaySettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: DisplaySettingsViewModel = koinViewModel()
) {
    // Existing states
    val animationsEnabled by viewModel.animationsEnabled.collectAsState()
    val hapticFeedbackEnabled by viewModel.hapticFeedbackEnabled.collectAsState()
    
    // NEW: Reading settings states
    val arabicFontSize by viewModel.arabicFontSize.collectAsState()
    val translationFontSize by viewModel.translationFontSize.collectAsState()
    val lineSpacing by viewModel.lineSpacing.collectAsState()
    val showTranslation by viewModel.showTranslation.collectAsState()
    val showTransliteration by viewModel.showTransliteration.collectAsState()
    val keepScreenOn by viewModel.keepScreenOn.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Display & Reading Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Font Settings Section
            item {
                Text(
                    text = "Font Settings",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Arabic Text Size",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Slider(
                                value = arabicFontSize,
                                onValueChange = { viewModel.setArabicFontSize(it) },
                                valueRange = 16f..48f,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${arabicFontSize.toInt()}sp",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.width(50.dp)
                            )
                        }
                    }
                }
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Translation Text Size",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Slider(
                                value = translationFontSize,
                                onValueChange = { viewModel.setTranslationFontSize(it) },
                                valueRange = 12f..32f,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${translationFontSize.toInt()}sp",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.width(50.dp)
                            )
                        }
                    }
                }
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Line Spacing",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Slider(
                                value = lineSpacing,
                                onValueChange = { viewModel.setLineSpacing(it) },
                                valueRange = 1.0f..2.5f,
                                steps = 14,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = String.format("%.1f", lineSpacing),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.width(50.dp)
                            )
                        }
                    }
                }
            }
            
            // Display Options Section
            item {
                Text(
                    text = "Display Options",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    ListItem(
                        headlineContent = { Text("Show Translation") },
                        supportingContent = { Text("Display translation below Arabic text") },
                        trailingContent = {
                            Switch(
                                checked = showTranslation,
                                onCheckedChange = { viewModel.setShowTranslation(it) }
                            )
                        }
                    )
                }
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    ListItem(
                        headlineContent = { Text("Show Transliteration") },
                        supportingContent = { Text("Display romanized Arabic text") },
                        trailingContent = {
                            Switch(
                                checked = showTransliteration,
                                onCheckedChange = { viewModel.setShowTransliteration(it) }
                            )
                        }
                    )
                }
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    ListItem(
                        headlineContent = { Text("Keep Screen On") },
                        supportingContent = { Text("Prevent screen from sleeping while reading") },
                        trailingContent = {
                            Switch(
                                checked = keepScreenOn,
                                onCheckedChange = { viewModel.setKeepScreenOn(it) }
                            )
                        }
                    )
                }
            }
            
            // UI Preferences Section
            item {
                Text(
                    text = "UI Preferences",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    ListItem(
                        headlineContent = { Text("Enable Animations") },
                        supportingContent = { Text("Smooth transitions and effects") },
                        trailingContent = {
                            Switch(
                                checked = animationsEnabled,
                                onCheckedChange = { viewModel.setAnimationsEnabled(it) }
                            )
                        }
                    )
                }
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    ListItem(
                        headlineContent = { Text("Haptic Feedback") },
                        supportingContent = { Text("Subtle vibrations for interactions") },
                        trailingContent = {
                            Switch(
                                checked = hapticFeedbackEnabled,
                                onCheckedChange = { viewModel.setHapticFeedbackEnabled(it) }
                            )
                        }
                    )
                }
            }
        }
    }
}
