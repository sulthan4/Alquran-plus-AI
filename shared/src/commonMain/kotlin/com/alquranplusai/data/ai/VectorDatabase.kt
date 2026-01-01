package com.alquranplusai.data.ai

/**
 * Vector database for storing and querying embeddings
 */
class VectorDatabase(
    private val embeddingsProcessor: EmbeddingsProcessor
) {
    
    private val storage = mutableMapOf<Int, FloatArray>()
    
    suspend fun storeEmbedding(id: Int, embedding: FloatArray) {
        storage[id] = embedding
    }
    
    suspend fun getEmbedding(id: Int): FloatArray? = storage[id]
    
    suspend fun searchSimilar(queryEmbedding: FloatArray, limit: Int = 10): List<Int> {
        return searchSimilarWithScores(queryEmbedding, limit).map { it.first }
    }
    
    suspend fun searchSimilarWithScores(queryEmbedding: FloatArray, limit: Int = 10): List<Pair<Int, Float>> {
        if (storage.isEmpty()) return emptyList()
        
        return storage.map { (id, embedding) ->
            id to embeddingsProcessor.calculateSimilarity(queryEmbedding, embedding)
        }
        .sortedByDescending { it.second }
        .take(limit)
    }
}
