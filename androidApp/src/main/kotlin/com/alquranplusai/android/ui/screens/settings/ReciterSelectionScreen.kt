package com.alquranplusai.android.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReciterSelectionScreen(
    onReciterSelected: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val reciters = listOf(
        "Abdul Basit",
        "Mishary Rashid",
        "Saad Al-Ghamdi",
        "Maher Al-Muaiqly"
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Reciter") },
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
            items(reciters.size) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    onClick = { onReciterSelected(reciters[index]) }
                ) {
                    Text(
                        text = reciters[index],
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

