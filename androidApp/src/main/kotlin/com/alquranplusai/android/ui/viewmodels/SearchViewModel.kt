package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.SearchResult
import com.alquranplusai.domain.repositories.SearchRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*
import com.alquranplusai.domain.models.SearchType

class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val quranRepository: com.alquranplusai.domain.repositories.QuranRepository
) : ViewModel() {
    
    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()
    
    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()
    
    private val _selectedSurahs = MutableStateFlow<List<Int>>(emptyList())
    val selectedSurahs: StateFlow<List<Int>> = _selectedSurahs.asStateFlow()
    
    private val _selectedTranslations = MutableStateFlow<List<String>>(emptyList())
    val selectedTranslations: StateFlow<List<String>> = _selectedTranslations.asStateFlow()
    
    private val _searchType = MutableStateFlow(SearchType.TEXT)
    val searchType: StateFlow<SearchType> = _searchType.asStateFlow()
    
    // Available Filters
    private val _availableSurahs = MutableStateFlow<List<com.alquranplusai.domain.models.Surah>>(emptyList())
    val availableSurahs: StateFlow<List<com.alquranplusai.domain.models.Surah>> = _availableSurahs.asStateFlow()
    
    private val _availableTranslations = MutableStateFlow<List<String>>(listOf("Sahih International", "Urdu - Jalandhry")) // Mock for now
    val availableTranslations: StateFlow<List<String>> = _availableTranslations.asStateFlow()

    private var searchJob: Job? = null
    
    init {
        loadRecentSearches()
        loadAvailableSurahs()
    }
    
    private fun loadRecentSearches() {
        viewModelScope.launch {
            try {
                searchRepository.getRecentSearches().collect { searches ->
                    _recentSearches.value = searches
                }
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
    
    private fun loadAvailableSurahs() {
        viewModelScope.launch {
            try {
                quranRepository.getAllSurahs().collect { surahs ->
                    _availableSurahs.value = surahs
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun setSearchType(type: SearchType) {
        _searchType.value = type
        if (_searchQuery.value.isNotBlank()) {
            performSearch(_searchQuery.value)
        }
    }

    fun onQueryChanged(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // Debounce
            performSearch(query)
        }
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        
        _isSearching.value = true
        
        viewModelScope.launch {
            try {
                // Check if filters are applied
                if (_selectedSurahs.value.isNotEmpty() || _selectedTranslations.value.isNotEmpty()) {
                    searchRepository.searchWithFilters(query, _selectedSurahs.value, _selectedTranslations.value).collect { results ->
                        _searchResults.value = results
                    }
                } else {
                    val flow = when (_searchType.value) {
                        SearchType.TEXT -> searchRepository.searchInQuran(query)
                        SearchType.SEMANTIC -> searchRepository.semanticSearch(query, limit = 20)
                        SearchType.ROOT -> searchRepository.searchByRoot(query)
                        SearchType.TOPIC -> searchRepository.searchByTopic(query)
                        else -> searchRepository.searchInQuran(query)
                    }

                    flow.collect { results ->
                        _searchResults.value = results
                    }
                }
                searchRepository.saveRecentSearch(query)
            } catch (e: Exception) {
                // TODO: Handle error
            } finally {
                _isSearching.value = false
            }
        }
    }

    fun search(query: String) {
        _searchQuery.value = query
        performSearch(query)
    }
    
    fun applyFilters(surahs: List<Int>, translations: List<String>) {
        _selectedSurahs.value = surahs
        _selectedTranslations.value = translations
        if (_searchQuery.value.isNotBlank()) {
            performSearch(_searchQuery.value)
        }
    }
    
    // searchWithFilters (Keeping API but routing through performSearch logic if used explicitly)
    fun searchWithFilters(
        query: String,
        surahs: List<Int>,
        translations: List<String>
    ) {
         applyFilters(surahs, translations)
         search(query)
    }
    
    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }
    
    fun deleteRecentSearch(query: String) {
        viewModelScope.launch {
            try {
                searchRepository.deleteRecentSearch(query)
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
    
    fun clearRecentSearches() {
        viewModelScope.launch {
            try {
                searchRepository.clearRecentSearches()
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
}
