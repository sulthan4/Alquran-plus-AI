package com.alquranplusai.ui.viewmodels

import com.alquranplusai.android.ui.viewmodels.ReadingViewModel
import com.alquranplusai.domain.models.Ayah
import com.alquranplusai.domain.repositories.BookmarkRepository
import com.alquranplusai.domain.repositories.QuranRepository
import com.alquranplusai.domain.repositories.TranslationRepository
import com.alquranplusai.data.preferences.PreferencesManager
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReadingViewModelTest {
    
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var quranRepository: QuranRepository
    private lateinit var translationRepository: TranslationRepository
    private lateinit var bookmarkRepository: BookmarkRepository
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var viewModel: ReadingViewModel
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        quranRepository = mockk(relaxed = true)
        translationRepository = mockk(relaxed = true)
        bookmarkRepository = mockk(relaxed = true)
        preferencesManager = mockk(relaxed = true)
        
        coEvery { bookmarkRepository.getAllBookmarks() } returns flowOf(emptyList())
        coEvery { preferencesManager.fontSize } returns flowOf(18)
        
        viewModel = ReadingViewModel(quranRepository, translationRepository, bookmarkRepository, preferencesManager)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `loadAyahs updates state`() = runTest {
        // Given
        val ayahs = listOf(
            Ayah(
                id = 1L,
                surahNumber = 1,
                ayahNumber = 1,
                text = "Text",
                textUthmani = "TextUthmani",
                textSimple = "TextSimple",
                juzNumber = 1,
                hizbNumber = 1,
                rukuNumber = 1,
                manzilNumber = 1,
                pageNumber = 1
            )
        )
        coEvery { quranRepository.getAyahsBySurah(1) } returns flowOf(ayahs)
        
        // When
        viewModel.loadAyahs(1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        assertEquals(1, viewModel.ayahs.value.size)
        assertFalse(viewModel.isLoading.value)
    }
    
    @Test
    fun `setFontSize updates preferences`() = runTest {
        viewModel.setFontSize(24)
        testDispatcher.scheduler.advanceUntilIdle()
        io.mockk.coVerify { preferencesManager.updateFontSize(24) }
    }
}
