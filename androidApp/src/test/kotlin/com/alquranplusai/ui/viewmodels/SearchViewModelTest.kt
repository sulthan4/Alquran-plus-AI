package com.alquranplusai.ui.viewmodels

import com.alquranplusai.android.ui.viewmodels.SearchViewModel
import com.alquranplusai.domain.repositories.SearchRepository
import com.alquranplusai.domain.models.SearchResult
import io.mockk.coEvery
import io.mockk.coVerify
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
class SearchViewModelTest {
    
    private lateinit var repository: SearchRepository
    private lateinit var viewModel: SearchViewModel
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = SearchViewModel(repository)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `search returns results`() = runTest {
        // Given
        val query = "Allah"
        val mockResults = listOf(mockk<SearchResult>(relaxed = true))
        coEvery { repository.searchInQuran(query) } returns flowOf(mockResults)
        
        // When
        viewModel.search(query)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val results = viewModel.searchResults.value
        assertTrue(results.isNotEmpty())
        assertEquals(mockResults, results)
        assertFalse(viewModel.isSearching.value)
    }
    
    @Test
    fun `search with empty query clears results`() = runTest {
        // When
        viewModel.search("")
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val results = viewModel.searchResults.value
        assertTrue(results.isEmpty())
    }
    
    @Test
    fun `search sets loading state`() = runTest {
        // Given
        val query = "test"
        coEvery { repository.searchInQuran(query) } returns flowOf(emptyList())
        
        // When
        viewModel.search(query)
        
        // Then
        assertTrue(viewModel.isSearching.value)
        
        testDispatcher.scheduler.advanceUntilIdle()
        assertFalse(viewModel.isSearching.value)
    }
}
