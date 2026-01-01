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
@Composable
fun EditBookmarkScreen(
    bookmarkId: Long,
    onBookmarkUpdated: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var note by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Bookmark") },
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
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onBookmarkUpdated,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update")
            }
        }
    }
}
