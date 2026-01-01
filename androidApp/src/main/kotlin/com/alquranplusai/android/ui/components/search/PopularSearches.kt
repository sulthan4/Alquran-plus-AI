package com.alquranplusai.android.ui.components.search

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PopularSearches(
    searches: List<String>,
    onSearchClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("Popular Searches", style = MaterialTheme.typography.titleSmall)
        FlowRow {
            searches.forEach { search ->
                SuggestionChip(suggestion = search, onClick = { onSearchClick(search) })
            }
        }
    }
}
