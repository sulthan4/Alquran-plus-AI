package com.alquranplusai.android.ui.screens.bookmarks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alquranplusai.android.ui.components.dialogs.*
import com.alquranplusai.android.ui.theme.*
import com.alquranplusai.android.ui.viewmodels.BookmarkViewModel
import com.alquranplusai.domain.models.Bookmark
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    onNavigateToReading: (Int, Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: BookmarkViewModel = koinViewModel()
) {
    val bookmarks by viewModel.bookmarks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val folders by viewModel.folders.collectAsState()
    
    var showEditNoteDialog by remember { mutableStateOf(false) }
    var showFolderDialog by remember { mutableStateOf(false) }
    var showCreateFolderDialog by remember { mutableStateOf(false) }
    var selectedBookmark by remember { mutableStateOf<Bookmark?>(null) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BookmarksGradient)
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = Spacing.md, end = Spacing.md, bottom = Spacing.sm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(Spacing.sm))
                    Text(
                        text = "Bookmarks",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.md, vertical = Spacing.sm),
                    placeholder = { Text("Search bookmarks...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, "Search")
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.clearSearch() }) {
                                Icon(Icons.Default.Clear, "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.9f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.8f),
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                    )
                )
                
                Spacer(modifier = Modifier.height(Spacing.sm))
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Bookmarks)
            }
        } else if (bookmarks.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(Spacing.md))
                    Text(
                        if (searchQuery.isEmpty()) "No bookmarks yet" else "No bookmarks found",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(Spacing.sm))
                    Text(
                        if (searchQuery.isEmpty()) "Bookmark ayahs while reading" else "Try a different search",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                items(bookmarks) { bookmark ->
                    BookmarkItem(
                        bookmark = bookmark,
                        onClick = { onNavigateToReading(bookmark.surahNumber, bookmark.ayahNumber) },
                        onDelete = { viewModel.deleteBookmark(bookmark.id) },
                        onEditNote = {
                            selectedBookmark = bookmark
                            showEditNoteDialog = true
                        },
                        onMoveToFolder = {
                            selectedBookmark = bookmark
                            showFolderDialog = true
                        }
                    )
                }
            }
        }
    }
    
    // Dialogs
    if (showEditNoteDialog && selectedBookmark != null) {
        if (selectedBookmark!!.note.isNullOrBlank()) {
            AddNoteDialog(
                surahNumber = selectedBookmark!!.surahNumber,
                ayahNumber = selectedBookmark!!.ayahNumber,
                onDismiss = { showEditNoteDialog = false },
                onSave = { note ->
                    viewModel.addNoteToBookmark(selectedBookmark!!.id, note)
                }
            )
        } else {
            EditNoteDialog(
                surahNumber = selectedBookmark!!.surahNumber,
                ayahNumber = selectedBookmark!!.ayahNumber,
                currentNote = selectedBookmark!!.note!!,
                onDismiss = { showEditNoteDialog = false },
                onSave = { note ->
                    viewModel.updateBookmarkNote(selectedBookmark!!.id, note)
                },
                onDelete = {
                    viewModel.deleteBookmarkNote(selectedBookmark!!.id)
                }
            )
        }
    }
    
    if (showFolderDialog && selectedBookmark != null) {
        FolderSelectionDialog(
            folders = folders,
            currentFolderId = selectedBookmark!!.folderId,
            onDismiss = { showFolderDialog = false },
            onSelect = { folderId ->
                viewModel.moveBookmarkToFolder(selectedBookmark!!.id, folderId)
            },
            onCreateNew = {
                showFolderDialog = false
                showCreateFolderDialog = true
            }
        )
    }
    
    if (showCreateFolderDialog) {
        CreateFolderDialog(
            onDismiss = { showCreateFolderDialog = false },
            onCreate = { name ->
                viewModel.createFolder(name)
            }
        )
    }
}

@Composable
fun BookmarkItem(
    bookmark: Bookmark,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onEditNote: () -> Unit,
    onMoveToFolder: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.medium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bookmark indicator with gradient
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(48.dp)
                    .background(BookmarksGradient)
            )
            
            Spacer(modifier = Modifier.width(Spacing.md))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Surah ${bookmark.surahNumber}:${bookmark.ayahNumber}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Bookmarks
                    )
                    
                    // Note indicator
                    if (!bookmark.note.isNullOrBlank()) {
                        Spacer(modifier = Modifier.width(Spacing.sm))
                        Icon(
                            Icons.Default.Note,
                            contentDescription = "Has note",
                            modifier = Modifier.size(16.dp),
                            tint = Bookmarks
                        )
                    }
                    
                    // Folder indicator
                    if (bookmark.folderId != null) {
                        Spacer(modifier = Modifier.width(Spacing.sm))
                        Icon(
                            Icons.Default.Folder,
                            contentDescription = "In folder",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                
                if (!bookmark.note.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(Spacing.xs))
                    Text(
                        text = bookmark.note!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }
                
                if (bookmark.createdAt != null) {
                    Spacer(modifier = Modifier.height(Spacing.xs))
                    Text(
                        text = "Added ${formatTimestamp(bookmark.createdAt!!)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // More options menu
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "More options"
                    )
                }
                
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(if (bookmark.note.isNullOrBlank()) "Add Note" else "Edit Note") },
                        onClick = {
                            showMenu = false
                            onEditNote()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Edit, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Move to Folder") },
                        onClick = {
                            showMenu = false
                            onMoveToFolder()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Folder, contentDescription = null)
                        }
                    )
                    Divider()
                    DropdownMenuItem(
                        text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                        onClick = {
                            showMenu = false
                            onDelete()
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val days = diff / (1000 * 60 * 60 * 24)
    
    return when {
        days == 0L -> "today"
        days == 1L -> "yesterday"
        days < 7 -> "$days days ago"
        days < 30 -> "${days / 7} weeks ago"
        else -> "${days / 30} months ago"
    }
}

