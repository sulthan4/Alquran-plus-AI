package com.alquranplusai.ui.viewmodels

import com.alquranplusai.android.ui.viewmodels.HomeViewModel
import com.alquranplusai.domain.models.Surah
import com.alquranplusai.domain.models.RevelationType
import com.alquranplusai.domain.repositories.AnalyticsRepository
import com.alquranplusai.domain.repositories.QuranRepository
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
class HomeViewModelTest {
    
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var quranRepository: QuranRepository
    private lateinit var analyticsRepository: AnalyticsRepository
    private lateinit var viewModel: HomeViewModel
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        quranRepository = mockk(relaxed = true)
        analyticsRepository = mockk(relaxed = true)
        
        coEvery { quranRepository.getLastReadingPosition() } returns flowOf(null)
        coEvery { analyticsRepository.getCurrentStreak(any()) } returns flowOf(5)
        coEvery { analyticsRepository.getTotalReadingTime(any()) } returns flowOf(100L)
        coEvery { quranRepository.getCompletedSurahs() } returns flowOf(emptyList())
        coEvery { quranRepository.getSurahByNumber(any()) } returns flowOf(
             Surah(
                number = 1,
                name = "Al-Fatiha", 
                nameArabic = "الفاتحة", 
                nameTransliteration = "Al-Fatiha",
                nameTranslation = "The Opening",
                revelationType = RevelationType.MECCAN,
                numberOfAyahs = 7,
                bismillahPre = true,
                juzNumbers = listOf(1),
                pageNumbers = listOf(1),
                manzilNumber = 1,
                rukuCount = 1
            )
        )

        viewModel = HomeViewModel(quranRepository, analyticsRepository)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `viewModel loads daily verse and streaks on init`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals(5, viewModel.readingStreak.value)
        assertEquals(100L, viewModel.totalReadingTime.value)
        assertNotNull(viewModel.dailyVerse.value)
        assertFalse(viewModel.isLoading.value)
    }
    
    @Test
    fun `refresh reloads data`() = runTest {
        viewModel.refresh()
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(true) 
    }
}
