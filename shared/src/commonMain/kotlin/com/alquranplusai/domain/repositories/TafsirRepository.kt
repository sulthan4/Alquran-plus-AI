package com.alquranplusai.domain.repositories

import com.alquranplusai.domain.models.Tafsir
import com.alquranplusai.domain.models.TafsirText
import com.alquranplusai.domain.models.TafsirMetadata
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing Tafsir (Quranic commentary) data
 */
interface TafsirRepository {
    
    /**
     * Get all available tafsir sources
     */
    suspend fun getAllTafsirs(): Flow<List<Tafsir>>
    
    /**
     * Get a specific tafsir by ID
     */
    suspend fun getTafsirById(tafsirId: String): Flow<Tafsir?>
    
    /**
     * Get downloaded tafsirs only
     */
    suspend fun getDownloadedTafsirs(): Flow<List<Tafsir>>
    
    /**
     * Get tafsir text for a specific ayah
     * @param tafsirId The tafsir source ID
     * @param surahNumber The surah number (1-114)
     * @param ayahNumber The ayah number within the surah
     */
    suspend fun getTafsirForAyah(
        tafsirId: String,
        surahNumber: Int,
        ayahNumber: Int
    ): Flow<TafsirText?>
    
    /**
     * Get tafsir texts for multiple ayahs (e.g., a range)
     * @param tafsirId The tafsir source ID
     * @param surahNumber The surah number
     * @param fromAyah Starting ayah number
     * @param toAyah Ending ayah number
     */
    suspend fun getTafsirForAyahRange(
        tafsirId: String,
        surahNumber: Int,
        fromAyah: Int,
        toAyah: Int
    ): Flow<List<TafsirText>>
    
    /**
     * Get tafsir for an entire surah
     */
    suspend fun getTafsirForSurah(
        tafsirId: String,
        surahNumber: Int
    ): Flow<List<TafsirText>>
    
    /**
     * Download a tafsir source
     * @param tafsirId The tafsir to download
     * @return Flow emitting download progress (0.0 to 1.0)
     */
    suspend fun downloadTafsir(tafsirId: String): Flow<Float>
    
    /**
     * Delete a downloaded tafsir
     */
    suspend fun deleteTafsir(tafsirId: String): Boolean
    
    /**
     * Get download metadata for a tafsir
     */
    suspend fun getTafsirMetadata(tafsirId: String): Flow<TafsirMetadata?>
    
    /**
     * Search within tafsir text
     */
    suspend fun searchTafsir(
        tafsirId: String,
        query: String
    ): Flow<List<TafsirText>>
    
    /**
     * Get user's preferred tafsir sources
     */
    suspend fun getPreferredTafsirs(): Flow<List<String>>
    
    /**
     * Set user's preferred tafsir sources
     */
    suspend fun setPreferredTafsirs(tafsirIds: List<String>)
}
