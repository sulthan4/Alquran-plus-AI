package com.alquranplusai.ai

import com.alquranplusai.domain.models.MatchType
import com.alquranplusai.domain.models.SearchResult

/**
 * Semantic search engine using TF-IDF embeddings and cosine similarity.
 * Provides meaningful search results without requiring a TFLite model file.
 */
class SemanticSearchEngine {

    private val embeddingsProcessor = EmbeddingsProcessor()
    private val vectorDatabase = VectorDatabase()
    private var isIndexed = false

    /**
     * Index a corpus of verses for semantic search.
     * @param verses List of (id, text) pairs where id is "surahNumber:ayahNumber"
     */
    fun indexVerses(verses: List<Pair<String, String>>) {
        // First pass: build IDF weights across the corpus
        embeddingsProcessor.indexCorpus(verses.map { it.second })

        // Second pass: generate and store embeddings
        val embeddings = verses.associate { (id, text) ->
            id to embeddingsProcessor.generateEmbedding(text)
        }
        vectorDatabase.addVectors(embeddings)
        isIndexed = true
    }

    /**
     * Search for verses semantically similar to the query.
     * @param query Search query text
     * @param topK Number of results to return
     * @return List of SearchResult sorted by relevance
     */
    suspend fun search(query: String, topK: Int = 20): List<SearchResult> {
        if (!isIndexed || query.isBlank()) return emptyList()

        val queryEmbedding = embeddingsProcessor.generateEmbedding(query)
        val results = vectorDatabase.search(queryEmbedding, topK)

        return results.mapNotNull { (id, score) ->
            try {
                val parts = id.split(":")
                if (parts.size == 2) {
                    SearchResult(
                        id = id,
                        surahNumber = parts[0].toInt(),
                        ayahNumber = parts[1].toInt(),
                        text = "", // Caller should enrich with actual text from DB
                        relevanceScore = score,
                        matchType = MatchType.SEMANTIC
                    )
                } else null
            } catch (e: Exception) {
                null
            }
        }
    }

    /** Check if the search engine has been indexed. */
    fun isReady(): Boolean = isIndexed

    /** Get the number of indexed verses. */
    fun indexedCount(): Int = vectorDatabase.size()

    fun loadModel() {
        // No external model file required for this TF-IDF implementation.
        // Call indexVerses() to prepare for search.
    }
}
