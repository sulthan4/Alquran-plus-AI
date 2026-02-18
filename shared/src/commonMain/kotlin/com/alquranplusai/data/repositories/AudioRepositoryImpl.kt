package com.alquranplusai.data.repositories

import com.alquranplusai.data.database.AlQuranDatabaseWrapper
import com.alquranplusai.domain.repositories.AudioRepository
import com.alquranplusai.data.network.api.AudioApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.alquranplusai.data.network.dto.ReciterDto
import com.alquranplusai.domain.models.*
import kotlinx.datetime.Clock

class AudioRepositoryImpl(
    private val database: AlQuranDatabaseWrapper,
    private val api: AudioApiService,
    private val quranComApi: com.alquranplusai.data.network.api.QuranComApiService
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
                if (remoteAudio.verseTimings.any { it.segments.isNotEmpty() }) {
                    database.audioQueries.updateReciterSyncStatus(1L, reciterId)
                }

                remoteAudio.verseTimings.forEach { vt ->
                    val ayahNum = vt.verseKey.split(":").lastOrNull()?.toIntOrNull() ?: 0
                    
                    vt.segments.forEach { segment ->
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
                     val wordTimings = database.audioQueries.selectWordTimings(it.id).executeAsList().map { wt ->
                        WordTiming(
                            verseNumber = wt.verseNumber.toInt(),
                            wordPosition = wt.wordPosition.toInt(),
                            startTime = wt.startTime,
                            endTime = wt.endTime,
                            duration = wt.duration
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

    // Playlist Implementation
    override suspend fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = database.playlistQueries.selectAllPlaylists().executeAsList().map { entity ->
            Playlist(
                id = entity.id,
                name = entity.name,
                description = entity.description,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                itemCount = entity.itemCount.toInt(),
                totalDuration = entity.totalDuration,
                coverImageUrl = entity.coverImageUrl,
                isDefault = entity.isDefault == 1L
            )
        }
        emit(playlists)
    }

    override suspend fun getPlaylistById(id: String): Flow<Playlist?> = flow {
        val entity = database.playlistQueries.selectPlaylistById(id).executeAsOneOrNull()
        emit(entity?.let {
            Playlist(
                id = it.id,
                name = it.name,
                description = it.description,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                itemCount = it.itemCount.toInt(),
                totalDuration = it.totalDuration,
                coverImageUrl = it.coverImageUrl,
                isDefault = it.isDefault == 1L
            )
        })
    }

    override suspend fun createPlaylist(name: String, description: String?): Flow<String> = flow {
        val now = Clock.System.now().toEpochMilliseconds()
        val id = "playlist_$now" // Simple ID generation
        
        database.playlistQueries.insertPlaylist(
            id = id,
            name = name,
            description = description,
            createdAt = now,
            updatedAt = now,
            itemCount = 0,
            totalDuration = 0,
            coverImageUrl = null,
            isDefault = 0
        )
        emit(id)
    }

    override suspend fun updatePlaylist(id: String, name: String, description: String?) {
        val now = Clock.System.now().toEpochMilliseconds()
        // We preserve the existing cover image for now
        val existing = database.playlistQueries.selectPlaylistById(id).executeAsOneOrNull()
        if (existing != null) {
            database.playlistQueries.updatePlaylist(
                name = name,
                description = description,
                updatedAt = now,
                coverImageUrl = existing.coverImageUrl,
                id = id
            )
        }
    }

    override suspend fun deletePlaylist(id: String) {
        database.playlistQueries.deletePlaylist(id)
    }

    override suspend fun addToPlaylist(playlistId: String, reciterId: String, surahNumber: Int, ayahNumber: Int?) {
        val now = Clock.System.now().toEpochMilliseconds()
        val itemId = "item_${now}_${surahNumber}_${ayahNumber ?: 0}"
        
        // Get current max position
        val currentCount = database.playlistItemQueries.countPlaylistItems(playlistId).executeAsOne()
        
        database.playlistItemQueries.insertPlaylistItem(
            id = itemId,
            playlistId = playlistId,
            reciterId = reciterId,
            surahNumber = surahNumber.toLong(),
            ayahStart = ayahNumber?.toLong(),
            ayahEnd = ayahNumber?.toLong(), // For single ayah usage
            position = currentCount,
            addedAt = now
        )
        
        // Update playlist count
        updatePlaylistCount(playlistId)
    }

    override suspend fun removeFromPlaylist(playlistId: String, itemId: String) {
        database.playlistItemQueries.deletePlaylistItem(itemId)
        updatePlaylistCount(playlistId)
    }

    override suspend fun getPlaylistItems(playlistId: String): Flow<List<PlaylistItem>> = flow {
        val items = database.playlistItemQueries.selectPlaylistItems(playlistId).executeAsList().map { entity ->
            PlaylistItem(
                id = entity.id,
                playlistId = entity.playlistId,
                reciterId = entity.reciterId,
                surahNumber = entity.surahNumber.toInt(),
                ayahStart = entity.ayahStart?.toInt(),
                ayahEnd = entity.ayahEnd?.toInt(),
                position = entity.position.toInt(),
                addedAt = entity.addedAt
            )
        }
        emit(items)
    }
    
    private fun updatePlaylistCount(playlistId: String) {
        val count = database.playlistItemQueries.countPlaylistItems(playlistId).executeAsOne()
        // Duration calculation would require summing up tracks, for now setting to 0 or keeping existing logic if we had it.
        // Simplified: just update count and timestamp
        val now = Clock.System.now().toEpochMilliseconds()
        database.playlistQueries.updatePlaylistCounts(
            itemCount = count,
            totalDuration = 0, // TODO: Calculate actual duration
            updatedAt = now,
            id = playlistId
        )
    }
}
