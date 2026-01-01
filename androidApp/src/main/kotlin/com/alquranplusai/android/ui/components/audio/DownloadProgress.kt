package com.alquranplusai.android.ui.components.audio

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DownloadProgress(
    progress: Float,
    fileName: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = "Downloading: $fileName", style = MaterialTheme.typography.bodySmall)
        LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth())
    }
}
