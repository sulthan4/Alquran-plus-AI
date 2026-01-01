package com.alquranplusai.data.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.sqrt

/**
 * Vector Database for storing and searching embeddings
 */
class VectorDatabase {
    
    private val vectors = mutableMapOf<String, VectorEntry>()
    
    data class VectorEntry(
        val id: String,
        val embedding: FloatArray,
        val metadata: Map<String, String> = emptyMap()
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as VectorEntry
            return id == other.id
        }
        
        override fun hashCode(): Int = id.hashCode()
    }
    
    /**
     * Add vector to database
     */
    suspend fun addVector(id: String, embedding: FloatArray, metadata: Map<String, String> = emptyMap()) {
        withContext(Dispatchers.IO) {
            vectors[id] = VectorEntry(id, embedding, metadata)
        }
    }
    
    /**
     * Add multiple vectors
     */
    suspend fun addVectors(entries: List<VectorEntry>) {
        withContext(Dispatchers.IO) {
            entries.forEach { vectors[it.id] = it }
        }
    }
    
    /**
     * Search for similar vectors using cosine similarity
     */
    suspend fun search(queryEmbedding: FloatArray, topK: Int = 10, threshold: Float = 0.0f): List<SearchResult> {
        return withContext(Dispatchers.Default) {
            vectors.values
                .map { entry ->
                    val similarity = cosineSimilarity(queryEmbedding, entry.embedding)
                    SearchResult(entry.id, similarity, entry.metadata)
                }
                .filter { it.score >= threshold }
                .sortedByDescending { it.score }
                .take(topK)
        }
    }
    
    /**
     * Get vector by ID
     */
    fun getVector(id: String): VectorEntry? = vectors[id]
    
    /**
     * Remove vector
     */
    fun removeVector(id: String) {
        vectors.remove(id)
    }
    
    /**
     * Clear all vectors
     */
    fun clear() {
        vectors.clear()
    }
    
    /**
     * Get database size
     */
    fun size(): Int = vectors.size
    
    /**
     * Calculate cosine similarity between two vectors
     */
    private fun cosineSimilarity(a: FloatArray, b: FloatArray): Float {
        require(a.size == b.size) { "Vectors must have same dimension" }
        
        var dotProduct = 0f
        var normA = 0f
        var normB = 0f
        
        for (i in a.indices) {
            dotProduct += a[i] * b[i]
            normA += a[i] * a[i]
            normB += b[i] * b[i]
        }
        
        return if (normA == 0f || normB == 0f) {
            0f
        } else {
            dotProduct / (sqrt(normA) * sqrt(normB))
        }
    }
    
    data class SearchResult(
        val id: String,
        val score: Float,
        val metadata: Map<String, String>
    )
}
