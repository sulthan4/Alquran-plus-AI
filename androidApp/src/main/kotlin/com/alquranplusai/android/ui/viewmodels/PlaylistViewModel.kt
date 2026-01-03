package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.repositories.AudioRepository
import com.alquranplusai.domain.models.Playlist
import com.alquranplusai.domain.models.PlaylistItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val audioRepository: AudioRepository
) : ViewModel() {
    
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()
    
    private val _currentPlaylist = MutableStateFlow<Playlist?>(null)
    val currentPlaylist: StateFlow<Playlist?> = _currentPlaylist.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _playlistItems = MutableStateFlow<List<PlaylistItem>>(emptyList())
    val playlistItems: StateFlow<List<PlaylistItem>> = _playlistItems.asStateFlow()
    
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
    
    fun updatePlaylist(playlistId: String, name: String, description: String) {
        viewModelScope.launch {
            try {
                audioRepository.updatePlaylist(playlistId, name, description)
                loadPlaylists()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to update playlist"
            }
        }
    }
    
    fun loadPlaylistItems(playlistId: String) {
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
    
    fun addToPlaylist(playlistId: String, reciterId: String, surahNumber: Int, ayahNumber: Int = 1) {
        viewModelScope.launch {
            try {
                audioRepository.addToPlaylist(playlistId, reciterId, surahNumber, ayahNumber)
                loadPlaylistItems(playlistId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to add to playlist"
            }
        }
    }
    
    fun removeFromPlaylist(playlistId: String, itemId: String) {
        viewModelScope.launch {
            try {
                audioRepository.removeFromPlaylist(playlistId, itemId)
                loadPlaylistItems(playlistId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to remove from playlist"
            }
        }
    }
    
    fun selectPlaylist(playlist: Playlist) {
        _currentPlaylist.value = playlist
        loadPlaylistItems(playlist.id)
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun deletePlaylist(playlistId: String) {
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
