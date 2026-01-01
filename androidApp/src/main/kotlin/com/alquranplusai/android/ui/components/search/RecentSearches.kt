package com.alquranplusai.android.ui.components.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RecentSearches(
    searches: List<String>,
    onSearchClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("Recent Searches", style = MaterialTheme.typography.titleSmall)
        searches.forEach { search ->
            ListItem(
                headlineContent = { Text(search) },
                leadingContent = { Icon(androidx.compose.material.icons.Icons.Default.History, "Recent") }
            )
        }
    }
}
