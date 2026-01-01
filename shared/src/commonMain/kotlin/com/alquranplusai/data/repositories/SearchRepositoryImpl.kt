package com.alquranplusai.data.repositories

import com.alquranplusai.data.database.AlQuranDatabaseWrapper
import com.alquranplusai.data.network.api.SearchApiService
import com.alquranplusai.data.ai.SemanticSearchEngine
import com.alquranplusai.domain.models.*
import com.alquranplusai.domain.repositories.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.alquranplusai.database.SearchHistory as SearchHistoryDb
import com.alquranplusai.database.SavedSearch as SavedSearchDb

/**
 * Hybrid Search Repository combining:
 * 1. Quran Foundation API (fast, accurate text search)
 * 2. AI Semantic Search (concept-based, natural language)
 * 3. Local Database (offline fallback)
 */
class SearchRepositoryImpl(
    private val database: AlQuranDatabaseWrapper,
    private val api: SearchApiService,
    private val semanticSearch: SemanticSearchEngine? = null // Optional AI enhancement
) : SearchRepository {

    /**
     * Hybrid text search strategy:
     * 1. Try API first (best results)
     * 2. If semantic search available, merge results
     * 3. Fallback to local database
     */
    override suspend fun searchText(
        query: String,
        options: SearchOptions
    ): Flow<List<SearchResult>> = flow {
        val mergedResults = mutableMapOf<String, SearchResult>()
        
        // STRATEGY CHANGE: Prioritize local database (guaranteed to work)
        // Then enhance with API and AI if available
        
        // 1. Local Database Search (Primary - Always Works)
        try {
            database.ayahQueries
                .searchInText(query, query, query)
                .executeAsList()
                .forEach { ayah ->
                    val key = "${ayah.surahNumber}:${ayah.ayahNumber}"
                    mergedResults[key] = SearchResult(
                        id = "ayah_${ayah.id}",
                        surahNumber = ayah.surahNumber.toInt(),
                        ayahNumber = ayah.ayahNumber.toInt(),
                        text = ayah.text,
                        relevanceScore = 0.8f,
                        matchType = MatchType.PARTIAL
                    )
                }
        } catch (e: Exception) {
            println("Database search error: ${e.message}")
            e.printStackTrace()
        }
        
        // 2. API Search (Enhancement - May Fail)
        try {
            val apiResults = api.searchText(query, "ar")
            apiResults.forEach {
                val verseKeyParts = it.verseKey.split(":")
                val s = verseKeyParts[0].toInt()
                val a = verseKeyParts[1].toInt()
                val key = "$s:$a"
                
                // Override or add API result (better quality)
                mergedResults[key] = SearchResult(
                    id = it.verseId?.toString() ?: it.verseKey,
                    surahNumber = s,
                    ayahNumber = a,
                    text = it.text,
                    highlightedText = it.highlighted,
                    relevanceScore = 1.0f,
                    matchType = MatchType.EXACT
                )
            }
        } catch (e: Exception) {
            println("API search error (non-critical): ${e.message}")
            // Continue with database results
        }

        // 3. Semantic Search (AI Enhancement - Optional)
        if (semanticSearch != null) {
            try {
                val semanticResults = semanticSearch.searchSemantic(query)
                semanticResults.forEach { semanticResult ->
                    val key = "${semanticResult.surahNumber}:${semanticResult.ayahNumber}"
                    
                    // Only add if not already present
                    if (!mergedResults.containsKey(key)) {
                        val ayah = database.ayahQueries
                            .selectByNumber(semanticResult.surahNumber.toLong(), semanticResult.ayahNumber.toLong())
                            .executeAsOneOrNull()
                        
                        mergedResults[key] = semanticResult.copy(
                            text = ayah?.text ?: "",
                            matchType = MatchType.SEMANTIC
                        )
                    }
                }
            } catch (e: Exception) {
                println("Semantic search error (non-critical): ${e.message}")
                // Continue with existing results
            }
        }

        // Convert to list and apply filters
        var finalResults = mergedResults.values.toList()
        
        if (options.surahNumbers.isNotEmpty()) {
            finalResults = finalResults.filter { it.surahNumber in options.surahNumbers }
        }

        saveSearchQuery(query)
        emit(finalResults.sortedByDescending { it.relevanceScore })
    }

    override suspend fun searchByRoot(root: String): Flow<List<SearchResult>> = flow {
        // Root search uses local database (morphological analysis)
        try {
            val results = database.ayahQueries
                .searchInText(root, root, root)
                .executeAsList()
                .map { ayah ->
                    SearchResult(
                        id = "ayah_${ayah.id}",
                        surahNumber = ayah.surahNumber.toInt(),
                        ayahNumber = ayah.ayahNumber.toInt(),
                        text = ayah.text,
                        relevanceScore = 0.9f,
                        matchType = MatchType.ROOT
                    )
                }
            emit(results)
        } catch (e: Exception) {
            println("Root search error: ${e.message}")
            e.printStackTrace()
            emit(emptyList())
        }
    }

    override suspend fun searchByTopic(topic: String): Flow<List<SearchResult>> = flow {
        // Topic search benefits from semantic AI, but fallback to text search
        if (semanticSearch != null) {
            try {
                val results = semanticSearch.searchByTopic(topic)
                if (results.isNotEmpty()) {
                    emit(results)
                    return@flow
                }
            } catch (e: Exception) {
                println("Semantic topic search error: ${e.message}")
                // Fallback to text search
            }
        }
        
        // Fallback to regular text search
        searchText(topic, SearchOptions()).collect { list ->
            emit(list.map { it.copy(matchType = MatchType.SEMANTIC) })
        }
    }

    override suspend fun semanticSearch(query: String, limit: Int): Flow<List<SearchResult>> = flow {
        if (semanticSearch != null) {
            try {
                val results = semanticSearch.searchSemantic(query).take(limit)
                emit(results)
                return@flow
            } catch (e: Exception) {
                // Fallback
            }
        }
        
        // Fallback to text search
        val results = searchText(query, SearchOptions()).first().take(limit)
        emit(results)
    }

    override suspend fun search(query: SearchQuery): Flow<List<SearchResult>> = flow {
        val options = SearchOptions(
            surahNumbers = query.filters.surahNumbers,
            translationIds = query.filters.translationIds
        )
        searchText(query.query, options).collect { emit(it) }
    }

    override suspend fun voiceSearch(audioData: ByteArray): Flow<VoiceSearchResult?> = flow {
        // Voice search requires platform-specific speech recognition
        emit(null)
    }

    override suspend fun getSearchSuggestions(query: String): Flow<List<String>> = flow {
        if (query.length < 2) {
            emit(emptyList())
            return@flow
        }

        try {
            val suggestions = api.getSearchSuggestions(query)
            emit(suggestions)
        } catch (e: Exception) {
            // Fallback to local suggestions
            val ayahs = database.ayahQueries.searchInText(query, query, query).executeAsList().take(5)
            val suggestions = ayahs.map { it.text.take(50) + "..." }
            emit(suggestions)
        }
    }

    override suspend fun getSearchHistory(): Flow<List<SearchHistory>> {
        return database.searchQueries.selectAllSearchHistory(20)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map { it.toDomain() }
            }
    }

    override suspend fun saveSearchQuery(query: String) {
        val timestamp = Clock.System.now().toEpochMilliseconds()
        database.searchQueries.insertSearchHistory(
            query = query,
            type = SearchType.TEXT.name,
            resultCount = 0,
            timestamp = timestamp,
            isFavorite = 0
        )
    }

    override suspend fun clearSearchHistory() {
        database.searchQueries.clearSearchHistory()
    }

    private fun SearchHistoryDb.toDomain(): SearchHistory {
        return SearchHistory(
            id = id,
            query = query,
            type = SearchType.valueOf(type),
            resultCount = resultCount.toInt(),
            timestamp = timestamp,
            isFavorite = isFavorite != 0L
        )
    }

    override suspend fun getSavedSearches(): Flow<List<SavedSearch>> {
        return database.searchQueries.selectAllSavedSearches()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map { it.toDomain() }
            }
    }

    override suspend fun saveSearch(name: String, query: String, filters: SearchOptions) {
        val timestamp = Clock.System.now().toEpochMilliseconds()
        val filtersJson = Json.encodeToString(filters)
        database.searchQueries.insertSavedSearch(
            name = name,
            query = query,
            filters = filtersJson,
            createdAt = timestamp,
            lastUsed = timestamp
        )
    }

    override suspend fun deleteSavedSearch(id: Long) {
        database.searchQueries.deleteSavedSearch(id)
    }

    private fun SavedSearchDb.toDomain(): SavedSearch {
        val filters = try {
            Json.decodeFromString<SearchOptions>(filters)
        } catch (e: Exception) {
            SearchOptions()
        }
        return SavedSearch(
            id = id,
            name = name,
            query = query,
            filters = filters,
            createdAt = createdAt,
            lastUsed = lastUsed
        )
    }

    override suspend fun getRecentSearches(): Flow<List<String>> {
        return getSearchHistory().map { list -> list.map { it.query } }
    }

    override suspend fun searchInQuran(query: String): Flow<List<SearchResult>> = 
        searchText(query, SearchOptions())

    override suspend fun saveRecentSearch(query: String) {
        saveSearchQuery(query)
    }

    override suspend fun searchWithFilters(
        query: String,
        surahs: List<Int>,
        translations: List<String>
    ): Flow<List<SearchResult>> {
        return searchText(query, SearchOptions(surahNumbers = surahs, translationIds = translations))
    }

    override suspend fun deleteRecentSearch(query: String) {
        database.searchQueries.deleteSearchHistoryByQuery(query)
    }

    override suspend fun clearRecentSearches() {
        database.searchQueries.clearSearchHistory()
    }
}
