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
import com.alquranplusai.data.network.dto.QuranFoundationSearchResultDto

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
        println("SearchRepo: searchText called with query='$query'")
        val mergedResults = mutableMapOf<String, SearchResult>()
        
        // STRATEGY CHANGE: Prioritize local database (guaranteed to work)
        // Then enhance with API and AI if available
        
        // 1. Local Database Search (Primary - Always Works)
        try {
            val dbResults = database.ayahQueries
                .searchInText(query, query, query)
                .executeAsList()
            println("SearchRepo: Database returned ${dbResults.size} results")
            dbResults.forEach { ayah ->
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
            println("SearchRepo: Database search error: ${e.message}")
            e.printStackTrace()
        }
        
        // 2. API Search (Enhancement - May Fail)
        try {
            println("SearchRepo: Calling API search...")
            
            // Determine search languages based on filters
            val languagesToSearch = mutableListOf<String>()
            
            if (options.translationIds.isNotEmpty()) {
                if (options.translationIds.any { it.contains("Sahih", ignoreCase = true) || it.contains("English", ignoreCase = true) }) {
                    languagesToSearch.add("en")
                }
                if (options.translationIds.any { it.contains("Urdu", ignoreCase = true) }) {
                    languagesToSearch.add("ur")
                }
            }
            
            // Default fallback if no specific language selected or if only Surah filter applied
            if (languagesToSearch.isEmpty()) {
                languagesToSearch.add("en")
            }
            
            val allApiResults = mutableListOf<QuranFoundationSearchResultDto>()
            
            for (lang in languagesToSearch) {
                val results = api.searchText(query, lang)
                println("SearchRepo: API ($lang) returned ${results.size} results")
                allApiResults.addAll(results)
            }
            
            // If explicit filters yielded nothing, or defaults yielded nothing, try Arabic as fallback
            if (allApiResults.isEmpty() && languagesToSearch.contains("en") && options.translationIds.isEmpty()) {
                 val arResults = api.searchText(query, "ar")
                 println("SearchRepo: Fallback Arabic API returned ${arResults.size} results")
                 allApiResults.addAll(arResults)
            }
            
            allApiResults.forEach {
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
            println("SearchRepo: API search error (non-critical): ${e.message}")
            e.printStackTrace()
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
                println("SearchRepo: Semantic search error (non-critical): ${e.message}")
                // Continue with existing results
            }
        }

        // Convert to list and apply filters
        var finalResults = mergedResults.values.toList()
        
        if (options.surahNumbers.isNotEmpty()) {
            finalResults = finalResults.filter { it.surahNumber in options.surahNumbers }
        }

        println("SearchRepo: Emitting ${finalResults.size} total results")
        emit(finalResults.sortedByDescending { it.relevanceScore })
    }

    override suspend fun searchByRoot(root: String): Flow<List<SearchResult>> = flow {
        // Root search - use API with root as query, fallback to text search
        // True root search requires morphological analysis not available here
        try {
            // Try local database first
            val dbResults = database.ayahQueries
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
            
            if (dbResults.isNotEmpty()) {
                emit(dbResults)
                return@flow
            }
            
            // Fallback to API text search with ROOT label
            searchText(root, SearchOptions()).collect { list ->
                emit(list.map { it.copy(matchType = MatchType.ROOT) })
            }
        } catch (e: Exception) {
            println("Root search error: ${e.message}")
            e.printStackTrace()
            // Last resort - API search
            searchText(root, SearchOptions()).collect { list ->
                emit(list.map { it.copy(matchType = MatchType.ROOT) })
            }
        }
    }

    override suspend fun searchByTopic(topic: String): Flow<List<SearchResult>> = flow {
        // Topic search benefits from semantic AI, but fallback to text search
        if (semanticSearch != null) {
            try {
                val results = semanticSearch.searchByTopic(topic)
                if (results.isNotEmpty()) {
                    emit(results.map { it.copy(matchType = MatchType.TOPIC) })
                    return@flow
                }
            } catch (e: Exception) {
                println("Semantic topic search error: ${e.message}")
                // Fallback to text search
            }
        }
        
        // Fallback to regular text search with TOPIC label
        searchText(topic, SearchOptions()).collect { list ->
            emit(list.map { it.copy(matchType = MatchType.TOPIC) })
        }
    }

    override suspend fun semanticSearch(query: String, limit: Int): Flow<List<SearchResult>> {
        // Fallback to text search since AI semantic search is not ready
        // We reuse the working searchInQuran flow and just retag the results
        return searchInQuran(query).map { list ->
            list.map { it.copy(matchType = MatchType.SEMANTIC) }.take(limit)
        }
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
