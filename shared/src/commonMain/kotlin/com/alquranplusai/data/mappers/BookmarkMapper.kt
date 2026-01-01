package com.alquranplusai.data.mappers

import com.alquranplusai.data.network.dto.*
import com.alquranplusai.domain.models.*
import kotlinx.datetime.Clock

/** Mapper for Bookmark data */
class BookmarkMapper {

    fun mapBookmarkDtoToDomain(dto: BookmarkDto): Bookmark {
        val now = Clock.System.now().toEpochMilliseconds()
        return Bookmark(
                id = dto.id.toString(),
                surahNumber = dto.surahNumber,
                ayahNumber = dto.ayahNumber,
                note = dto.note,
                createdAt = dto.createdAt,
                updatedAt = now
        )
    }

    fun mapFolderDtoToDomain(dto: FolderDto): BookmarkFolder {
        val now = Clock.System.now().toEpochMilliseconds()
        return BookmarkFolder(
                id = dto.id.toString(),
                name = dto.name,
                color = dto.color,
                createdAt = now,
                updatedAt = now
        )
    }

    fun mapNoteDtoToDomain(dto: NoteDto, surahNumber: Int, ayahNumber: Int): Note {
        val now = Clock.System.now().toEpochMilliseconds()
        return Note(
                id = dto.id.toString(),
                surahNumber = surahNumber,
                ayahNumber = ayahNumber,
                content = dto.content,
                createdAt = dto.createdAt,
                updatedAt = now
        )
    }
}
