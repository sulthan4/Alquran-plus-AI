package com.alquranplusai.android.ui.components.bookmarks

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BookmarkListItem(
    surahName: String,
    ayahNumber: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text("$surahName:$ayahNumber") },
        modifier = modifier
    )
}
