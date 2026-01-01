package com.alquranplusai.data.ai

import com.alquranplusai.domain.models.Ayah
import com.alquranplusai.domain.models.SearchResult
import com.alquranplusai.domain.models.MatchType

/** Semantic search engine for Quran content */
interface SemanticSearchEngine {
    suspend fun searchSemantic(query: String): List<SearchResult>
    suspend fun findSimilarVerses(surahNumber: Int, ayahNumber: Int): List<SearchResult>
    suspend fun searchByTopic(topic: String): List<SearchResult>
}

class DefaultSemanticSearchEngine(
        private val embeddingsProcessor: EmbeddingsProcessor,
        private val vectorDatabase: VectorDatabase
) : SemanticSearchEngine {

    override suspend fun searchSemantic(query: String): List<SearchResult> {
        val queryEmbedding = embeddingsProcessor.generateEmbedding(query)
        val similarIdsWithScores = vectorDatabase.searchSimilarWithScores(queryEmbedding, limit = 20)
        return similarIdsWithScores.map { (id, score) ->
            SearchResult(
                id = "semantic_$id",
                surahNumber = id / 1000,
                ayahNumber = id % 1000,
                text = "", // To be populated by repository or local DB
                relevanceScore = score,
                matchType = MatchType.SEMANTIC
            )
        }
    }

    override suspend fun findSimilarVerses(surahNumber: Int, ayahNumber: Int): List<SearchResult> {
        val id = surahNumber * 1000 + ayahNumber
        val ayahEmbedding = vectorDatabase.getEmbedding(id) ?: return emptyList()
        val similarIdsWithScores = vectorDatabase.searchSimilarWithScores(ayahEmbedding, limit = 10)
        return similarIdsWithScores
            .filter { it.first != id }
            .map { (simId, score) ->
                SearchResult(
                    id = "similar_$simId",
                    surahNumber = simId / 1000,
                    ayahNumber = simId % 1000,
                    text = "",
                    relevanceScore = score,
                    matchType = MatchType.SEMANTIC
                )
            }
    }

    override suspend fun searchByTopic(topic: String): List<SearchResult> {
        return searchSemantic(topic)
    }
}
