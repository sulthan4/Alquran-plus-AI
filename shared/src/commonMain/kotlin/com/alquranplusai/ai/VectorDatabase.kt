package com.alquranplusai.ai

/**
 * In-memory vector database with cosine similarity search.
 * Stores embeddings indexed by string ID and supports top-K nearest neighbor search.
 */
class VectorDatabase {

    private val vectors = mutableMapOf<String, FloatArray>()
    private val embeddingsProcessor = EmbeddingsProcessor()

    /**
     * Add or update a vector with the given ID.
     */
    fun addVector(id: String, vector: FloatArray) {
        vectors[id] = vector
    }

    /**
     * Add multiple vectors at once (batch insert).
     */
    fun addVectors(entries: Map<String, FloatArray>) {
        vectors.putAll(entries)
    }

    /**
     * Search for the top-K most similar vectors to the query.
     * @return List of (id, similarity_score) pairs, sorted by score descending.
     */
    fun search(query: FloatArray, topK: Int = 10): List<Pair<String, Float>> {
        if (vectors.isEmpty() || query.isEmpty()) return emptyList()

        return vectors
            .map { (id, vector) -> id to embeddingsProcessor.cosineSimilarity(query, vector) }
            .filter { it.second > 0.01f }
            .sortedByDescending { it.second }
            .take(topK)
    }

    /** Get the number of stored vectors. */
    fun size(): Int = vectors.size

    /** Clear all stored vectors. */
    fun clear() {
        vectors.clear()
    }

    /** Check if a vector with the given ID exists. */
    fun contains(id: String): Boolean = vectors.containsKey(id)
}
