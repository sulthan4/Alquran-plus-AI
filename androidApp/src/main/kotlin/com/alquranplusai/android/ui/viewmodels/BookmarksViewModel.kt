package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.Bookmark
import com.alquranplusai.domain.models.BookmarkFolder
import com.alquranplusai.domain.repositories.BookmarkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookmarksViewModel(
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {
    
    private val _bookmarks = MutableStateFlow<List<Bookmark>>(emptyList())
    val bookmarks: StateFlow<List<Bookmark>> = _bookmarks.asStateFlow()
    
    private val _folders = MutableStateFlow<List<BookmarkFolder>>(emptyList())
    val folders: StateFlow<List<BookmarkFolder>> = _folders.asStateFlow()
    
    private val _selectedFolder = MutableStateFlow<BookmarkFolder?>(null)
    val selectedFolder: StateFlow<BookmarkFolder?> = _selectedFolder.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    init {
        loadBookmarks()
        loadFolders()
    }
    
    private fun loadBookmarks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                bookmarkRepository.getAllBookmarks().collect { bookmarkList ->
                    _bookmarks.value = bookmarkList
                }
            } catch (e: Exception) {
                // TODO: Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun loadFolders() {
        viewModelScope.launch {
            try {
                bookmarkRepository.getAllFolders().collect { folderList ->
                    _folders.value = folderList
                }
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
    
    fun selectFolder(folder: BookmarkFolder?) {
        _selectedFolder.value = folder
        if (folder != null) {
            loadBookmarksByFolder(folder.id)
        } else {
            loadBookmarks()
        }
    }
    
    private fun loadBookmarksByFolder(folderId: String) {
        viewModelScope.launch {
            try {
                bookmarkRepository.getBookmarksByFolder(folderId).collect { bookmarkList ->
                    _bookmarks.value = bookmarkList
                }
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
    
    fun addBookmark(surahNumber: Int, ayahNumber: Int, folderId: String? = null, note: String? = null) {
        viewModelScope.launch {
            try {
                // TODO: Create bookmark and add
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
    
    fun deleteBookmark(bookmarkId: String) {
        viewModelScope.launch {
            try {
                bookmarkRepository.deleteBookmark(bookmarkId)
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
    
    fun createFolder(name: String, color: String) {
        viewModelScope.launch {
            try {
                // TODO: Create folder
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
    
    fun deleteFolder(folderId: String) {
        viewModelScope.launch {
            try {
                bookmarkRepository.deleteFolder(folderId)
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
    
    fun searchBookmarks(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            try {
                bookmarkRepository.searchBookmarks(query).collect { results ->
                    _bookmarks.value = results
                }
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
}
