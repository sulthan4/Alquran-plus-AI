package com.alquranplusai.android.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alquranplusai.android.ui.viewmodels.SearchViewModel
import com.alquranplusai.domain.models.SearchResult
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color

@Composable
fun parseHtmlToAnnotatedString(text: String, highlightColor: Color): AnnotatedString {
    return buildAnnotatedString {
        var currentIndex = 0
        val emStart = "<em>"
        val emEnd = "</em>"

        while (currentIndex < text.length) {
            val startIndex = text.indexOf(emStart, currentIndex)
            if (startIndex == -1) {
                append(text.substring(currentIndex))
                break
            }

            append(text.substring(currentIndex, startIndex))
            val contentStart = startIndex + emStart.length
            val endIndex = text.indexOf(emEnd, contentStart)

            if (endIndex == -1) {
                append(text.substring(startIndex))
                break
            }

            pushStyle(SpanStyle(color = highlightColor, fontWeight = FontWeight.Bold))
            append(text.substring(contentStart, endIndex))
            pop()

            currentIndex = endIndex + emEnd.length
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateToReading: (Int, Int) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToVoiceSearch: () -> Unit = {}, // Added navigation callback
    viewModel: SearchViewModel = koinViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val recentSearches by viewModel.recentSearches.collectAsState()
    val searchType by viewModel.searchType.collectAsState()
    
    // Filter State
    val availableSurahs by viewModel.availableSurahs.collectAsState()
    val availableTranslations by viewModel.availableTranslations.collectAsState()
    val selectedSurahs by viewModel.selectedSurahs.collectAsState()
    val selectedTranslations by viewModel.selectedTranslations.collectAsState()

    var showFilters by remember { mutableStateOf(false) }
    
    if (showFilters) {
        com.alquranplusai.android.ui.screens.search.SearchFilterSheet(
            onDismiss = { showFilters = false },
            onApplyFilters = { surahs, translations ->
                viewModel.applyFilters(surahs, translations)
                showFilters = false
            },
            availableSurahs = availableSurahs,
            availableTranslations = availableTranslations,
            initialSelectedSurahs = selectedSurahs,
            initialSelectedTranslations = selectedTranslations
        )
    }

    Scaffold(
        topBar = {
            SearchTopBar(
                query = searchQuery,
                onQueryChange = { viewModel.onQueryChanged(it) },
                onClear = { viewModel.clearSearch() },
                onNavigateBack = onNavigateBack,
                onFilterClick = { showFilters = true },
                onVoiceClick = onNavigateToVoiceSearch
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Mode Tabs
            SearchModeTabs(
                selectedType = searchType,
                onTypeSelected = { viewModel.setSearchType(it) }
            )

            // Main Content
            when {
                isSearching -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                searchQuery.isBlank() -> {
                    RecentSearchesSection(
                        recentSearches = recentSearches,
                        onSearchClick = { viewModel.search(it) },
                        onDeleteClick = { viewModel.deleteRecentSearch(it) },
                        onClearAll = { viewModel.clearRecentSearches() }
                    )
                }
                searchResults.isEmpty() -> {
                    EmptySearchResults(query = searchQuery)
                }
                else -> {
                    SearchResultsList(
                        results = searchResults,
                        onResultClick = { result ->
                            onNavigateToReading(result.surahNumber, result.ayahNumber)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    onNavigateBack: () -> Unit,
    onFilterClick: () -> Unit,
    onVoiceClick: () -> Unit
) {
    TopAppBar(
        title = {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search Quran...") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                    unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
                ),
                trailingIcon = {
                    Row {
                         if (query.isNotEmpty()) {
                             IconButton(onClick = onClear) {
                                 Icon(Icons.Default.Clear, "Clear")
                             }
                         } else {
                             // Only show voice when query is empty, or always? Always is better for correction.
                             // But standard pattern is Mic usually replaces Send/Search when empty.
                             IconButton(onClick = onVoiceClick) {
                                 Icon(Icons.Default.Mic, "Voice Search")
                             }
                         }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, "Back")
            }
        },
        actions = {
            IconButton(onClick = onFilterClick) {
                Icon(Icons.Default.FilterList, "Filters")
            }
        }
    )
}

@Composable
fun SearchModeTabs(
    selectedType: com.alquranplusai.domain.models.SearchType,
    onTypeSelected: (com.alquranplusai.domain.models.SearchType) -> Unit
) {
    val types = com.alquranplusai.domain.models.SearchType.entries.filter { 
        it == com.alquranplusai.domain.models.SearchType.TEXT || 
        it == com.alquranplusai.domain.models.SearchType.SEMANTIC || 
        it == com.alquranplusai.domain.models.SearchType.ROOT || 
        it == com.alquranplusai.domain.models.SearchType.TOPIC 
    }
    
    val selectedIndex = types.indexOf(selectedType).coerceAtLeast(0)

    ScrollableTabRow(selectedTabIndex = selectedIndex) {
        types.forEach { type ->
            Tab(
                selected = selectedType == type,
                onClick = { onTypeSelected(type) },
                text = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}

@Composable
fun RecentSearchesSection(
    recentSearches: List<String>,
    onSearchClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    onClearAll: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Recent Searches",
                style = MaterialTheme.typography.titleMedium
            )
            if (recentSearches.isNotEmpty()) {
                TextButton(onClick = onClearAll) {
                    Text("Clear All")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (recentSearches.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No recent searches",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(recentSearches) { search ->
                    RecentSearchItem(
                        query = search,
                        onClick = { onSearchClick(search) },
                        onDelete = { onDeleteClick(search) }
                    )
                }
            }
        }
    }
}

@Composable
fun RecentSearchItem(
    query: String,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.History,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                query,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SearchResultsList(
    results: List<SearchResult>,
    onResultClick: (SearchResult) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                "${results.size} results found",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(results) { result ->
            SearchResultCard(
                result = result,
                onClick = { onResultClick(result) }
            )
        }
    }
}

@Composable
fun SearchResultCard(
    result: SearchResult,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Surah and Ayah info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Surah ${result.surahNumber}:${result.ayahNumber}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                // Match type badge
                result.matchType?.let { matchType ->
                    Surface(
                        color = when (matchType.name) {
                            "EXACT" -> MaterialTheme.colorScheme.primaryContainer
                            "SEMANTIC" -> MaterialTheme.colorScheme.secondaryContainer
                            "ROOT" -> MaterialTheme.colorScheme.tertiaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            matchType.name,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Arabic text
            CompositionLocalProvider(
                androidx.compose.ui.platform.LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Rtl
            ) {
                val displayText = result.highlightedText ?: result.translation ?: result.text
                val annotatedString = parseHtmlToAnnotatedString(
                    text = displayText,
                    highlightColor = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = annotatedString,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Relevance score
            if (result.relevanceScore > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { result.relevanceScore },
                    modifier = Modifier.fillMaxWidth().height(2.dp),
                )
            }
        }
    }
}

@Composable
fun EmptySearchResults(query: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "No results found for \"$query\"",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Try different keywords or check spelling",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
