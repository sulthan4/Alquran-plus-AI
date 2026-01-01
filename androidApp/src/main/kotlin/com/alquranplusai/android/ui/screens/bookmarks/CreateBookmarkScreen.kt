package com.alquranplusai.android.ui.screens.bookmarks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alquranplusai.android.ui.viewmodels.BookmarksViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Main Bookmarks Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBookmarkScreen(
    surahNumber: Int,
    ayahNumber: Int,
    onBookmarkCreated: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: com.alquranplusai.android.ui.viewmodels.BookmarksViewModel = koinViewModel()
) {
    var note by remember { mutableStateOf("") }
    var selectedFolder by remember { mutableStateOf<String?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Bookmark") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Surah $surahNumber:$ayahNumber")
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (optional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    viewModel.addBookmark(surahNumber, ayahNumber, selectedFolder, note)
                    onBookmarkCreated()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Bookmark")
            }
        }
    }
}

