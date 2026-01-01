package com.alquranplusai.data.repositories

import com.alquranplusai.domain.models.Tafsir
import com.alquranplusai.domain.models.TafsirText
import com.alquranplusai.domain.repositories.TafsirRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Fake TafsirRepository for testing
 */
class FakeTafsirRepository : TafsirRepository {
    
    private val tafsirs = mutableListOf<Tafsir>()
    private val tafsirTexts = mutableMapOf<String, MutableList<TafsirText>>()
    private val preferredIds = mutableSetOf<String>()
    
    override suspend fun getAllTafsirs(): Flow<List<Tafsir>> = flowOf(tafsirs)
    
    override suspend fun getTafsirById(tafsirId: String): Flow<Tafsir?> = 
        flowOf(tafsirs.find { it.id == tafsirId })
    
    override suspend fun getDownloadedTafsirs(): Flow<List<Tafsir>> = 
        flowOf(tafsirs.filter { it.isDownloaded })
    
    override suspend fun getPreferredTafsirs(): Flow<List<String>> = 
        flowOf(preferredIds.toList())
    
    override suspend fun setPreferredTafsirs(tafsirIds: List<String>) {
        preferredIds.clear()
        preferredIds.addAll(tafsirIds)
    }

    override suspend fun getTafsirForAyah(tafsirId: String, surahNumber: Int, ayahNumber: Int): Flow<TafsirText?> =
        flowOf(tafsirTexts[tafsirId]?.find { it.surahNumber == surahNumber && it.ayahNumber == ayahNumber })
    
    override suspend fun getTafsirForAyahRange(tafsirId: String, surahNumber: Int, fromAyah: Int, toAyah: Int): Flow<List<TafsirText>> =
        flowOf(tafsirTexts[tafsirId]?.filter { 
            it.surahNumber == surahNumber && it.ayahNumber >= fromAyah && it.ayahNumber <= toAyah 
        } ?: emptyList())
        
    override suspend fun getTafsirForSurah(tafsirId: String, surahNumber: Int): Flow<List<TafsirText>> =
        flowOf(tafsirTexts[tafsirId]?.filter { it.surahNumber == surahNumber } ?: emptyList())
    
    override suspend fun searchTafsir(tafsirId: String, query: String): Flow<List<TafsirText>> =
        flowOf(tafsirTexts[tafsirId]?.filter { 
            it.text.contains(query, ignoreCase = true) 
        } ?: emptyList())
    
    override suspend fun downloadTafsir(tafsirId: String): Flow<Float> = flowOf(1.0f)
    
    override suspend fun deleteTafsir(tafsirId: String): Boolean {
        val removed = tafsirs.removeAll { it.id == tafsirId }
        tafsirTexts.remove(tafsirId)
        return removed
    }
    
    override suspend fun getTafsirMetadata(tafsirId: String): Flow<com.alquranplusai.domain.models.TafsirMetadata?> = 
        flowOf(null) // Mock

    // Helper methods for testing
    fun addTafsir(tafsir: Tafsir) {
        tafsirs.add(tafsir)
    }
    
    fun addTafsirText(text: TafsirText) {
        tafsirTexts.getOrPut(text.tafsirId) { mutableListOf() }.add(text)
    }
}
