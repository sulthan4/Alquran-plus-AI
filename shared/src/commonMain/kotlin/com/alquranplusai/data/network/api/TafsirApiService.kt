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
    suspend fun getTafsirData(tafsirId: String): List<TafsirTextDto> {
        val allTexts = mutableListOf<TafsirTextDto>()
        
        // Generate mock tafsir for a subset of ayahs (first 10 surahs for faster download)
        // In production, this would fetch from a real API
        for (surahNumber in 1..10) {
            val ayahCount = getAyahCountForSurah(surahNumber)
            for (ayahNumber in 1..ayahCount) {
                allTexts.add(
                    TafsirTextDto(
                        tafsirId = tafsirId,
                        surahNumber = surahNumber,
                        ayahNumber = ayahNumber,
                        text = generateMockTafsirText(tafsirId, surahNumber, ayahNumber),
                        footnotes = emptyList(),
                        references = emptyList()
                    )
                )
            }
        }
        
        return allTexts
    }

    /**
     * Get tafsir for a specific surah
     * MOCK IMPLEMENTATION
     */
    suspend fun getTafsirForSurah(tafsirId: String, surahNumber: Int): List<TafsirTextDto> {
        val texts = mutableListOf<TafsirTextDto>()
        val ayahCount = getAyahCountForSurah(surahNumber)
        
        for (ayahNumber in 1..ayahCount) {
            texts.add(
                TafsirTextDto(
                    tafsirId = tafsirId,
                    surahNumber = surahNumber,
                    ayahNumber = ayahNumber,
                    text = generateMockTafsirText(tafsirId, surahNumber, ayahNumber),
                    footnotes = emptyList(),
                    references = emptyList()
                )
            )
        }
        
        return texts
    }
    
    /**
     * Generate mock tafsir text for testing
     */
    private fun generateMockTafsirText(tafsirId: String, surahNumber: Int, ayahNumber: Int): String {
        val tafsirName = when {
            tafsirId.contains("kathir") -> "Ibn Kathir"
            tafsirId.contains("jalalayn") -> "Jalalayn"
            tafsirId.contains("saadi") -> "As-Sa'di"
            tafsirId.contains("maududi") -> "Maududi"
            tafsirId.contains("tabari") -> "At-Tabari"
            tafsirId.contains("tamil") -> "Tamil Commentary"
            tafsirId.contains("urdu") -> "Urdu Commentary"
            else -> "Commentary"
        }
        
        return "[$tafsirName] This is a sample commentary for Surah $surahNumber, Ayah $ayahNumber. " +
               "In a production environment, this would contain the actual tafsir text from the selected source. " +
               "The commentary would explain the meaning, context, and lessons from this verse."
    }
    
    /**
     * Helper to get ayah count for each surah
     */
    private fun getAyahCountForSurah(surahNumber: Int): Int {
        return when (surahNumber) {
            1 -> 7; 2 -> 286; 3 -> 200; 4 -> 176; 5 -> 120
            6 -> 165; 7 -> 206; 8 -> 75; 9 -> 129; 10 -> 109
            else -> 50 // Default for mock
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
            val response: TafsirAyahResponse = client.get(
                "$baseUrl/tafsirs/$tafsirId/by_ayah/$surahNumber:$ayahNumber"
            ) {
                parameter("language", "en")
            }.body()
            
            TafsirTextDto(
                tafsirId = tafsirId,
                surahNumber = surahNumber,
                ayahNumber = ayahNumber,
                text = response.tafsir.text,
                footnotes = emptyList(),
                references = emptyList()
            )
        } catch (e: Exception) {
            null
        }
    }
}

// Response DTOs (these would match the actual API structure)
@kotlinx.serialization.Serializable
private data class TafsirResponse(
    val tafsirs: List<TafsirItemDto>
)

@kotlinx.serialization.Serializable
private data class TafsirItemDto(
    @kotlinx.serialization.SerialName("verse_number")
    val verseNumber: Int,
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
