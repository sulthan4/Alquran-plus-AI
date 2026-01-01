package com.alquranplusai.android.ui.components.search

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SearchHistory(
    history: List<String>,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Search History", style = MaterialTheme.typography.titleSmall)
            TextButton(onClick = onClear) {
                Text("Clear")
            }
        }
        history.forEach { search ->
            ListItem(headlineContent = { Text(search) })
        }
    }
}
