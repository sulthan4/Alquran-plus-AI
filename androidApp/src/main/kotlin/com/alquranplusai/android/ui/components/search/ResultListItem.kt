package com.alquranplusai.android.ui.components.search

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ResultListItem(
    surahName: String,
    ayahNumber: Int,
    snippet: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text("$surahName:$ayahNumber") },
        supportingContent = { Text(snippet) },
        modifier = modifier
    )
}
