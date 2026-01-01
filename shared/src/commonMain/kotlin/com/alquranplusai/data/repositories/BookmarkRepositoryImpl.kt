package com.alquranplusai.data.repositories

import com.alquranplusai.data.database.AlQuranDatabaseWrapper
import com.alquranplusai.domain.models.*
import com.alquranplusai.domain.repositories.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock

class BookmarkRepositoryImpl(private val database: AlQuranDatabaseWrapper) : BookmarkRepository {
    // Bookmark operations
    override suspend fun getAllBookmarks(): Flow<List<Bookmark>> = flow {
        val bookmarks =
                database.bookmarkQueries.selectAllBookmarks().executeAsList().map { entity ->
                    Bookmark(
                            id = entity.id,
                            surahNumber = entity.surahNumber.toInt(),
                            ayahNumber = entity.ayahNumber.toInt(),
                            note = entity.note,
                            folderId = entity.folderId,
                            createdAt = entity.createdAt,
                            updatedAt = entity.updatedAt
                    )
                }
        emit(bookmarks)
    }

    override suspend fun getBookmarkById(id: String): Flow<Bookmark?> = flow {
        val entity = database.bookmarkQueries.selectBookmarkById(id).executeAsOneOrNull()
        emit(
                entity?.let {
                    Bookmark(
                            id = it.id,
                            surahNumber = it.surahNumber.toInt(),
                            ayahNumber = it.ayahNumber.toInt(),
                            note = it.note,
                            folderId = it.folderId,
                            createdAt = it.createdAt,
                            updatedAt = it.updatedAt
                    )
                }
        )
    }

    override suspend fun getBookmarksByFolder(folderId: String?): Flow<List<Bookmark>> = flow {
        val bookmarks =
                database.bookmarkQueries
                        .selectBookmarksByFolder(folderId)
                        .executeAsList()
                        .map { entity ->
                            Bookmark(
                                    id = entity.id,
                                    surahNumber = entity.surahNumber.toInt(),
                                    ayahNumber = entity.ayahNumber.toInt(),
                                    note = entity.note,
                                    folderId = entity.folderId,
                                    createdAt = entity.createdAt,
                                    updatedAt = entity.updatedAt
                            )
                        }
        emit(bookmarks)
    }

    override suspend fun getBookmarksByTag(tagId: String): Flow<List<Bookmark>> = flow {
        emit(emptyList()) // Stub
    }

    override suspend fun searchBookmarks(query: String): Flow<List<Bookmark>> = flow {
        emit(emptyList()) // Stub for now
    }

    override suspend fun createBookmark(
            surahNumber: Int,
            ayahNumber: Int,
            note: String?,
            folderId: String?
    ): Flow<String> = flow {
        val now = Clock.System.now().toEpochMilliseconds()
        val id = now.toString()
        database.bookmarkQueries.insertBookmark(
                id = id,
                surahNumber = surahNumber.toLong(),
                ayahNumber = ayahNumber.toLong(),
                folderId = folderId,
                title = null,
                note = note,
                category = "GENERAL",
                priority = "NORMAL",
                color = null,
                createdAt = now,
                updatedAt = now,
                lastAccessedAt = null,
                accessCount = 0,
                hasReminder = 0
        )
        emit(id)
    }

    override suspend fun updateBookmark(id: String, note: String?, folderId: String?) {
        val now = Clock.System.now().toEpochMilliseconds()
        database.bookmarkQueries.updateBookmark(
                id = id,
                title = null,
                note = note,
                folderId = folderId,
                category = "GENERAL",
                priority = "NORMAL",
                color = null,
                updatedAt = now
        )
    }

    override suspend fun deleteBookmark(id: String) {
        database.bookmarkQueries.deleteBookmark(id)
    }

    override suspend fun addTagToBookmark(bookmarkId: String, tagId: String) {
        // Link tag to bookmark in database
    }

    override suspend fun removeTagFromBookmark(bookmarkId: String, tagId: String) {
        // Unlink tag from bookmark in database
    }

    override suspend fun isBookmarked(surahNumber: Int, ayahNumber: Int): Flow<Boolean> = flow {
        val bookmark =
                database.bookmarkQueries
                        .selectBookmarkForAyah(surahNumber.toLong(), ayahNumber.toLong())
                        .executeAsOneOrNull()
        emit(bookmark != null)
    }
    
    // Folder operations
    override suspend fun getAllFolders(): Flow<List<BookmarkFolder>> = flow {
        emit(emptyList()) // Stub
    }

    override suspend fun createFolder(name: String, color: String?, parentId: String?): Flow<String> =
            flow {
                emit("folder_${Clock.System.now().toEpochMilliseconds()}")
            }

    override suspend fun updateFolder(id: String, name: String, color: String?) {}

    override suspend fun deleteFolder(id: String) {}
    
    // Tag operations
    override suspend fun getAllTags(): Flow<List<BookmarkTag>> = flow { emit(emptyList()) }

    override suspend fun createTag(name: String, color: String): Flow<String> = flow {
        emit("tag_${Clock.System.now().toEpochMilliseconds()}")
    }

    override suspend fun deleteTag(id: String) {}
    
    // Note operations
    override suspend fun getAllNotes(): Flow<List<Note>> = flow { emit(emptyList()) }

    override suspend fun getNotesByAyah(surahNumber: Int, ayahNumber: Int): Flow<List<Note>> =
            flow {
                emit(emptyList())
            }

    override suspend fun createNote(
            surahNumber: Int,
            ayahNumber: Int,
            content: String,
            isPrivate: Boolean
    ): Flow<String> = flow {
        emit("note_${Clock.System.now().toEpochMilliseconds()}")
    }

    override suspend fun updateNote(id: String, content: String, isPrivate: Boolean) {}

    override suspend fun deleteNote(id: String) {}
}
