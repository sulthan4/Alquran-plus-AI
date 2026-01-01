package com.alquranplusai.android.ui.components.bookmarks

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BookmarkFolderCard(
    folderName: String,
    bookmarkCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = folderName, style = MaterialTheme.typography.titleMedium)
                Text(text = "$bookmarkCount bookmarks", style = MaterialTheme.typography.bodySmall)
            }
            Icon(androidx.compose.material.icons.Icons.Default.Folder, "Folder")
        }
    }
}
