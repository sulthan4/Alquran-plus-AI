package com.alquranplusai.data.network.api

import com.alquranplusai.data.network.dto.TafsirDto
import com.alquranplusai.data.network.dto.TafsirTextDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * API service for fetching Tafsir data
 */
class TafsirApiService(private val client: HttpClient) {
    
    private val baseUrl = "https://api.quran.com/api/v4"

    /**
     * Get all available tafsirs
     */
    suspend fun getAllTafsirs(): List<TafsirDto> {
        // Note: This endpoint may vary based on the actual API
        // For now, we'll return an empty list and rely on local seeding
        return emptyList()
    }

    /**
     * Get tafsir data for all ayahs
     * MOCK IMPLEMENTATION - generates sample tafsir text for testing
     */
    /**
     * Get tafsir data for all ayahs (Full Download)
     */
    /**
     * Get tafsir data for all ayahs (Full Download)
     */
    suspend fun getTafsirData(tafsirId: String): List<TafsirTextDto> {
        val allTexts = mutableListOf<TafsirTextDto>()
        
        // Loop through all 114 surahs
        for (surahNumber in 1..114) {
            try {
                // Determine ayah count to properly size requests or just fetch by chapter
                val surahTexts = getTafsirForSurah(tafsirId, surahNumber)
                allTexts.addAll(surahTexts)
            } catch (e: Exception) {
                println("Failed to fetch tafsir $tafsirId for surah $surahNumber: ${e.message}")
            }
        }
        
        return allTexts
    }

    /**
     * Get tafsir for a specific surah using real API
     * Supports both Tafsir (Standard IDs) and Translation-as-Tafsir (ID prefix "trans_")
     */
    suspend fun getTafsirForSurah(tafsirId: String, surahNumber: Int): List<TafsirTextDto> {
        return if (tafsirId.startsWith("trans_")) {
            // Fetch Translation as Tafsir
            val realId = tafsirId.removePrefix("trans_")
            getTranslationForSurah(tafsirId, realId, surahNumber)
        } else {
            // Fetch Real Tafsir
            val response: TafsirResponse = client.get(
                "$baseUrl/tafsirs/$tafsirId/by_chapter/$surahNumber"
            ) {
                parameter("per_page", 300)
            }.body()
            
            response.tafsirs.map { dto ->
                val parts = dto.verseKey.split(":")
                TafsirTextDto(
                    tafsirId = tafsirId,
                    surahNumber = parts[0].toInt(),
                    ayahNumber = parts[1].toInt(),
                    text = dto.text,
                    footnotes = emptyList(),
                    references = emptyList()
                )
            }
        }
    }

    private suspend fun getTranslationForSurah(originalId: String, resourceId: String, surahNumber: Int): List<TafsirTextDto> {
        val response: VersesResponse = client.get(
            "$baseUrl/verses/by_chapter/$surahNumber"
        ) {
            parameter("translations", resourceId)
            parameter("per_page", 300)
            parameter("fields", "text_uthmani") // Minimize payload
        }.body()

        return response.verses.mapNotNull { verse ->
            val translation = verse.translations.firstOrNull { it.resourceId.toString() == resourceId }
            translation?.let {
                val parts = verse.verseKey.split(":")
                TafsirTextDto(
                    tafsirId = originalId,
                    surahNumber = parts[0].toInt(),
                    ayahNumber = parts[1].toInt(),
                    text = it.text, // Use translation text as tafsir
                    footnotes = emptyList(),
                    references = emptyList()
                )
            }
        }
    }
    
    /**
     * Get tafsir for a specific ayah
     */
    suspend fun getTafsirForAyah(
        tafsirId: String,
        surahNumber: Int,
        ayahNumber: Int
    ): TafsirTextDto? {
        return try {
            if (tafsirId.startsWith("trans_")) {
                val realId = tafsirId.removePrefix("trans_")
                // Fetch translation for single ayah
                val response: VersesResponse = client.get(
                    "$baseUrl/verses/by_key/$surahNumber:$ayahNumber"
                ) {
                   parameter("translations", realId)
                }.body()
                
                val verse = response.verses.firstOrNull() ?: return null
                val translation = verse.translations.firstOrNull { it.resourceId.toString() == realId }
                
                translation?.let {
                    TafsirTextDto(
                        tafsirId = tafsirId,
                        surahNumber = surahNumber,
                        ayahNumber = ayahNumber,
                        text = it.text,
                        footnotes = emptyList(),
                        references = emptyList()
                    )
                }
            } else {
                val response: TafsirAyahResponse = client.get(
                    "$baseUrl/tafsirs/$tafsirId/by_ayah/$surahNumber:$ayahNumber"
                ).body()
                
                TafsirTextDto(
                    tafsirId = tafsirId,
                    surahNumber = surahNumber,
                    ayahNumber = ayahNumber,
                    text = response.tafsir.text,
                    footnotes = emptyList(),
                    references = emptyList()
                )
            }
        } catch (e: Exception) {
            println("Error fetching ayah tafsir: $e")
            null
        }
    }
}

// Response DTOs
@kotlinx.serialization.Serializable
private data class TafsirResponse(
    val tafsirs: List<TafsirItemDto>
)

@kotlinx.serialization.Serializable
private data class TafsirItemDto(
    @kotlinx.serialization.SerialName("resource_id") val resourceId: Int,
    @kotlinx.serialization.SerialName("verse_key") val verseKey: String,
    val text: String
)

@kotlinx.serialization.Serializable
private data class TafsirAyahResponse(
    val tafsir: TafsirAyahDto
)

@kotlinx.serialization.Serializable
private data class TafsirAyahDto(
    val text: String
)

@kotlinx.serialization.Serializable
private data class VersesResponse(
    val verses: List<VerseDto> = emptyList(),
    @kotlinx.serialization.SerialName("verse") val verse: VerseDto? = null // Handle single verse response structure if different
) {
    // Helper because single verse endpoint might return "verse": {} instead of "verses": []? 
    // Checking API docs: /verses/by_key returns {"verse": {...}}
    // So I need to handle that.
    fun getList(): List<VerseDto> = if (verse != null) listOf(verse) else verses
}
// Wait, VersesResponse might differ for by_key vs by_chapter.
// by_chapter -> { "verses": [] }
// by_key -> { "verse": {} } 
// I should handle this carefully.

@kotlinx.serialization.Serializable
private data class VerseDto(
    @kotlinx.serialization.SerialName("verse_key") val verseKey: String,
    val translations: List<TranslationDto> = emptyList()
)

@kotlinx.serialization.Serializable
private data class TranslationDto(
    val id: Int? = null,
    @kotlinx.serialization.SerialName("resource_id") val resourceId: Int,
    val text: String
)
