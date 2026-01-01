package com.alquranplusai.android.ui.components.audio

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Audio repeat/loop controls
 */
@Composable
fun RepeatLoopControls(
    repeatMode: RepeatMode,
    loopCount: Int,
    onRepeatModeChange: (RepeatMode) -> Unit,
    onLoopCountChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Repeat Mode Selection
            Text(
                "Repeat Mode",
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RepeatMode.values().forEach { mode ->
                    FilterChip(
                        selected = repeatMode == mode,
                        onClick = { onRepeatModeChange(mode) },
                        label = { Text(mode.displayName) },
                        leadingIcon = if (repeatMode == mode) {
                            { Icon(Icons.Default.CheckCircle, null, Modifier.size(18.dp)) }
                        } else null
                    )
                }
            }
            
            // Loop Count (only show if repeat mode is not NONE)
            if (repeatMode != RepeatMode.NONE) {
                Divider()
                
                Text(
                    "Loop Count: ${if (loopCount == 0) "Infinite" else loopCount}",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { if (loopCount > 0) onLoopCountChange(loopCount - 1) }) {
                        Icon(Icons.Default.Remove, "Decrease")
                    }
                    
                    Slider(
                        value = loopCount.toFloat(),
                        onValueChange = { onLoopCountChange(it.toInt()) },
                        valueRange = 0f..10f,
                        steps = 9,
                        modifier = Modifier.weight(1f)
                    )
                    
                    IconButton(onClick = { if (loopCount < 10) onLoopCountChange(loopCount + 1) }) {
                        Icon(Icons.Default.Add, "Increase")
                    }
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf(0, 1, 3, 5, 7, 10).forEach { count ->
                        FilterChip(
                            selected = loopCount == count,
                            onClick = { onLoopCountChange(count) },
                            label = { Text(if (count == 0) "∞" else count.toString()) }
                        )
                    }
                }
            }
        }
    }
}

enum class RepeatMode(val displayName: String) {
    NONE("Off"),
    AYAH("Ayah"),
    SURAH("Surah"),
    RANGE("Range")
}
