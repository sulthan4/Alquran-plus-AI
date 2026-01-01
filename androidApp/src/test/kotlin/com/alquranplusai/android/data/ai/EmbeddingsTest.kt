package com.alquranplusai.data.ai

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlinx.coroutines.runBlocking

class EmbeddingsTest {

    private val processor = EmbeddingsProcessorImpl()
    private val database = VectorDatabase()

    @Test
    fun `test embeddings are deterministic`() = runBlocking {
        val text = "In the name of Allah"
        val vec1 = processor.generateEmbedding(text)
        val vec2 = processor.generateEmbedding(text)
        
        assertEquals(vec1.size, vec2.size)
        for (i in vec1.indices) {
            assertEquals(vec1[i], vec2[i], 0.0001f)
        }
    }

    @Test
    fun `test similar texts have higher similarity`() = runBlocking {
        val text1 = "Allah is Merciful"
        val text2 = "Allah is the Most Merciful" // Overlaps: Allah, is, Merciful
        val text3 = "The weather is nice" // Overlaps: is (maybe)

        val vec1 = processor.generateEmbedding(text1)
        val vec2 = processor.generateEmbedding(text2)
        val vec3 = processor.generateEmbedding(text3)

        val sim12 = processor.cosineSimilarity(vec1, vec2)
        val sim13 = processor.cosineSimilarity(vec1, vec3)

        println("Similarity 1-2: $sim12")
        println("Similarity 1-3: $sim13")

        assertTrue("Related texts should be more similar", sim12 > sim13)
    }

    @Test
    fun `test vector database search`() = runBlocking {
        val entry1 = Triple("1", "Patience is a virtue", mapOf("text" to "Patience is a virtue"))
        val entry2 = Triple("2", "God loves those who are patient", mapOf("text" to "God loves those who are patient"))
        val entry3 = Triple("3", "Apples are red", mapOf("text" to "Apples are red"))

        // Manually adding to DB logic simulation since I can't call SemantinSearchEngine.indexTexts which relies on Context
        // I will use processor to gen vectors and add to DB directly
        
        database.addVector("1", processor.generateEmbedding(entry1.second), entry1.third)
        database.addVector("2", processor.generateEmbedding(entry2.second), entry2.third)
        database.addVector("3", processor.generateEmbedding(entry3.second), entry3.third)

        val query = "patient"
        val queryVec = processor.generateEmbedding(query)
        val results = database.search(queryVec)

        println("Results for '$query': ${results.joinToString { "${it.id} (${it.score})" }}")
        
        // Expect entry 2 to be high because of "patient"
        // Entry 1 also relates via "Patience" if stemming was used, but with pure BoW "Patience" != "patient" unless normalized.
        // My implementation splits by regex and lowercase.
        // "Patience".lowercase -> patience. "patient" -> patient. Distinct.
        // So 2 should be top. 1 might be 0 unless overlap.
        
        assertTrue(results.any { it.id == "2" })
    }
}
