package com.alquranplusai.data.repositories

import com.alquranplusai.data.database.TestAlQuranDatabaseWrapper
import com.alquranplusai.domain.models.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BookmarkRepositoryImplTest {

    private lateinit var database: TestAlQuranDatabaseWrapper
    private lateinit var repository: BookmarkRepositoryImpl

    @BeforeTest
    fun setup() {
        database = TestAlQuranDatabaseWrapper()
        repository = BookmarkRepositoryImpl(database)
    }

    @AfterTest
    fun tearDown() {
        database.close()
    }

    @Test
    fun `createFolder should create folder`() = runTest {
        val id = repository.createFolder("My Folder", "#FF0000", null).first()
        
        val folders = repository.getAllFolders().first()
        assertEquals(1, folders.size)
        assertEquals("My Folder", folders[0].name)
    }

    @Test
    fun `createBookmark in folder should update folder count`() = runTest {
        val folderId = repository.createFolder("Folder", null, null).first()
        
        repository.createBookmark(1, 1, "Note", folderId).first()
        
        val folders = repository.getAllFolders().first()
        assertEquals(1, folders[0].bookmarkCount)
        
        val bookmarks = repository.getBookmarksByFolder(folderId).first()
        assertEquals(1, bookmarks.size)
    }

    @Test
    fun `createTag should create tag`() = runTest {
        val id = repository.createTag("Important", "#00FF00").first()
        
        val tags = repository.getAllTags().first()
        assertEquals(1, tags.size)
        assertEquals("Important", tags[0].name)
    }

    @Test
    fun `addTagToBookmark should link tag and update usage`() = runTest {
        val tagId = repository.createTag("Tag", "#000").first()
        val bookmarkId = repository.createBookmark(1, 1, null, null).first()
        
        repository.addTagToBookmark(bookmarkId, tagId)
        
        val tags = repository.getAllTags().first()
        assertEquals(1, tags[0].usageCount)
    }
}
