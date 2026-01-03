package com.alquranplusai.data.network.api

import com.alquranplusai.data.network.dto.*

class FakeAudioApiService : AudioApiService {
    
    var reciters = emptyList<ReciterDto>()
    var audioFiles = emptyList<AudioDto>()
    var playlists = emptyList<PlaylistDto>()
    
    var shouldThrowException = false

    override suspend fun getAllReciters(): List<ReciterDto> {
        if (shouldThrowException) throw Exception("Network error")
        return reciters
    }

    override suspend fun getReciter(id: Int): ReciterDto {
         if (shouldThrowException) throw Exception("Network error")
         return reciters.find { it.id == id } ?: throw Exception("Reciter not found")
    }

    override suspend fun getAudioFiles(reciterId: Int, surahNumber: Int): List<AudioDto> {
        if (shouldThrowException) throw Exception("Network error")
        return audioFiles // Simplified
    }

    override suspend fun getAudioFile(reciterId: Int, surahNumber: Int, ayahNumber: Int?): AudioDto {
        if (shouldThrowException) throw Exception("Network error")
        return audioFiles.firstOrNull() ?: AudioDto(
             id = 0,
             reciterId = reciterId,
             surahNumber = surahNumber,
             fileSize = 1000L,
             format = "mp3",
             url = "http://example.com/audio.mp3"
        )
    }

    override suspend fun getUserPlaylists(userId: Int): List<PlaylistDto> {
        return playlists
    }

    override suspend fun createPlaylist(userId: Int, playlist: PlaylistDto): PlaylistDto {
        return playlist
    }

    override suspend fun updatePlaylist(playlistId: Int, playlist: PlaylistDto): PlaylistDto {
        return playlist
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        
    }
}
