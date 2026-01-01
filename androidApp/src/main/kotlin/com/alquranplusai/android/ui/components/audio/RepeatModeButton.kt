package com.alquranplusai.android.ui.components.audio

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RepeatModeButton(
    repeatMode: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(androidx.compose.material.icons.Icons.Default.Repeat, "Repeat: $repeatMode")
    }
}
