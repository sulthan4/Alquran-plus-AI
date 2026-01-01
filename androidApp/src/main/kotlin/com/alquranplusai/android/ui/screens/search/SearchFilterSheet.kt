package com.alquranplusai.android.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilterSheet(
    onDismiss: () -> Unit,
    onApplyFilters: (List<Int>, List<String>) -> Unit,
    availableSurahs: List<com.alquranplusai.domain.models.Surah> = emptyList(), // Pass standard Surah model
    availableTranslations: List<String> = listOf("Sahih International", "Urdu - Jalandhry"), // Mock/Default
    initialSelectedSurahs: List<Int> = emptyList(),
    initialSelectedTranslations: List<String> = emptyList()
) {
    var selectedSurahs by remember { mutableStateOf(initialSelectedSurahs.toMutableSet()) }
    var selectedTranslations by remember { mutableStateOf(initialSelectedTranslations.toMutableSet()) }
    
    // Tab state for "Surahs" vs "Translations"
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Surahs", "Translations")

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Search Filters",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = {
                    selectedSurahs.clear()
                    selectedTranslations.clear()
                }) {
                    Text("Reset")
                }
            }
            
            Divider()
            
            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
            
            // Content
            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    0 -> { // Surahs
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { 
                                            if (selectedSurahs.isEmpty()) {
                                                // Select all? No, empty means "All" usually.
                                                // But UI might require explicit "All".
                                                // Let's say Empty = All.
                                            } else {
                                                selectedSurahs.clear()
                                            }
                                        }
                                        .padding(vertical = 8.dp)
                                ) {
                                    Checkbox(
                                        checked = selectedSurahs.isEmpty(),
                                        onCheckedChange = { 
                                            if (it) selectedSurahs.clear() 
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("All Surahs")
                                }
                                Divider()
                            }
                            
                            items(availableSurahs) { surah ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (selectedSurahs.contains(surah.number)) {
                                                selectedSurahs.remove(surah.number)
                                            } else {
                                                selectedSurahs.add(surah.number)
                                            }
                                        }
                                        .padding(vertical = 8.dp)
                                ) {
                                    Checkbox(
                                        checked = selectedSurahs.contains(surah.number),
                                        onCheckedChange = {
                                            if (it) selectedSurahs.add(surah.number)
                                            else selectedSurahs.remove(surah.number)
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("${surah.number}. ${surah.nameTransliteration}")
                                }
                            }
                        }
                    }
                    1 -> { // Translations
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                             items(availableTranslations) { padding ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (selectedTranslations.contains(padding)) {
                                                selectedTranslations.remove(padding)
                                            } else {
                                                selectedTranslations.add(padding)
                                            }
                                        }
                                        .padding(vertical = 8.dp)
                                ) {
                                    Checkbox(
                                        checked = selectedTranslations.contains(padding),
                                        onCheckedChange = {
                                            if (it) selectedTranslations.add(padding)
                                            else selectedTranslations.remove(padding)
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(padding)
                                }
                            }
                        }
                    }
                }
            }
            
            // Apply Button
            Button(
                onClick = { onApplyFilters(selectedSurahs.toList(), selectedTranslations.toList()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Apply Filters")
            }
        }
    }
}
