package com.alquranplusai.android.ui.components.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VoiceIndicator(
    isListening: Boolean,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = androidx.compose.material.icons.Icons.Default.Mic,
        contentDescription = "Voice",
        tint = if (isListening) MaterialTheme.colorScheme.primary 
              else MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}
