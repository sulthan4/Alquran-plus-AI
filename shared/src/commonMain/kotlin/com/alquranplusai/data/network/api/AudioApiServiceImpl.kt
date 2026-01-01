package com.alquranplusai.data.network.api

import com.alquranplusai.data.network.dto.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class AudioApiServiceImpl(
    private val client: HttpClient
) : AudioApiService {

    private val baseUrl = "https://api.quran.com/api/v4"

    override suspend fun getAllReciters(): List<ReciterDto> {
        val response: QuranFoundationRecitersResponse = client.get("$baseUrl/resources/recitations") {
            parameter("language", "en")
        }.body()

        return response.recitations.map { qfReciter ->
            ReciterDto(
                id = qfReciter.id,
                name = qfReciter.reciterName,
                nameArabic = qfReciter.translatedName?.name,
                style = qfReciter.style ?: "Murattal",
                languageCode = "ar", // Default to Arabic
                bitrate = 128, // Default common bitrate
                hasWordTiming = qfReciter.hasWordTiming
            )
        }
    }

    override suspend fun getReciter(id: Int): ReciterDto {
        val reciters = getAllReciters()
        return reciters.firstOrNull { it.id == id } 
            ?: throw Exception("Reciter not found")
    }

    override suspend fun getAudioFiles(reciterId: Int, surahNumber: Int): List<AudioDto> {
        // This endpoint returns the audio file for a specific chapter
        // When requesting /chapter_recitations/:reciter_id/:surah_number, 
        // the response key is "audio_file" (singular)
        val response: QuranFoundationSingleChapterRecitationResponse = 
            client.get("$baseUrl/chapter_recitations/$reciterId/$surahNumber") {
                parameter("segments", "true")
            }.body()
        
        println("AlQuranPlusAI: API returned ${response.audioFile.verseTimings.size} verse timings for reciter $reciterId")
        
        val audioFile = response.audioFile
        val url = if (audioFile.audioUrl.startsWith("//")) {
            "https:${audioFile.audioUrl}"
        } else if (!audioFile.audioUrl.startsWith("http")) {
            "https://audio.quran.com/${audioFile.audioUrl}"
        } else {
            audioFile.audioUrl
        }
        
        return listOf(
            AudioDto(
                id = audioFile.id,
                reciterId = reciterId,
                surahNumber = audioFile.chapterId,
                ayahNumber = null, 
                url = url,
                format = audioFile.format,
                quality = "high",
                fileSize = audioFile.fileSize.toLong(),
                verseTimings = audioFile.verseTimings.map { vt ->
                    VerseTimingDto(
                        verseKey = vt.verseKey,
                        from = vt.timestampFrom,
                        to = vt.timestampTo,
                        segments = vt.segments
                    )
                }
            )
        )
    }

    override suspend fun getAudioFile(reciterId: Int, surahNumber: Int, ayahNumber: Int?): AudioDto {
        // QF V4 audio is primarily chapter-based. We return the chapter audio 
        // and expect the player or timing logic to handle ayahs if needed.
        val validFiles = getAudioFiles(reciterId, surahNumber)
        return validFiles.firstOrNull() ?: throw Exception("Audio not found")
    }

    override suspend fun getUserPlaylists(userId: Int): List<PlaylistDto> {
        // Placeholder as QF doesn't have user playlists yet (custom backend feature)
        return emptyList()
    }

    override suspend fun createPlaylist(userId: Int, playlist: PlaylistDto): PlaylistDto {
        // Placeholder
        return playlist
    }

    override suspend fun updatePlaylist(playlistId: Int, playlist: PlaylistDto): PlaylistDto {
        // Placeholder
        return playlist
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        // Placeholder
    }
}
