package com.alquranplusai.ai

/**
 * Generates text embeddings using a bag-of-words TF-IDF approach.
 * Produces meaningful similarity scores without requiring a TFLite model file.
 * Uses 128-dimensional vectors with L2 normalization.
 */
class EmbeddingsProcessor {

    companion object {
        private const val VECTOR_SIZE = 128

        private val ARABIC_STOP_WORDS = setOf(
            "في", "من", "إلى", "على", "عن", "مع", "هو", "هي", "هم", "أن", "إن",
            "كان", "كانت", "لا", "ما", "لم", "قد", "ثم", "أو", "و", "ف", "هذا", "هذه"
        )

        private val ENGLISH_STOP_WORDS = setOf(
            "the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for",
            "of", "with", "by", "from", "is", "are", "was", "were", "be", "been",
            "have", "has", "had", "do", "does", "did", "will", "would", "could", "should"
        )
    }

    private val idfCache = mutableMapOf<String, Float>()
    private var documentCount = 0
    private val termDocumentFrequency = mutableMapOf<String, Int>()

    /**
     * Index a corpus of documents to compute IDF weights.
     * Must be called before generateEmbedding() for best results.
     */
    fun indexCorpus(documents: List<String>) {
        documentCount = documents.size
        termDocumentFrequency.clear()

        documents.forEach { doc ->
            val terms = tokenize(doc).toSet()
            terms.forEach { term ->
                termDocumentFrequency[term] = (termDocumentFrequency[term] ?: 0) + 1
            }
        }

        idfCache.clear()
        termDocumentFrequency.forEach { (term, df) ->
            idfCache[term] = kotlin.math.ln((documentCount + 1).toFloat() / (df + 1)) + 1f
        }
    }

    /**
     * Generate a normalized 128-dimensional embedding vector for the given text.
     */
    fun generateEmbedding(text: String): FloatArray {
        val tokens = tokenize(text)
        if (tokens.isEmpty()) return FloatArray(VECTOR_SIZE)

        val vector = FloatArray(VECTOR_SIZE)
        val termFrequency = mutableMapOf<String, Int>()
        tokens.forEach { token ->
            termFrequency[token] = (termFrequency[token] ?: 0) + 1
        }

        termFrequency.forEach { (term, tf) ->
            val tfWeight = tf.toFloat() / tokens.size
            val idfWeight = idfCache[term] ?: (kotlin.math.ln(documentCount.toFloat() + 1) + 1f)
            val tfidf = tfWeight * idfWeight

            // Multiple hash functions to reduce collisions
            val h1 = (term.hashCode() and Int.MAX_VALUE) % VECTOR_SIZE
            val h2 = ((term.hashCode() * 2654435761L).toInt() and Int.MAX_VALUE) % VECTOR_SIZE
            val h3 = ((term.hashCode() * 40503L).toInt() and Int.MAX_VALUE) % VECTOR_SIZE

            vector[h1] += tfidf
            vector[h2] += tfidf * 0.5f
            vector[h3] += tfidf * 0.25f
        }

        return normalize(vector)
    }

    /**
     * Compute cosine similarity between two embedding vectors.
     * Returns a value in [0, 1] where 1 means identical.
     */
    fun cosineSimilarity(a: FloatArray, b: FloatArray): Float {
        if (a.size != b.size) return 0f
        var dotProduct = 0f
        var normA = 0f
        var normB = 0f
        for (i in a.indices) {
            dotProduct += a[i] * b[i]
            normA += a[i] * a[i]
            normB += b[i] * b[i]
        }
        val denominator = kotlin.math.sqrt(normA.toDouble()) * kotlin.math.sqrt(normB.toDouble())
        return if (denominator == 0.0) 0f else (dotProduct / denominator).toFloat()
    }

    private fun tokenize(text: String): List<String> {
        return text
            .lowercase()
            .replace(Regex("[^\\w\\s\\u0600-\\u06FF]"), " ")
            .split(Regex("\\s+"))
            .filter { it.length > 1 }
            .filter { it !in ARABIC_STOP_WORDS && it !in ENGLISH_STOP_WORDS }
    }

    private fun normalize(vector: FloatArray): FloatArray {
        val norm = kotlin.math.sqrt(vector.sumOf { (it * it).toDouble() }).toFloat()
        return if (norm == 0f) vector else FloatArray(vector.size) { vector[it] / norm }
    }
}
