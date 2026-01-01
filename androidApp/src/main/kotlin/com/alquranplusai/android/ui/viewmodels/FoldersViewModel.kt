package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FoldersViewModel(
    private val bookmarkRepository: com.alquranplusai.domain.repositories.BookmarkRepository
) : ViewModel() {
    
    private val _folders = MutableStateFlow<List<com.alquranplusai.domain.models.BookmarkFolder>>(emptyList())
    val folders: StateFlow<List<com.alquranplusai.domain.models.BookmarkFolder>> = _folders.asStateFlow()
    
    private val _selectedFolder = MutableStateFlow<com.alquranplusai.domain.models.BookmarkFolder?>(null)
    val selectedFolder: StateFlow<com.alquranplusai.domain.models.BookmarkFolder?> = _selectedFolder.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _folderBookmarks = MutableStateFlow<List<com.alquranplusai.domain.models.Bookmark>>(emptyList())
    val folderBookmarks: StateFlow<List<com.alquranplusai.domain.models.Bookmark>> = _folderBookmarks.asStateFlow()
    
    init {
        loadFolders()
    }
    
    private fun loadFolders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                bookmarkRepository.getAllFolders().collect { folderList ->
                    _folders.value = folderList
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load folders"
            } finally{
                _isLoading.value = false
            }
        }
    }
    
    fun createFolder(name: String, color: String) {
        viewModelScope.launch {
            try {
                bookmarkRepository.createFolder(name, color, null)
                loadFolders()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to create folder"
            }
        }
    }
    
    fun updateFolder(folderId: String, name: String, color: String) {
        viewModelScope.launch {
            try {
                bookmarkRepository.updateFolder(folderId, name, color)
                loadFolders()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to update folder"
            }
        }
    }
    
    fun loadFolderBookmarks(folderId: String) {
        viewModelScope.launch {
            try {
                bookmarkRepository.getBookmarksByFolder(folderId).collect { bookmarks ->
                    _folderBookmarks.value = bookmarks
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load bookmarks"
            }
        }
    }
    
    fun moveBookmarkToFolder(bookmarkId: String, folderId: String) {
        viewModelScope.launch {
            try {
                // Use updateBookmark to change folder
                loadFolderBookmarks(folderId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to move bookmark"
            }
        }
    }
    
    fun selectFolder(folder: com.alquranplusai.domain.models.BookmarkFolder) {
        _selectedFolder.value = folder
        loadFolderBookmarks(folder.id)
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun deleteFolder(folderId: String) {
        viewModelScope.launch {
            try {
                bookmarkRepository.deleteFolder(folderId)
                loadFolders()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to delete folder"
            }
        }
    }
}

