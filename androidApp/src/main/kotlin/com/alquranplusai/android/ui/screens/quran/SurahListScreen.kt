package com.alquranplusai.android.ui.screens.quran

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.alquranplusai.android.ui.viewmodels.SurahListViewModel
import com.alquranplusai.android.ui.viewmodels.JuzListViewModel
import com.alquranplusai.domain.models.Juz
import com.alquranplusai.domain.models.Surah
import com.alquranplusai.domain.models.RevelationType
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahListScreen(
    onSurahClick: (Int) -> Unit,
    onJuzClick: (Int) -> Unit,
    onAudioClick: () -> Unit,
    onSearchClick: () -> Unit,
    viewModel: SurahListViewModel = koinViewModel(),
    juzViewModel: JuzListViewModel = koinViewModel()
) {
    val filteredSurahs by viewModel.filteredSurahs.collectAsState()
    val isSurahLoading by viewModel.isLoading.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedRevelationType by viewModel.selectedRevelationType.collectAsState()
    
    val juzList by juzViewModel.juzList.collectAsState()
    val completedJuz by juzViewModel.completedJuz.collectAsState()
    val isJuzLoading by juzViewModel.isLoading.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Surah", "Juz")

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Quran Library") },
                    actions = {
                        IconButton(onClick = onAudioClick) {
                            Icon(Icons.Default.Headset, "Audio Player")
                        }
                    }
                )
                
                // Tabs
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar (Visible for both, but primarily filters Surah currently)
            if (selectedTabIndex == 0) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchSurahs(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Search Surah...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // Filter chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 0.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedRevelationType == null,
                        onClick = { viewModel.filterByRevelationType(null) },
                        label = { Text("All") }
                    )
                    FilterChip(
                        selected = selectedRevelationType == RevelationType.MECCAN,
                        onClick = { viewModel.filterByRevelationType(RevelationType.MECCAN) },
                        label = { Text("Meccan") }
                    )
                    FilterChip(
                        selected = selectedRevelationType == RevelationType.MEDINAN,
                        onClick = { viewModel.filterByRevelationType(RevelationType.MEDINAN) },
                        label = { Text("Medinan") }
                    )
                }
            }

            // Content
            when (selectedTabIndex) {
                0 -> {
                    if (isSurahLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredSurahs) { surah ->
                                SurahCard(
                                    surah = surah,
                                    onClick = { onSurahClick(surah.number) }
                                )
                            }
                        }
                    }
                }
                1 -> {
                    JuzListContent(
                        juzList = juzList,
                        completedJuz = completedJuz,
                        isLoading = isJuzLoading,
                        onJuzClick = onJuzClick
                    )
                }
            }
        }
    }
}

@Composable
fun SurahCard(
    surah: Surah,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            // Surah Number
            Box(
                modifier = Modifier
                    .size(40.dp),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                // You could add a decorative star/shape image behind the text here
                Text(
                    text = "${surah.number}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // English Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = surah.nameTransliteration,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = surah.nameTranslation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Text(
                        text = surah.revelationType.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = " • ",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = "${surah.numberOfAyahs} ayahs",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Arabic Name
            Text(
                text = surah.nameArabic,
                style = MaterialTheme.typography.headlineSmall, // Larger for Arabic
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}
