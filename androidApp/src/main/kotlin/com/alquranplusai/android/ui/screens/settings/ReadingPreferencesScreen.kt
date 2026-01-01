package com.alquranplusai.android.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alquranplusai.android.ui.viewmodels.ReadingPreferencesViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingPreferencesScreen(
    onNavigateBack: () -> Unit,
    viewModel: ReadingPreferencesViewModel = koinViewModel()
) {
    val fontSize by viewModel.fontSize.collectAsState()
    val readingMode by viewModel.readingMode.collectAsState()
    val showWordByWord by viewModel.showWordByWord.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reading Preferences") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            item {
                Text("Font Size: ${fontSize.toInt()}")
                Slider(
                    value = fontSize,
                    onValueChange = { viewModel.setFontSize(it.toInt()) },
                    valueRange = 16f..48f
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Reading Mode: $readingMode")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { viewModel.setReadingMode("CONTINUOUS") }, enabled = readingMode != "CONTINUOUS") { Text("Continuous") }
                    Button(onClick = { viewModel.setReadingMode("PAGE") }, enabled = readingMode != "PAGE") { Text("Page") }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Show Word by Word")
                    Switch(checked = showWordByWord, onCheckedChange = { viewModel.setShowWordByWord(it) })
                }
            }

            item {
                Text(
                    text = "Bismillah...",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = androidx.compose.ui.unit.TextUnit(fontSize, androidx.compose.ui.unit.TextUnitType.Sp)
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
