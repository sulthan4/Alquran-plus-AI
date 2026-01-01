package com.alquranplusai.android.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun FontSelectionScreen(
    onFontSelected: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val fonts = listOf("Amiri", "Uthmani", "KFGQPC", "Noto Naskh Arabic")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Font") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(fonts.size) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    onClick = { onFontSelected(fonts[index]) }
                ) {
                    Text(
                        text = fonts[index],
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

