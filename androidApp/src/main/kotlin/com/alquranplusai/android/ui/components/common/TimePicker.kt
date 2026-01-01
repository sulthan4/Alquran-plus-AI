package com.alquranplusai.android.ui.components.common

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    selectedHour: Int,
    selectedMinute: Int,
    onTimeSelected: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val timePickerState = rememberTimePickerState(
        initialHour = selectedHour,
        initialMinute = selectedMinute
    )
    
    TimePicker(
        state = timePickerState,
        modifier = modifier
    )
    
    LaunchedEffect(timePickerState.hour, timePickerState.minute) {
        onTimeSelected(timePickerState.hour, timePickerState.minute)
    }
}
