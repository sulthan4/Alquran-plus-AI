package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val audioRepository: com.alquranplusai.domain.repositories.AudioRepository
) : ViewModel() {
    
    private val _playlists = MutableStateFlow<List<com.alquranplusai.domain.models.Playlist>>(emptyList())
    val playlists: StateFlow<List<com.alquranplusai.domain.models.Playlist>> = _playlists.asStateFlow()
    
    private val _currentPlaylist = MutableStateFlow<com.alquranplusai.domain.models.Playlist?>(null)
    val currentPlaylist: StateFlow<com.alquranplusai.domain.models.Playlist?> = _currentPlaylist.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _playlistItems = MutableStateFlow<List<com.alquranplusai.domain.models.PlaylistItem>>(emptyList())
    val playlistItems: StateFlow<List<com.alquranplusai.domain.models.PlaylistItem>> = _playlistItems.asStateFlow()
    
    init {
        loadPlaylists()
    }
    
    private fun loadPlaylists() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                audioRepository.getAllPlaylists().collect { playlistList ->
                    _playlists.value = playlistList
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load playlists"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun createPlaylist(name: String, description: String) {
        viewModelScope.launch {
            try {
                audioRepository.createPlaylist(name, description)
                loadPlaylists()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to create playlist"
            }
        }
    }
    
    fun updatePlaylist(playlistId: Long, name: String, description: String) {
        viewModelScope.launch {
            try {
                audioRepository.updatePlaylist(playlistId, name, description)
                loadPlaylists()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to update playlist"
            }
        }
    }
    
    fun loadPlaylistItems(playlistId: Long) {
        viewModelScope.launch {
            try {
                audioRepository.getPlaylistItems(playlistId).collect { items ->
                    _playlistItems.value = items
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load playlist items"
            }
        }
    }
    
    fun addToPlaylist(playlistId: Long, surahNumber: Int, ayahNumber: Int = 1) {
        viewModelScope.launch {
            try {
                audioRepository.addToPlaylist(playlistId, surahNumber, ayahNumber)
                loadPlaylistItems(playlistId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to add to playlist"
            }
        }
    }
    
    fun removeFromPlaylist(playlistId: Long, itemId: Long) {
        viewModelScope.launch {
            try {
                audioRepository.removeFromPlaylist(playlistId, itemId)
                loadPlaylistItems(playlistId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to remove from playlist"
            }
        }
    }
    
    fun selectPlaylist(playlist: com.alquranplusai.domain.models.Playlist) {
        _currentPlaylist.value = playlist
        loadPlaylistItems(playlist.id.toLong())
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            try {
                audioRepository.deletePlaylist(playlistId)
                loadPlaylists()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to delete playlist"
            }
        }
    }
}
