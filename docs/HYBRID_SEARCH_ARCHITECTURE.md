# Hybrid Search Architecture - AlQuranPlusAI

## Overview

AlQuranPlusAI implements a **3-tier hybrid search system** that intelligently combines:

1. **Quran Foundation API** - Fast, accurate text-based search with Arabic language support
2. **AI Semantic Search Engine** - Concept-based understanding using embeddings and vector similarity
3. **Local Database** - Offline fallback with full-text search capabilities

This architecture provides the best possible search experience by leveraging the strengths of each approach.

---

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                     SearchRepository                         │
│                   (Hybrid Coordinator)                       │
└─────────────────────────────────────────────────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
        ▼                   ▼                   ▼
┌──────────────┐   ┌──────────────┐   ┌──────────────┐
│  Quran API   │   │  AI Semantic │   │   Local DB   │
│   (Primary)  │   │  (Enhanced)  │   │  (Fallback)  │
└──────────────┘   └──────────────┘   └──────────────┘
        │                   │                   │
        ▼                   ▼                   ▼
  Text Search        Concept Search      Offline Search
  + Highlighting     + Similarity        + Full-text
  + Multi-lang       + Topic-based       + Root-based
```

---

## Search Flow Strategy

### 1. Text Search (Primary)

**Priority Order:**
```kotlin
1. Try Quran Foundation API
   ├─ Success? → Return API results (best quality)
   └─ Fail? → Continue to step 2

2. Try AI Semantic Search (if available)
   ├─ Success? → Return semantic results
   └─ Fail? → Continue to step 3

3. Fallback to Local Database
   └─ Always succeeds (offline support)
```

**Implementation:**
```kotlin
override suspend fun searchText(
    query: String,
    options: SearchOptions
): Flow<List<SearchResult>> = flow {
    val results = mutableListOf<SearchResult>()
    
    // 1. API Search (Primary)
    try {
        val apiResults = api.searchText(query, options.language ?: "ar")
        if (apiResults.isNotEmpty()) {
            results.addAll(apiResults.map { /* map to SearchResult */ })
        }
    } catch (e: Exception) {
        // Continue to semantic/local fallback
    }

    // 2. Semantic Search (AI Enhancement)
    if (semanticSearch != null && results.isEmpty()) {
        try {
            val semanticResults = semanticSearch.searchSemantic(query)
            results.addAll(semanticResults)
        } catch (e: Exception) {
            // Continue to local fallback
        }
    }

    // 3. Local Database Fallback
    if (results.isEmpty()) {
        val localResults = database.ayahQueries
            .searchInText(query, query, query)
            .executeAsList()
        results.addAll(localResults)
    }

    emit(results.sortedByDescending { it.relevanceScore })
}
```

---

## Search Modes

### 1. **Text Search** (Default)
- **Best for**: Exact word or phrase matching
- **Uses**: Quran Foundation API → Local DB
- **Features**:
  - Arabic text search with proper stemming
  - Multi-language translation search
  - Highlighted matches
  - Relevance scoring

**Example:**
```kotlin
searchRepository.searchText("الرحمن", SearchOptions())
// Returns: All verses containing "الرحمن" with highlights
```

### 2. **Semantic Search** (AI-Powered)
- **Best for**: Concept-based queries, natural language
- **Uses**: AI Semantic Engine → Text Search fallback
- **Features**:
  - Understanding of meaning, not just words
  - Topic clustering
  - Similar verse discovery
  - Multilingual concept matching

**Example:**
```kotlin
searchRepository.semanticSearch("verses about patience")
// Returns: Verses related to patience concept, even without exact word
```

### 3. **Root Search** (Arabic Morphology)
- **Best for**: Finding all forms of an Arabic root
- **Uses**: Local Database (morphological analysis)
- **Features**:
  - Arabic root extraction
  - All word forms from same root
  - Grammatical variations

**Example:**
```kotlin
searchRepository.searchByRoot("ص ل ي")
// Returns: صلى، صلاة، مصلى، etc.
```

### 4. **Topic Search** (Thematic)
- **Best for**: Finding verses by theme or subject
- **Uses**: AI Semantic Engine → Text Search fallback
- **Features**:
  - Thematic clustering
  - Related concepts
  - Cross-reference discovery

**Example:**
```kotlin
searchRepository.searchByTopic("prayer times")
// Returns: Verses about Salah, times, obligations
```

---

## Components

### 1. SearchApiService (Quran Foundation API)

**Location:** `shared/src/commonMain/kotlin/com/alquranplusai/data/network/api/SearchApiServiceImpl.kt`

**Responsibilities:**
- HTTP calls to Quran Foundation API `/search` endpoint
- Query parameter handling (q, language, size, page)
- Response parsing and mapping
- Suggestion generation

**API Endpoint:**
```
GET https://api.quran.com/api/v4/search
Parameters:
  - q: search query (required)
  - language: ISO language code (optional)
  - size: results per page (default: 20)
  - page: page number (default: 1)
  - translations: filter by translation IDs
```

### 2. SemanticSearchEngine (AI Component)

**Location:** `shared/src/commonMain/kotlin/com/alquranplusai/data/ai/SemanticSearchEngine.kt`

**Responsibilities:**
- Generate embeddings for queries
- Vector similarity search
- Topic clustering
- Similar verse discovery

**Dependencies:**
- `EmbeddingsProcessor` - Generates vector embeddings
- `VectorDatabase` - Stores and searches verse embeddings
- `TFLiteInterpreter` - Runs ML models

**Note:** Currently optional - gracefully degrades if not available.

### 3. SearchRepositoryImpl (Hybrid Coordinator)

**Location:** `shared/src/commonMain/kotlin/com/alquranplusai/data/repositories/SearchRepositoryImpl.kt`

**Responsibilities:**
- Coordinate between API, AI, and local search
- Intelligent fallback strategy
- Result merging and deduplication
- Relevance scoring
- Search history management

---

## Search Result Model

```kotlin
data class SearchResult(
    val id: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val text: String,
    val highlightedText: String? = null,  // HTML with <em> tags
    val relevanceScore: Float,             // 0.0 to 1.0
    val matchType: MatchType? = null,      // EXACT, SEMANTIC, ROOT, etc.
    val translations: List<Translation> = emptyList()
)

enum class MatchType {
    EXACT,      // Exact word match
    PARTIAL,    // Partial match
    SEMANTIC,   // AI semantic match
    ROOT,       // Arabic root match
    FUZZY       // Fuzzy/approximate match
}
```

---

## Search Options & Filters

```kotlin
data class SearchOptions(
    val surahNumbers: List<Int> = emptyList(),        // Filter by surahs
    val translationIds: List<String> = emptyList(),   // Search in translations
    val language: String? = null,                      // Language preference
    val matchType: MatchType? = null,                  // Force match type
    val limit: Int = 20                                // Max results
)
```

**Usage:**
```kotlin
val options = SearchOptions(
    surahNumbers = listOf(2, 3, 4),  // Only search Al-Baqarah, Al-Imran, An-Nisa
    language = "en",                  // Prefer English results
    limit = 10                        // Return max 10 results
)

searchRepository.searchText("guidance", options)
```

---

## UI Integration

### SearchScreen Components

**Location:** `androidApp/src/main/kotlin/com/alquranplusai/android/ui/screens/search/SearchScreen.kt`

**Features:**
1. **Search Bar** - Real-time search with debouncing
2. **Mode Tabs** - Switch between Text/Semantic/Root/Topic
3. **Recent Searches** - Quick access to previous queries
4. **Filters** - Surah and translation filters
5. **Results List** - Highlighted results with match type badges
6. **Empty States** - Helpful messages when no results

**ViewModel:**
```kotlin
class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {
    
    val searchResults: StateFlow<List<SearchResult>>
    val isSearching: StateFlow<Boolean>
    val recentSearches: StateFlow<List<String>>
    
    fun search(query: String)
    fun searchWithFilters(query: String, surahs: List<Int>, translations: List<String>)
    fun clearSearch()
    fun deleteRecentSearch(query: String)
}
```

---

## Performance Optimization

### 1. **Caching Strategy**
```kotlin
// API results cached for 5 minutes
private val apiCache = LruCache<String, List<SearchResult>>(maxSize = 50)

// Semantic results cached indefinitely (expensive to compute)
private val semanticCache = LruCache<String, List<SearchResult>>(maxSize = 100)
```

### 2. **Debouncing**
```kotlin
// In SearchViewModel
private val searchJob = MutableStateFlow<Job?>(null)

fun search(query: String) {
    searchJob.value?.cancel()
    searchJob.value = viewModelScope.launch {
        delay(300) // Wait 300ms before searching
        performSearch(query)
    }
}
```

### 3. **Pagination**
```kotlin
// Load results in batches
fun loadMore() {
    currentPage++
    searchRepository.searchText(query, options.copy(page = currentPage))
}
```

---

## Future Enhancements

### 1. **Voice Search**
```kotlin
override suspend fun voiceSearch(audioData: ByteArray): Flow<VoiceSearchResult?> {
    // Convert speech to text using platform-specific API
    val text = speechRecognizer.recognize(audioData)
    
    // Perform semantic search on recognized text
    return semanticSearch(text, limit = 10)
}
```

### 2. **Advanced Filters**
- Date range (revelation order)
- Verse length
- Recitation availability
- Translation quality score

### 3. **Search Analytics**
- Track popular queries
- Improve relevance based on user behavior
- Personalized search results

### 4. **Offline AI**
- Download TFLite models for offline semantic search
- On-device embedding generation
- Local vector database

---

## Testing

### Unit Tests
```kotlin
class SearchRepositoryTest {
    @Test
    fun `search falls back to local when API fails`() = runTest {
        // Given: API throws exception
        coEvery { api.searchText(any()) } throws IOException()
        
        // When: Search is performed
        val results = repository.searchText("test", SearchOptions()).first()
        
        // Then: Local database is used
        assertTrue(results.isNotEmpty())
        assertEquals(MatchType.PARTIAL, results.first().matchType)
    }
}
```

### Integration Tests
```kotlin
@Test
fun `hybrid search returns API results first`() = runTest {
    // Given: Both API and local have results
    coEvery { api.searchText("الله") } returns listOf(apiResult)
    coEvery { database.search("الله") } returns listOf(localResult)
    
    // When: Search is performed
    val results = repository.searchText("الله", SearchOptions()).first()
    
    // Then: API result is returned (higher priority)
    assertEquals(apiResult.id, results.first().id)
}
```

---

## Troubleshooting

### Issue: No results from API
**Solution:** Check network connectivity, verify API key, check rate limits

### Issue: Semantic search not working
**Solution:** Ensure AI models are downloaded, check `SemanticSearchEngine` is injected

### Issue: Slow search performance
**Solution:** Enable caching, reduce result limit, optimize database indices

---

## Summary

The hybrid search architecture provides:

✅ **Best Quality** - Quran Foundation API with proper Arabic handling  
✅ **Smart Understanding** - AI semantic search for concept-based queries  
✅ **Offline Support** - Local database fallback always available  
✅ **Flexibility** - Multiple search modes for different use cases  
✅ **Performance** - Intelligent caching and fallback strategies  

This design ensures users always get relevant results, whether online or offline, with the best possible search experience.
