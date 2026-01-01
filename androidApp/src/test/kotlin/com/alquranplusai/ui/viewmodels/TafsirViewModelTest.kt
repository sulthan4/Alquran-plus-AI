package com.alquranplusai.ui.viewmodels

import com.alquranplusai.android.ui.viewmodels.TafsirViewModel
import com.alquranplusai.domain.repositories.TafsirRepository
import com.alquranplusai.domain.models.Tafsir
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TafsirViewModelTest {
    
    private lateinit var repository: TafsirRepository
    private lateinit var viewModel: TafsirViewModel
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = TafsirViewModel(repository)
        
        coEvery { repository.getPreferredTafsirs() } returns flowOf(emptyList())
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `loadAllTafsirs updates state with tafsirs from repository`() = runTest {
        // Given
        val tafsir = Tafsir(
            id = "1",
            name = "Test Tafsir",
            nameArabic = "تفسير",
            author = "Author",
            authorArabic = "مؤلف",
            language = "English",
            languageCode = "en",
            description = "Test description",
            source = "manual_test",
            isDownloaded = false
        )
        coEvery { repository.getAllTafsirs() } returns flowOf(listOf(tafsir))
        
        // When
        viewModel.loadAllTafsirs()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val tafsirs = viewModel.allTafsirs.first()
        assertEquals(1, tafsirs.size)
        assertEquals("Test Tafsir", tafsirs[0].name)
    }
    
    @Test
    fun `togglePreferredTafsir updates preferences`() = runTest {
        // Given
        val tafsirId = "1"
        
        // When
        viewModel.togglePreferredTafsir(tafsirId)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        // Expect list with just the added ID
        io.mockk.coVerify { repository.setPreferredTafsirs(listOf(tafsirId)) }
    }
    
    @Test
    fun `searchTafsir filters tafsirs by query calls repository`() = runTest {
        // Given
        val query = "query"
        
        // When
        viewModel.searchTafsir("id", query)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        io.mockk.coVerify { repository.searchTafsir("id", query) }
    }
}
