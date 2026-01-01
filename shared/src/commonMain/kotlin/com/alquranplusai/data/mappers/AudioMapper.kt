package com.alquranplusai.data.mappers

import com.alquranplusai.data.network.dto.*
import com.alquranplusai.domain.models.*
import kotlinx.datetime.Clock

/** Mapper for Audio data */
class AudioMapper {

    fun mapReciterDtoToDomain(dto: ReciterDto): Reciter {
        return Reciter(
                id = dto.id.toString(),
                name = dto.name,
                nameArabic = dto.nameArabic ?: dto.name,
                style =
                        when (dto.style.uppercase()) {
                            "MURATTAL" -> RecitationStyle.MURATTAL
                            "MUJAWWAD" -> RecitationStyle.MUJAWWAD
                            "MUALLIM" -> RecitationStyle.MUALLIM
                            else -> RecitationStyle.MURATTAL
                        }
        )
    }

    fun mapAudioDtoToDomain(dto: AudioDto): AudioFile {
        return AudioFile(
                id = dto.id.toString(),
                reciterId = dto.reciterId.toString(),
                surahNumber = dto.surahNumber,
                ayahNumber = dto.ayahNumber,
                url = dto.url,
                duration = 0 // Not provided in DTO
        )
    }

    fun mapPlaylistDtoToDomain(dto: PlaylistDto): Playlist {
        val now = Clock.System.now().toEpochMilliseconds()
        return Playlist(
                id = dto.id.toString(),
                name = dto.name,
                description = dto.description,
                createdAt = dto.createdAt,
                updatedAt = dto.updatedAt
        )
    }
}
