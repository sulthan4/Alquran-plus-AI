package com.alquranplusai.domain.usecases

import com.alquranplusai.domain.models.Bookmark
import com.alquranplusai.domain.models.BookmarkFolder
import com.alquranplusai.domain.repositories.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class AddBookmarkUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(
        surahNumber: Int,
        ayahNumber: Int,
        note: String? = null,
        folderId: String? = null,
        tags: List<String> = emptyList()
    ): Result<String> {
        return try {
            // TODO: Create bookmark entity and add
            Result.success("0")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class RemoveBookmarkUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(bookmarkId: String): Result<Unit> {
        return try {
            bookmarkRepository.deleteBookmark(bookmarkId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class OrganizeBookmarksUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(
        bookmarkId: String,
        newFolderId: String?
    ): Result<Unit> {
        return try {
            // TODO: Move bookmark to folder
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class CreateFolderUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(
        name: String,
        color: String,
        parentId: String? = null
    ): Result<String> {
        return try {
            // TODO: Create folder
            Result.success("0")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class GetBookmarksWithFoldersUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(): Flow<Map<BookmarkFolder?, List<Bookmark>>> {
        return combine(
            bookmarkRepository.getAllBookmarks(),
            bookmarkRepository.getAllFolders()
        ) { bookmarks, folders ->
            // Group bookmarks by folder
            val grouped = mutableMapOf<BookmarkFolder?, MutableList<Bookmark>>()
            grouped[null] = mutableListOf() // Unfiled bookmarks
            
            folders.forEach { folder ->
                grouped[folder] = mutableListOf()
            }
            
            bookmarks.forEach { bookmark ->
                val folder = folders.find { it.id == bookmark.folderId }
                grouped[folder]?.add(bookmark)
            }
            
            grouped
        }
    }
}
