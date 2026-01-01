package com.alquranplusai.android.ui.components.search

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchFilters(
    selectedFilters: List<String>,
    onFilterToggled: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf("Surah", "Ayah", "Translation", "Tafsir")
    FlowRow(modifier = modifier) {
        filters.forEach { filter ->
            FilterChip(
                selected = filter in selectedFilters,
                onClick = { onFilterToggled(filter) },
                label = { Text(filter) },
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
