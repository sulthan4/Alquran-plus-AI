package com.alquranplusai.android.ui.components.audio

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DownloadButton(
    isDownloaded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            if (isDownloaded) androidx.compose.material.icons.Icons.Default.CheckCircle 
            else androidx.compose.material.icons.Icons.Default.Download,
            if (isDownloaded) "Downloaded" else "Download"
        )
    }
}
