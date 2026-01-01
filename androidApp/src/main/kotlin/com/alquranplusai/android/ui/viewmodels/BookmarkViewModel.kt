package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.Bookmark
import com.alquranplusai.domain.repositories.BookmarkRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookmarkViewModel(
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {
    
    private val _bookmarks = MutableStateFlow<List<Bookmark>>(emptyList())
    val bookmarks: StateFlow<List<Bookmark>> = _bookmarks.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _folders = MutableStateFlow<List<String>>(emptyList())
    val folders: StateFlow<List<String>> = _folders.asStateFlow()
    
    init {
        loadBookmarks()
        loadFolders()
    }
    
    fun loadBookmarks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                bookmarkRepository.getAllBookmarks().collect { list ->
                    _bookmarks.value = list
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadFolders() {
        viewModelScope.launch {
            try {
                bookmarkRepository.getAllFolders().collect { folderList ->
                    _folders.value = folderList.map { it.name }
                }
            } catch (e: Exception) {
                // Ignore folder loading errors
            }
        }
    }
    
    fun toggleBookmark(surahNumber: Int, ayahNumber: Int) {
        viewModelScope.launch {
            val isCurrentlyBookmarked = _bookmarks.value.any { it.surahNumber == surahNumber && it.ayahNumber == ayahNumber }
            if (isCurrentlyBookmarked) {
                val bookmark = _bookmarks.value.find { it.surahNumber == surahNumber && it.ayahNumber == ayahNumber }
                bookmark?.let { bookmarkRepository.deleteBookmark(it.id) }
            } else {
                bookmarkRepository.createBookmark(surahNumber, ayahNumber, null, null).collect { id ->
                    // New bookmark created
                }
            }
            loadBookmarks()
        }
    }

    fun deleteBookmark(id: String) {
        viewModelScope.launch {
            bookmarkRepository.deleteBookmark(id)
            loadBookmarks()
        }
    }
    
    // Note management
    fun addNoteToBookmark(bookmarkId: String, note: String) {
        viewModelScope.launch {
            val bookmark = _bookmarks.value.find { it.id == bookmarkId }
            if (bookmark != null) {
                bookmarkRepository.updateBookmark(bookmarkId, note, bookmark.folderId)
                loadBookmarks()
            }
        }
    }
    
    fun updateBookmarkNote(bookmarkId: String, note: String) {
        viewModelScope.launch {
            val bookmark = _bookmarks.value.find { it.id == bookmarkId }
            if (bookmark != null) {
                bookmarkRepository.updateBookmark(bookmarkId, note, bookmark.folderId)
                loadBookmarks()
            }
        }
    }
    
    fun deleteBookmarkNote(bookmarkId: String) {
        viewModelScope.launch {
            val bookmark = _bookmarks.value.find { it.id == bookmarkId }
            if (bookmark != null) {
                bookmarkRepository.updateBookmark(bookmarkId, null, bookmark.folderId)
                loadBookmarks()
            }
        }
    }
    
    // Folder management
    fun createFolder(name: String) {
        viewModelScope.launch {
            bookmarkRepository.createFolder(name, null, null).collect { id ->
                loadFolders()
            }
        }
    }
    
    fun moveBookmarkToFolder(bookmarkId: String, folderId: String?) {
        viewModelScope.launch {
            val bookmark = _bookmarks.value.find { it.id == bookmarkId }
            if (bookmark != null) {
                bookmarkRepository.updateBookmark(bookmarkId, bookmark.note, folderId)
                loadBookmarks()
            }
        }
    }
    
    // Search
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            loadBookmarks()
        } else {
            viewModelScope.launch {
                bookmarkRepository.searchBookmarks(query).collect { results ->
                    _bookmarks.value = results
                }
            }
        }
    }
    
    fun clearSearch() {
        _searchQuery.value = ""
        loadBookmarks()
    }
}
