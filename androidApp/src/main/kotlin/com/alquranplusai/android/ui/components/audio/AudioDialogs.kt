package com.alquranplusai.android.ui.components.audio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SleepTimerDialog(
    onDismiss: () -> Unit,
    onTimeSelected: (Int) -> Unit
) {
    val options = listOf(
        15 to "15 Minutes",
        30 to "30 Minutes",
        45 to "45 Minutes",
        60 to "1 Hour",
        90 to "1.5 Hours",
        120 to "2 Hours"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sleep Timer") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                options.forEach { (minutes, label) ->
                    TextButton(
                        onClick = { onTimeSelected(minutes) },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = label, 
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                TextButton(
                    onClick = { onTimeSelected(0) }, // 0 to cancel/turn off
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Turn Off Timer", color = MaterialTheme.colorScheme.error)
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

@Composable
fun RepeatRangeDialog(
    currentSurahTotalAyahs: Int = 286, // Default max if unknown
    onDismiss: () -> Unit,
    onRangeSelected: (Int, Int) -> Unit
) {
    var startText by remember { mutableStateOf("") }
    var endText by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Repeat Range") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Enter Ayah range (1 - $currentSurahTotalAyahs)",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = startText,
                        onValueChange = { 
                            if (it.all { char -> char.isDigit() }) startText = it 
                        },
                        label = { Text("From") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = endText,
                        onValueChange = { 
                            if (it.all { char -> char.isDigit() }) endText = it 
                        },
                        label = { Text("To") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                
                if (error != null) {
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val start = startText.toIntOrNull()
                    val end = endText.toIntOrNull()
                    
                    if (start == null || end == null) {
                        error = "Please enter valid numbers"
                    } else if (start < 1 || end > currentSurahTotalAyahs || start > end) {
                        error = "Invalid range. Ensure 1 <= Start <= End <= $currentSurahTotalAyahs"
                    } else {
                        onRangeSelected(start, end)
                    }
                }
            ) {
                Text("Set Range")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun HifzModeDialog(
    isHifzModeEnabled: Boolean,
    currentRepeatCount: Int,
    onDismiss: () -> Unit,
    onToggleHifzMode: () -> Unit,
    onRepeatCountChange: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Hifz Mode (Memorization)") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleHifzMode() }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isHifzModeEnabled,
                        onCheckedChange = { onToggleHifzMode() }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enable Hifz Mode")
                }
                
                if (isHifzModeEnabled) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Repeats per Ayah: $currentRepeatCount")
                    Slider(
                        value = currentRepeatCount.toFloat(),
                        onValueChange = { onRepeatCountChange(it.toInt()) },
                        valueRange = 1f..10f,
                        steps = 9
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}
