package com.alquranplusai.android.ui.components.quran

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ReadingControls(
    onBookmark: () -> Unit,
    onShare: () -> Unit,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = onBookmark) {
            Icon(androidx.compose.material.icons.Icons.Default.Bookmark, "Bookmark")
        }
        IconButton(onClick = onShare) {
            Icon(androidx.compose.material.icons.Icons.Default.Share, "Share")
        }
        IconButton(onClick = onPlay) {
            Icon(androidx.compose.material.icons.Icons.Default.PlayArrow, "Play")
        }
    }
}
