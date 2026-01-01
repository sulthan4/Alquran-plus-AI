package com.alquranplusai.android.ui.screens.audio

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
import com.alquranplusai.android.audio.EqualizerController

/**
 * Equalizer Screen for audio customization
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerScreen(
    navController: NavController
) {
    var isEnabled by remember { mutableStateOf(true) }
    var selectedPreset by remember { mutableStateOf<EqualizerController.EqualizerPreset?>(null) }
    var bandLevels by remember { mutableStateOf(List(5) { 0.toShort() }) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Equalizer") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { bandLevels = List(5) { 0.toShort() } }) {
                        Icon(Icons.Default.Refresh, "Reset")
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
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Equalizer",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                if (isEnabled) "Enabled" else "Disabled",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = isEnabled,
                            onCheckedChange = { isEnabled = it }
                        )
                    }
                }
            }
            
            item {
                Text(
                    "Presets",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(EqualizerController.PRESETS) { preset ->
                PresetCard(
                    preset = preset,
                    selected = selectedPreset == preset,
                    onClick = {
                        selectedPreset = preset
                        bandLevels = preset.levels
                    }
                )
            }
            
            item {
                Divider()
            }
            
            item {
                Text(
                    "Custom",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            item {
                EqualizerBands(
                    bandLevels = bandLevels,
                    onBandLevelChange = { index, level ->
                        bandLevels = bandLevels.toMutableList().apply {
                            set(index, level)
                        }
                        selectedPreset = null
                    },
                    enabled = isEnabled
                )
            }
        }
    }
}

@Composable
fun PresetCard(
    preset: EqualizerController.EqualizerPreset,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                preset.name,
                style = MaterialTheme.typography.titleMedium
            )
            if (selected) {
                Icon(
                    Icons.Default.CheckCircle,
                    "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun EqualizerBands(
    bandLevels: List<Short>,
    onBandLevelChange: (Int, Short) -> Unit,
    enabled: Boolean
) {
    val bandNames = listOf("60Hz", "230Hz", "910Hz", "3.6kHz", "14kHz")
    
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            bandLevels.forEachIndexed { index, level ->
                Column {
                    Text(
                        bandNames.getOrNull(index) ?: "Band ${index + 1}",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Slider(
                        value = level.toFloat(),
                        onValueChange = { onBandLevelChange(index, it.toInt().toShort()) },
                        valueRange = -1500f..1500f,
                        enabled = enabled
                    )
                }
            }
        }
    }
}
