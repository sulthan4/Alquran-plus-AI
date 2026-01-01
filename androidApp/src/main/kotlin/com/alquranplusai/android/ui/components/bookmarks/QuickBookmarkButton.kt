package com.alquranplusai.android.ui.components.bookmarks

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun QuickBookmarkButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(onClick = onClick, modifier = modifier) {
        Icon(androidx.compose.material.icons.Icons.Default.Bookmark, "Quick Bookmark")
    }
}
