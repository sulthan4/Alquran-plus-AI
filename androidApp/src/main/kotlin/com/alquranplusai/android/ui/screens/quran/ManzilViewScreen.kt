package com.alquranplusai.android.ui.screens.quran

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun ManzilViewScreen(
    manzilNumber: Int,
    onNavigateBack: () -> Unit,
    viewModel: com.alquranplusai.android.ui.viewmodels.ManzilViewModel = koinViewModel()
) {
    val ayahs by viewModel.ayahs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    LaunchedEffect(manzilNumber) {
        viewModel.loadManzil(manzilNumber)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manzil $manzilNumber") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Manzil $manzilNumber content")
            }
        }
    }
}

