package com.alquranplusai.android.ui.components.bookmarks

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FolderListItem(
    folderName: String,
    bookmarkCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(folderName) },
        supportingContent = { Text("$bookmarkCount bookmarks") },
        leadingContent = { Icon(androidx.compose.material.icons.Icons.Default.Folder, "Folder") },
        modifier = modifier
    )
}
