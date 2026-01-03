package com.alquranplusai.data.repositories

import com.alquranplusai.data.database.TestAlQuranDatabaseWrapper
import com.alquranplusai.data.network.api.FakeAudioApiService
import com.alquranplusai.domain.models.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AudioRepositoryImplTest {

    private val api = FakeAudioApiService()
    private lateinit var database: TestAlQuranDatabaseWrapper
    private lateinit var repository: AudioRepositoryImpl

    @BeforeTest
    fun setup() {
        database = TestAlQuranDatabaseWrapper()
        repository = AudioRepositoryImpl(database, api)
        
        // Insert a dummy reciter for FK constraints
        database.audioQueries.insertReciter(
            id = "1",
            name = "Test Reciter",
            nameArabic = "Test",
            style = "Murattal",
            bitrate = 128,
            format = "MP3",
            isDownloaded = 0,
            downloadSize = 0,
            imageUrl = null,
            bio = null,
            country = "sa",
            hasWordTiming = 0
        )
    }

    @AfterTest
    fun tearDown() {
        database.close()
    }

    @Test
    fun `createPlaylist should create a new playlist`() = runTest {
        val id = repository.createPlaylist("My Playlist", "Description").first()
        assertNotNull(id)
        
        val playlist = repository.getPlaylistById(id).first()
        assertNotNull(playlist)
        assertEquals("My Playlist", playlist.name)
        assertEquals("Description", playlist.description)
        assertEquals(0, playlist.itemCount)
    }

    @Test
    fun `addToPlaylist should add items and update counts`() = runTest {
        val id = repository.createPlaylist("My Playlist", null).first()
        
        repository.addToPlaylist(id, "1", 1, 1)
        repository.addToPlaylist(id, "1", 1, 2)
        
        val items = repository.getPlaylistItems(id).first()
        assertEquals(2, items.size)
        assertEquals(1, items[0].surahNumber)
        assertEquals(1, items[0].ayahStart)
        
        // Count update requires manual trigger or observed flow update?
        // In implementation, we called updatePlaylistCount.
        // Let's verify playlist item count
        val playlist = repository.getPlaylistById(id).first()
        assertEquals(2, playlist?.itemCount)
    }

    @Test
    fun `removeFromPlaylist should remove item`() = runTest {
        val id = repository.createPlaylist("My Playlist", null).first()
        repository.addToPlaylist(id, "1", 1, 1)
        
        val items = repository.getPlaylistItems(id).first()
        val itemId = items[0].id
        
        repository.removeFromPlaylist(id, itemId)
        
        val newItems = repository.getPlaylistItems(id).first()
        assertTrue(newItems.isEmpty())
        
        val playlist = repository.getPlaylistById(id).first()
        assertEquals(0, playlist?.itemCount)
    }
    
    @Test
    fun `updatePlaylist should update metadata`() = runTest {
        val id = repository.createPlaylist("Old Name", null).first()
        
        repository.updatePlaylist(id, "New Name", "New Desc")
        
        val playlist = repository.getPlaylistById(id).first()
        assertEquals("New Name", playlist?.name)
        assertEquals("New Desc", playlist?.description)
    }
    
    @Test
    fun `deletePlaylist should remove playlist`() = runTest {
        val id = repository.createPlaylist("To Delete", null).first()
        
        repository.deletePlaylist(id)
        
        val playlist = repository.getPlaylistById(id).first()
        assertTrue(playlist == null)
    }
}
