package com.alquranplusai.data.network.api

import com.alquranplusai.data.network.dto.BookmarkDto
import com.alquranplusai.data.network.dto.FolderDto
import com.alquranplusai.data.network.dto.NoteDto

/**
 * API service for bookmarks
 */
interface BookmarkApiService {
    
    suspend fun getUserBookmarks(userId: Int): List<BookmarkDto>
    
    suspend fun getBookmark(id: Int): BookmarkDto
    
    suspend fun createBookmark(userId: Int, bookmark: BookmarkDto): BookmarkDto
    
    suspend fun updateBookmark(id: Int, bookmark: BookmarkDto): BookmarkDto
    
    suspend fun deleteBookmark(id: Int)
    
    suspend fun getUserFolders(userId: Int): List<FolderDto>
    
    suspend fun createFolder(userId: Int, folder: FolderDto): FolderDto
    
    suspend fun getNotes(bookmarkId: Int): List<NoteDto>
    
    suspend fun createNote(bookmarkId: Int, note: NoteDto): NoteDto
}
