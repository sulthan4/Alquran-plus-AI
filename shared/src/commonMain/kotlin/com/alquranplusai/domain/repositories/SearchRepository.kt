package com.alquranplusai.domain.repositories

import com.alquranplusai.domain.models.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository for search operations
 */
interface SearchRepository {
    // Search operations
    suspend fun searchText(query: String, options: SearchOptions): Flow<List<SearchResult>>
    suspend fun searchByRoot(root: String): Flow<List<SearchResult>>
    suspend fun searchByTopic(topic: String): Flow<List<SearchResult>>
    suspend fun semanticSearch(query: String, limit: Int): Flow<List<SearchResult>>
    suspend fun search(query: SearchQuery): Flow<List<SearchResult>>
    suspend fun voiceSearch(audioData: ByteArray): Flow<VoiceSearchResult?>
    
    // Search suggestions
    suspend fun getSearchSuggestions(query: String): Flow<List<String>>
    
    // Search history
    suspend fun getSearchHistory(): Flow<List<SearchHistory>>
    suspend fun saveSearchQuery(query: String)
    suspend fun clearSearchHistory()
    
    // Saved searches
    suspend fun getSavedSearches(): Flow<List<SavedSearch>>
    suspend fun saveSearch(name: String, query: String, filters: SearchOptions)
    suspend fun deleteSavedSearch(id: Long)

    // ViewModel compatibility
    suspend fun getRecentSearches(): Flow<List<String>>
    suspend fun searchInQuran(query: String): Flow<List<SearchResult>>
    suspend fun saveRecentSearch(query: String)
    suspend fun searchWithFilters(query: String, surahs: List<Int>, translations: List<String>): Flow<List<SearchResult>>
    suspend fun deleteRecentSearch(query: String)
    suspend fun clearRecentSearches()
}
