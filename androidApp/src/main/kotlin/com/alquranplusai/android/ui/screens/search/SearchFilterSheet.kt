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
    // Use mutableStateListOf for reliable Compose updates
    val selectedSurahs = remember { 
        mutableStateListOf<Int>().apply { addAll(initialSelectedSurahs) } 
    }
    val selectedTranslations = remember { 
        mutableStateListOf<String>().apply { addAll(initialSelectedTranslations) } 
    }
    
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
                    onApplyFilters(emptyList(), emptyList())
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
                                            selectedSurahs.clear()
                                            onApplyFilters(emptyList(), selectedTranslations.toList())
                                        }
                                        .padding(vertical = 8.dp)
                                ) {
                                    Checkbox(
                                        checked = selectedSurahs.isEmpty(),
                                        onCheckedChange = { 
                                            if (it) {
                                                selectedSurahs.clear()
                                                onApplyFilters(emptyList(), selectedTranslations.toList())
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("All Surahs")
                                }
                                Divider()
                            }
                            
                            items(availableSurahs) { surah ->
                                val isSelected = selectedSurahs.contains(surah.number)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (isSelected) {
                                                selectedSurahs.remove(surah.number)
                                            } else {
                                                selectedSurahs.add(surah.number)
                                            }
                                            onApplyFilters(selectedSurahs.toList(), selectedTranslations.toList())
                                        }
                                        .padding(vertical = 8.dp)
                                ) {
                                    Checkbox(
                                        checked = isSelected,
                                        onCheckedChange = {
                                            if (it) selectedSurahs.add(surah.number)
                                            else selectedSurahs.remove(surah.number)
                                            onApplyFilters(selectedSurahs.toList(), selectedTranslations.toList())
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
                                val isSelected = selectedTranslations.contains(padding)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (isSelected) {
                                                selectedTranslations.remove(padding)
                                            } else {
                                                selectedTranslations.add(padding)
                                            }
                                            onApplyFilters(selectedSurahs.toList(), selectedTranslations.toList())
                                        }
                                        .padding(vertical = 8.dp)
                                ) {
                                    Checkbox(
                                        checked = isSelected,
                                        onCheckedChange = {
                                            if (it) selectedTranslations.add(padding)
                                            else selectedTranslations.remove(padding)
                                            onApplyFilters(selectedSurahs.toList(), selectedTranslations.toList())
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
            
            // Apply Button Removed - Filters apply immediately
        }
    }
}
