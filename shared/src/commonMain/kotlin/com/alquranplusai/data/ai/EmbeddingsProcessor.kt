package com.alquranplusai.data.ai

import kotlin.math.sqrt

/**
 * Embeddings processor for semantic search
 */
interface EmbeddingsProcessor {
    suspend fun generateEmbedding(text: String): FloatArray
    suspend fun calculateSimilarity(embedding1: FloatArray, embedding2: FloatArray): Float
}

/**
 * Basic implementation of EmbeddingsProcessor for demonstration
 */
class BasicEmbeddingsProcessor : EmbeddingsProcessor {
    override suspend fun generateEmbedding(text: String): FloatArray {
        val embedding = FloatArray(512)
        val hash = text.hashCode()
        for (i in embedding.indices) {
            embedding[i] = ((hash + i) % 100) / 100f
        }
        return embedding
    }
    
    override suspend fun calculateSimilarity(embedding1: FloatArray, embedding2: FloatArray): Float {
        require(embedding1.size == embedding2.size) { "Embeddings must have same size" }
        
        var dotProduct = 0f
        var norm1 = 0f
        var norm2 = 0f
        
        for (i in embedding1.indices) {
            dotProduct += embedding1[i] * embedding2[i]
            norm1 += embedding1[i] * embedding1[i]
            norm2 += embedding2[i] * embedding2[i]
        }
        
        val norms = sqrt(norm1) * sqrt(norm2)
        return if (norms == 0f) 0f else dotProduct / norms
    }
}
