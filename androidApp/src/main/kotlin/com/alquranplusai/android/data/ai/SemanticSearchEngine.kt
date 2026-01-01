package com.alquranplusai.data.ai

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Semantic Search Engine using embeddings and vector database
 */
class SemanticSearchEngine(
    private val context: Context
) {
    private val vectorDatabase = VectorDatabase()
    private val embeddingsProcessor = EmbeddingsProcessorImpl()
    
    /**
     * Initialize the search engine
     */
    /**
     * Initialize the search engine
     */
    suspend fun initialize() {
        withContext(Dispatchers.IO) {
            try {
                // Load Quran Text
                val quranJson = loadAsset("quran/quran_uthmani.json")
                val quranObj = org.json.JSONObject(quranJson)
                val surahs = quranObj.getJSONArray("surahs")
                
                val textsToIndex = mutableListOf<Triple<String, String, Map<String, String>>>()
                
                for (i in 0 until surahs.length()) {
                    val surah = surahs.getJSONObject(i)
                    val surahNum = surah.getInt("number")
                    val ayahs = surah.getJSONArray("ayahs")
                    
                    for (j in 0 until ayahs.length()) {
                        val ayah = ayahs.getJSONObject(j)
                        val ayahNum = ayah.getInt("number")
                        val text = ayah.getString("text")
                        
                        // ID format: surah_ayah
                        val id = "${surahNum}_${ayahNum}"
                        val metadata = mapOf(
                            "surah" to surahNum.toString(),
                            "ayah" to ayahNum.toString(),
                            "text" to text,
                            "translation" to "Translation placeholder" // Ideally load translation too
                        )
                        
                        textsToIndex.add(Triple(id, text, metadata))
                    }
                }
                
                indexTexts(textsToIndex)
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadAsset(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
    
    /**
     * Index a text with its embedding
     */
    suspend fun indexText(id: String, text: String, metadata: Map<String, String> = emptyMap()) {
        val embedding = embeddingsProcessor.generateEmbedding(text)
        vectorDatabase.addVector(id, embedding, metadata)
    }
    
    /**
     * Index multiple texts
     */
    suspend fun indexTexts(texts: List<Triple<String, String, Map<String, String>>>) {
        withContext(Dispatchers.Default) {
            val entries = texts.map { (id, text, metadata) ->
                val embedding = embeddingsProcessor.generateEmbedding(text)
                VectorDatabase.VectorEntry(id, embedding, metadata)
            }
            vectorDatabase.addVectors(entries)
        }
    }
    
    /**
     * Search for similar texts
     */
    suspend fun search(query: String, topK: Int = 10, threshold: Float = 0.5f): List<SearchResult> {
        return withContext(Dispatchers.Default) {
            val queryEmbedding = embeddingsProcessor.generateEmbedding(query)
            
            val results = vectorDatabase.search(queryEmbedding, topK, threshold)
            
            results.map { result ->
                SearchResult(
                    id = result.id,
                    score = result.score,
                    surahNumber = result.metadata["surah"]?.toIntOrNull() ?: 0,
                    ayahNumber = result.metadata["ayah"]?.toIntOrNull() ?: 0,
                    text = result.metadata["text"] ?: "",
                    translation = result.metadata["translation"] ?: ""
                )
            }
        }
    }
    
    /**
     * Get search suggestions based on partial query
     */
    /**
     * Get search suggestions based on partial query
     */
    suspend fun getSuggestions(partialQuery: String, limit: Int = 5): List<String> {
        if (partialQuery.isBlank()) return emptyList()
        
        return withContext(Dispatchers.Default) {
            // Simple linear scan for substring match in text - acceptable for small dataset
            // In production, use FTS or Trie
            // Accessing vectorDatabase internals ideally, but for now we can't easily iterate 
            // without adding a method to VectorDatabase. 
            // Let's assume we can search for the term as a "keyword"
            
            // For now, return a placeholder list to show UI works
            // In a real implementation, we'd query the FTS (SQLDelight) database
            listOf(
                "$partialQuery in Quran",
                "Verses about $partialQuery",
                "Meaning of $partialQuery"
            )
        }
    }
    
    /**
     * Clear the search index
     */
    fun clearIndex() {
        vectorDatabase.clear()
    }
    
    /**
     * Get index size
     */
    fun getIndexSize(): Int = vectorDatabase.size()
    
    data class SearchResult(
        val id: String,
        val score: Float,
        val surahNumber: Int,
        val ayahNumber: Int,
        val text: String,
        val translation: String
    )
}
