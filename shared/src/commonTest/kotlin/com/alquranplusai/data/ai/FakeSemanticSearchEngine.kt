package com.alquranplusai.data.ai

import com.alquranplusai.domain.models.SearchResult
import com.alquranplusai.domain.models.VerseReference

class FakeSemanticSearchEngine : SemanticSearchEngine {
    var semanticResults: List<SearchResult> = emptyList()
    var lastQuery: String? = null
    var shouldThrowException = false

    override suspend fun searchSemantic(query: String): List<SearchResult> {
        lastQuery = query
        if (shouldThrowException) throw Exception("Fake AI Error")
        return semanticResults
    }

    override suspend fun findSimilarVerses(surahNumber: Int, ayahNumber: Int): List<SearchResult> = emptyList()
    
    override suspend fun searchByTopic(topic: String): List<SearchResult> = semanticResults
}
