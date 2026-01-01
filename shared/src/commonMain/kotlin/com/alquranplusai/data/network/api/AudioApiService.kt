package com.alquranplusai.data.network.api

import com.alquranplusai.data.network.dto.AudioDto
import com.alquranplusai.data.network.dto.ReciterDto
import com.alquranplusai.data.network.dto.PlaylistDto

/**
 * API service for audio content
 */
interface AudioApiService {
    
    suspend fun getAllReciters(): List<ReciterDto>
    
    suspend fun getReciter(id: Int): ReciterDto
    
    suspend fun getAudioFiles(reciterId: Int, surahNumber: Int): List<AudioDto>
    
    suspend fun getAudioFile(reciterId: Int, surahNumber: Int, ayahNumber: Int?): AudioDto
    
    suspend fun getUserPlaylists(userId: Int): List<PlaylistDto>
    
    suspend fun createPlaylist(userId: Int, playlist: PlaylistDto): PlaylistDto
    
    suspend fun updatePlaylist(playlistId: Int, playlist: PlaylistDto): PlaylistDto
    
    suspend fun deletePlaylist(playlistId: Int)
}
