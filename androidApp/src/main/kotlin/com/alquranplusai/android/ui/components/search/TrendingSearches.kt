package com.alquranplusai.android.ui.components.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TrendingSearches(
    searches: List<String>,
    onSearchClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("Trending Searches", style = MaterialTheme.typography.titleSmall)
        searches.forEach { search ->
            ListItem(
                headlineContent = { Text(search) },
                leadingContent = { Icon(androidx.compose.material.icons.Icons.Default.TrendingUp, "Trending") }
            )
        }
    }
}
