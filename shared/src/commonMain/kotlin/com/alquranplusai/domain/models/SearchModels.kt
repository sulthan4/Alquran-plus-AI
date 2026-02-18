package com.alquranplusai.domain.models

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

/** Search query with filters and context */
@Serializable
data class SearchQuery(
        val query: String,
        val type: SearchType = SearchType.TEXT,
        val filters: SearchFilters = SearchFilters(),
        val context: SearchContext? = null,
        val timestamp: Long = Clock.System.now().toEpochMilliseconds()
)

/** Search filters */
@Serializable
data class SearchFilters(
        val surahNumbers: List<Int> = emptyList(),
        val juzNumbers: List<Int> = emptyList(),
        val revelationType: RevelationType? = null,
        val searchIn: SearchScope = SearchScope.ALL,
        val translationIds: List<String> = emptyList(),
        val exactMatch: Boolean = false,
        val caseSensitive: Boolean = false,
        val includeRoot: Boolean = false,
        val includeMorphology: Boolean = false
)

/** Search context for semantic search */
@Serializable
data class SearchContext(
        val topic: String? = null,
        val intent: SearchIntent? = null,
        val previousQueries: List<String> = emptyList(),
        val userPreferences: Map<String, String> = emptyMap()
)

/** Search result */
@Serializable
data class SearchResult(
        val id: String,
        val surahNumber: Int,
        val ayahNumber: Int,
        val text: String,
        val highlightedText: String? = null,
        val translation: String? = null,
        val highlights: List<TextHighlight> = emptyList(),
        val relevanceScore: Float = 0f,
        val matchType: MatchType = MatchType.EXACT,
        val context: String? = null
)

/** Text highlight in search results */
@Serializable
data class TextHighlight(
        val start: Int,
        val end: Int,
        val type: HighlightType = HighlightType.MATCH
)

/** Voice search result */
@Serializable
data class VoiceSearchResult(
        val transcription: String,
        val confidence: Float,
        val language: String,
        val alternativeTranscriptions: List<String> = emptyList(),
        val searchResults: List<SearchResult> = emptyList()
)

/** AI-powered insights for search results */
@Serializable
data class AIInsights(
        val summary: String,
        val relatedTopics: List<String> = emptyList(),
        val relatedVerses: List<VerseReference> = emptyList(),
        val themes: List<String> = emptyList(),
        val keyWords: List<String> = emptyList(),
        val confidence: Float = 0f
)

/** Verse reference */
@Serializable
data class VerseReference(
        val surahNumber: Int,
        val ayahNumber: Int,
        val relevance: Float = 0f,
        val reason: String? = null
)

/** Search history item */
@Serializable
data class SearchHistoryItem(
        val id: String,
        val query: String,
        val type: SearchType,
        val resultCount: Int,
        val timestamp: Long,
        val isFavorite: Boolean = false
)

/** Trending search query */
@Serializable
data class TrendingSearch(
        val query: String,
        val count: Int,
        val trend: TrendDirection,
        val category: String? = null
)

/** Search suggestion */
@Serializable
data class SearchSuggestion(
        val text: String,
        val type: SuggestionType,
        val score: Float = 0f,
        val metadata: Map<String, String> = emptyMap()
)

/** Search type */
@Serializable
enum class SearchType {
    TEXT,
    VOICE,
    SEMANTIC,
    ROOT,
    MORPHOLOGY,
    TOPIC
}

/** Search scope */
@Serializable
enum class SearchScope {
    ALL,
    QURAN_TEXT,
    TRANSLATIONS,
    TAFSIR,
    WORD_BY_WORD
}

/** Search intent */
@Serializable
enum class SearchIntent {
    FIND_VERSE,
    LEARN_TOPIC,
    FIND_RULING,
    FIND_STORY,
    FIND_DUA,
    GENERAL
}

/** Match type */
@Serializable
enum class MatchType {
    EXACT,
    PARTIAL,
    SEMANTIC,
    ROOT,
    MORPHOLOGY,
    FUZZY,
    TOPIC
}

/** Highlight type */
@Serializable
enum class HighlightType {
    MATCH,
    KEYWORD,
    ENTITY,
    TOPIC
}

/** Suggestion type */
@Serializable
enum class SuggestionType {
    RECENT,
    POPULAR,
    TRENDING,
    AUTOCOMPLETE,
    RELATED
}

/** Trend direction */
@Serializable
enum class TrendDirection {
    UP,
    DOWN,
    STABLE,
    NEW
}

/** Search history entry */
@Serializable
data class SearchHistory(
        val id: Long,
        val query: String,
        val type: SearchType = SearchType.TEXT,
        val resultCount: Int = 0,
        val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
        val isFavorite: Boolean = false
)

/** Saved search */
@Serializable
data class SavedSearch(
        val id: Long,
        val name: String,
        val query: String,
        val filters: SearchOptions = SearchOptions(),
        val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
        val lastUsed: Long = Clock.System.now().toEpochMilliseconds()
)

/** Search options (alias for SearchFilters) */
typealias SearchOptions = SearchFilters
