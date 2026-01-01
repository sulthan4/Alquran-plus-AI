package com.alquranplusai.android.ui.screens.quran

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alquranplusai.android.ui.viewmodels.AyahDetailViewModel
import com.alquranplusai.domain.models.Ayah
import com.alquranplusai.domain.models.TafsirText
import com.alquranplusai.domain.models.AyahTranslation
import com.alquranplusai.domain.models.Tafsir
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyahDetailScreen(
    surahNumber: Int,
    ayahNumber: Int,
    onNavigateBack: () -> Unit,
    viewModel: AyahDetailViewModel = koinViewModel()
) {
    LaunchedEffect(surahNumber, ayahNumber) {
        viewModel.loadAyah(surahNumber, ayahNumber)
    }
    
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Ayah", "Tafsir", "Analysis") // Analysis for Words/Grammar

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Surah $surahNumber : Ayah $ayahNumber") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
            
            Box(modifier = Modifier.weight(1f)) {
                 if (uiState.isLoading) {
                     CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                 } else {
                     when (selectedTab) {
                         0 -> AyahView(uiState.ayah, uiState.translations)
                         1 -> TafsirView(
                             tafsir = uiState.tafsir,
                             availableTafsirs = uiState.availableTafsirs,
                             selectedTafsirId = uiState.selectedTafsirId,
                             onSelectTafsir = { viewModel.loadTafsir(surahNumber, ayahNumber, it) }
                         )
                         2 -> WordAnalysisView(uiState.ayah)
                     }
                 }
            }
        }
    }
}

@Composable
fun AyahView(ayah: Ayah?, translations: List<AyahTranslation>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ayah?.let {
            Text(
                text = it.text,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = androidx.compose.ui.text.style.TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))
        
        translations.forEach { trans ->
            Text(
                text = trans.text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TafsirView(
    tafsir: TafsirText?,
    availableTafsirs: List<Tafsir>,
    selectedTafsirId: String?,
    onSelectTafsir: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Tafsir Selector
        if (availableTafsirs.isNotEmpty()) {
            ScrollableTabRow(
                selectedTabIndex = availableTafsirs.indexOfFirst { it.id == selectedTafsirId }.coerceAtLeast(0),
                edgePadding = 0.dp
            ) {
                availableTafsirs.forEach { t ->
                    Tab(
                        selected = t.id == selectedTafsirId,
                        onClick = { onSelectTafsir(t.id) },
                        text = { Text(t.name) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        } else {
             Text("No downloaded Tafsirs found. Go to Settings to download.", color = MaterialTheme.colorScheme.error)
        }

        // Content
        if (tafsir != null) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                     text = tafsir.text, // Assuming HTML or Plain text. If HTML, requires HtmlText
                     style = MaterialTheme.typography.bodyMedium
                )
            }
        } else if (availableTafsirs.isNotEmpty()) {
            Text("Select a Tafsir to view.")
        }
    }
}

@Composable
fun WordAnalysisView(ayah: Ayah?) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Word-by-word Analysis Coming Soon")
    }
}
