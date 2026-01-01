package com.alquranplusai.domain.repositories

import com.alquranplusai.domain.models.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository for bookmark and note operations
 */
interface BookmarkRepository {
    // Bookmark operations
    suspend fun getAllBookmarks(): Flow<List<Bookmark>>
    suspend fun getBookmarkById(id: String): Flow<Bookmark?>
    suspend fun getBookmarksByFolder(folderId: String?): Flow<List<Bookmark>>
    suspend fun getBookmarksByTag(tagId: String): Flow<List<Bookmark>>
    suspend fun searchBookmarks(query: String): Flow<List<Bookmark>>
    suspend fun createBookmark(surahNumber: Int, ayahNumber: Int, note: String?, folderId: String?): Flow<String>
    suspend fun updateBookmark(id: String, note: String?, folderId: String?)
    suspend fun deleteBookmark(id: String)
    suspend fun addTagToBookmark(bookmarkId: String, tagId: String)
    suspend fun removeTagFromBookmark(bookmarkId: String, tagId: String)
    suspend fun isBookmarked(surahNumber: Int, ayahNumber: Int): Flow<Boolean>
    
    // Folder operations
    suspend fun getAllFolders(): Flow<List<BookmarkFolder>>
    suspend fun createFolder(name: String, color: String?, parentId: String?): Flow<String>
    suspend fun updateFolder(id: String, name: String, color: String?)
    suspend fun deleteFolder(id: String)
    
    // Tag operations
    suspend fun getAllTags(): Flow<List<BookmarkTag>>
    suspend fun createTag(name: String, color: String): Flow<String>
    suspend fun deleteTag(id: String)
    
    // Note operations
    suspend fun getAllNotes(): Flow<List<Note>>
    suspend fun getNotesByAyah(surahNumber: Int, ayahNumber: Int): Flow<List<Note>>
    suspend fun createNote(surahNumber: Int, ayahNumber: Int, content: String, isPrivate: Boolean): Flow<String>
    suspend fun updateNote(id: String, content: String, isPrivate: Boolean)
    suspend fun deleteNote(id: String)
}
