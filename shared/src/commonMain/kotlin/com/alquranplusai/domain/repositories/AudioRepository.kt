package com.alquranplusai.domain.repositories

import com.alquranplusai.domain.models.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository for audio and recitation operations
 */
interface AudioRepository {
    // Reciter operations
    suspend fun getAllReciters(): Flow<List<Reciter>>
    suspend fun getReciterById(id: String): Flow<Reciter?>
    suspend fun getRecitersByLanguage(languageCode: String): Flow<List<Reciter>>
    
    // Audio file operations
    suspend fun getAudioFile(reciterId: String, surahNumber: Int, ayahNumber: Int?): Flow<AudioFile?>
    suspend fun downloadAudio(reciterId: String, surahNumber: Int, ayahNumber: Int?): Flow<DownloadProgress>
    suspend fun deleteAudio(reciterId: String, surahNumber: Int, ayahNumber: Int?)
    suspend fun getDownloadedAudio(reciterId: String): Flow<List<AudioFile>>
    
    // Playlist operations
    suspend fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun getPlaylistById(id: String): Flow<Playlist?>
    suspend fun createPlaylist(name: String, description: String?): Flow<String>
    suspend fun updatePlaylist(id: String, name: String, description: String?)
    suspend fun deletePlaylist(id: String)
    suspend fun addToPlaylist(playlistId: String, reciterId: String, surahNumber: Int, ayahNumber: Int?)
    suspend fun removeFromPlaylist(playlistId: String, itemId: String)
    suspend fun getPlaylistItems(playlistId: String): Flow<List<PlaylistItem>>
}
