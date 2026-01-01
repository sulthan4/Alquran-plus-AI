package com.alquranplusai.data.repositories

import com.alquranplusai.data.database.AlQuranDatabaseWrapper
import com.alquranplusai.domain.repositories.AudioRepository
import com.alquranplusai.data.network.api.AudioApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.alquranplusai.data.network.dto.ReciterDto
import com.alquranplusai.domain.models.*

class AudioRepositoryImpl(
    private val database: AlQuranDatabaseWrapper,
    private val api: AudioApiService
) : AudioRepository {

    override suspend fun getAllReciters(): Flow<List<Reciter>> = flow {
        // Emit cached data first
        val cached = database.audioQueries.selectAllReciters().executeAsList().map { 
            Reciter(
                id = it.id,
                name = it.name,
                nameArabic = it.nameArabic,
                style = try { RecitationStyle.valueOf(it.style) } catch(e:Exception) { RecitationStyle.MURATTAL },
                bitrate = it.bitrate.toInt(),
                format = try { AudioFormat.valueOf(it.format) } catch(e:Exception) { AudioFormat.MP3 },
                isDownloaded = it.isDownloaded == 1L,
                downloadSize = it.downloadSize,
                imageUrl = it.imageUrl,
                bio = it.bio,
                country = it.country,
                hasWordTiming = it.hasWordTiming == 1L
            )
        }
        if (cached.isNotEmpty()) {
            emit(cached)
        }

        try {
            // Fetch from API
            val remoteReciters = api.getAllReciters()
            val knownSyncIds = setOf("1", "6", "7", "10", "12") // Common QF v4 IDs with timings
            
            // Update cache
            database.audioQueries.transaction {
                remoteReciters.forEach { dto ->
                    val hasTiming = dto.hasWordTiming || knownSyncIds.contains(dto.id.toString())
                    database.audioQueries.insertReciter(
                        id = dto.id.toString(),
                        name = dto.name,
                        nameArabic = dto.nameArabic ?: dto.name,
                        style = dto.style,
                        bitrate = dto.bitrate.toLong(),
                        format = "MP3",
                        isDownloaded = 0,
                        downloadSize = 0,
                        imageUrl = null,
                        bio = null,
                        country = dto.languageCode,
                        hasWordTiming = if (hasTiming) 1L else 0L
                    )
                }
            }
 
          // Emit fresh data
            val fresh = database.audioQueries.selectAllReciters().executeAsList().map { 
                 Reciter(
                    id = it.id,
                    name = it.name,
                    nameArabic = it.nameArabic,
                    style = try { RecitationStyle.valueOf(it.style) } catch(e:Exception) { RecitationStyle.MURATTAL },
                    bitrate = it.bitrate.toInt(),
                    format = try { AudioFormat.valueOf(it.format) } catch(e:Exception) { AudioFormat.MP3 },
                    isDownloaded = it.isDownloaded == 1L,
                    downloadSize = it.downloadSize,
                    imageUrl = it.imageUrl,
                    bio = it.bio,
                    country = it.country,
                    hasWordTiming = it.hasWordTiming == 1L
                )
            }
            emit(fresh)

        } catch (e: Exception) {
            if (cached.isEmpty()) emit(emptyList()) 
        }
    }

    override suspend fun getReciterById(id: String): Flow<Reciter?> = flow {
        val entity = database.audioQueries.selectReciterById(id).executeAsOneOrNull()
        if (entity != null) {
            emit(
                Reciter(
                    id = entity.id,
                    name = entity.name,
                    nameArabic = entity.nameArabic,
                    style = try { RecitationStyle.valueOf(entity.style) } catch(e:Exception) { RecitationStyle.MURATTAL },
                    bitrate = entity.bitrate.toInt(),
                    format = try { AudioFormat.valueOf(entity.format) } catch(e:Exception) { AudioFormat.MP3 },
                    isDownloaded = entity.isDownloaded == 1L,
                    downloadSize = entity.downloadSize,
                    imageUrl = entity.imageUrl,
                    bio = entity.bio,
                    country = entity.country,
                    hasWordTiming = entity.hasWordTiming == 1L
                )
            )
        } else {
            emit(null)
        }
    }

    override suspend fun getRecitersByLanguage(languageCode: String): Flow<List<Reciter>> = flow {
        val reciters = database.audioQueries.selectRecitersByLanguage(languageCode).executeAsList().map { 
             Reciter(
                id = it.id,
                name = it.name,
                nameArabic = it.nameArabic,
                style = try { RecitationStyle.valueOf(it.style) } catch(e:Exception) { RecitationStyle.MURATTAL },
                bitrate = it.bitrate.toInt(),
                format = try { AudioFormat.valueOf(it.format) } catch(e:Exception) { AudioFormat.MP3 },
                isDownloaded = it.isDownloaded == 1L,
                downloadSize = it.downloadSize,
                imageUrl = it.imageUrl,
                bio = it.bio,
                country = it.country,
                hasWordTiming = it.hasWordTiming == 1L
            )
        }
        emit(reciters)
    }

    override suspend fun getAudioFile(reciterId: String, surahNumber: Int, ayahNumber: Int?): Flow<AudioFile?> = flow {
        // Check local DB
        val cached = database.audioQueries.selectAudioFile(reciterId, surahNumber.toLong(), ayahNumber?.toLong()).executeAsOneOrNull()
        
        if (cached != null) {
                val wordTimings = database.audioQueries.selectWordTimings(cached.id).executeAsList().map {
                    WordTiming(
                        verseNumber = it.verseNumber.toInt(),
                        wordPosition = it.wordPosition.toInt(),
                        startTime = it.startTime,
                        endTime = it.endTime,
                        duration = it.duration
                    )
                }
                emit(
                    AudioFile(
                        id = cached.id,
                        reciterId = cached.reciterId,
                        surahNumber = cached.surahNumber.toInt(),
                        ayahNumber = cached.ayahNumber?.toInt(),
                        url = cached.url,
                        localPath = cached.localPath,
                        duration = cached.duration,
                        fileSize = cached.fileSize,
                        isDownloaded = cached.isDownloaded == 1L,
                        downloadProgress = cached.downloadProgress.toInt(),
                        wordTimings = wordTimings
                    )
                )
        } else {
            try {
                // Fetch from API
                val apiReciterId = reciterId.toIntOrNull() ?: 0
                val remoteAudio = api.getAudioFile(apiReciterId, surahNumber, ayahNumber)
                
                // Cache it
                val entityId = "${reciterId}_${surahNumber}_${ayahNumber ?: "full"}"
                database.audioQueries.insertAudio(
                    id = entityId,
                    reciterId = reciterId,
                    surahNumber = surahNumber.toLong(),
                    ayahNumber = ayahNumber?.toLong(),
                    url = remoteAudio.url,
                    localPath = null,
                    duration = 0,
                    fileSize = remoteAudio.fileSize,
                    isDownloaded = 0,
                    downloadProgress = 0
                )

                // Insert Word Timings if available
                println("AlQuranPlusAI: Found ${remoteAudio.verseTimings.size} verse timings for remote audio")
                
                if (remoteAudio.verseTimings.any { it.segments.isNotEmpty() }) {
                    println("AlQuranPlusAI: Reciter $reciterId supports Word Timing! Updating database...")
                    database.audioQueries.updateReciterSyncStatus(1L, reciterId)
                }

                remoteAudio.verseTimings.forEach { vt ->
                    // Parse verseKey (format "surah:ayah") to get ayahNumber
                    val ayahNum = vt.verseKey.split(":").lastOrNull()?.toIntOrNull() ?: 0
                    
                    if (vt.segments.isNotEmpty()) {
                        println("AlQuranPlusAI: Syncing ${vt.segments.size} segments for verse ${vt.verseKey}")
                    }
                    
                    vt.segments.forEach { segment ->
                        // segment format: [word_index, start_time, end_time]
                        if (segment.size >= 3) {
                            database.audioQueries.insertWordTiming(
                                audioId = entityId,
                                verseNumber = ayahNum.toLong(),
                                wordPosition = segment[0].toLong(),
                                startTime = segment[1].toLong(),
                                endTime = segment[2].toLong(),
                                duration = (segment[2] - segment[1]).toLong()
                            )
                        }
                    }
                }
                
                // Fetch and emit
                 val fresh = database.audioQueries.selectAudioFile(reciterId, surahNumber.toLong(), ayahNumber?.toLong()).executeAsOneOrNull()
                 emit(fresh?.let {
                     val wordTimings = database.audioQueries.selectWordTimings(it.id).executeAsList().map {
                        WordTiming(
                            verseNumber = it.verseNumber.toInt(),
                            wordPosition = it.wordPosition.toInt(),
                            startTime = it.startTime,
                            endTime = it.endTime,
                            duration = it.duration
                        )
                    }
                     AudioFile(
                        id = it.id,
                        reciterId = it.reciterId,
                        surahNumber = it.surahNumber.toInt(),
                        ayahNumber = it.ayahNumber?.toInt(),
                        url = it.url,
                        localPath = it.localPath,
                        duration = it.duration,
                        fileSize = it.fileSize,
                        isDownloaded = it.isDownloaded == 1L,
                        downloadProgress = it.downloadProgress.toInt(),
                        wordTimings = wordTimings
                    )
                 })

            } catch (e: Exception) {
                println("AlQuranPlusAI: Error fetching audio file for reciter $reciterId, surah $surahNumber: ${e.message}")
                emit(null)
            }
        }
    }

    override suspend fun downloadAudio(reciterId: String, surahNumber: Int, ayahNumber: Int?): Flow<DownloadProgress> = flow {
        emit(DownloadProgress(0, 0, 0f, DownloadStatus.DOWNLOADING))
    }

    override suspend fun deleteAudio(reciterId: String, surahNumber: Int, ayahNumber: Int?) {
        database.audioQueries.deleteAudio(reciterId, surahNumber.toLong())
    }

    override suspend fun getDownloadedAudio(reciterId: String): Flow<List<AudioFile>> = flow {
        val files = database.audioQueries.selectDownloadedAudioFiles(reciterId).executeAsList().map {
            AudioFile(
                id = it.id,
                reciterId = it.reciterId,
                surahNumber = it.surahNumber.toInt(),
                ayahNumber = it.ayahNumber?.toInt(),
                url = it.url,
                localPath = it.localPath,
                duration = it.duration,
                fileSize = it.fileSize,
                isDownloaded = it.isDownloaded == 1L,
                downloadProgress = it.downloadProgress.toInt()
            )
        }
        emit(files)
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        emit(emptyList())
    }
    
    // Unimplemented methods stubbed
    override suspend fun getPlaylistById(id: Long) = flow { emit(null) }
    override suspend fun createPlaylist(name: String, description: String?) = flow { emit(0L) }
    override suspend fun updatePlaylist(id: Long, name: String, description: String?) {}
    override suspend fun deletePlaylist(id: Long) {}
    override suspend fun addToPlaylist(playlistId: Long, surahNumber: Int, ayahNumber: Int?) {}
    override suspend fun removeFromPlaylist(playlistId: Long, itemId: Long) {}
    override suspend fun getPlaylistItems(playlistId: Long) = flow { emit(emptyList<PlaylistItem>()) }
}
