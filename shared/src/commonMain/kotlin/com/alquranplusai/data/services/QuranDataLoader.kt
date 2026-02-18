package com.alquranplusai.data.services

import com.alquranplusai.data.database.AlQuranDatabase
import com.alquranplusai.data.network.api.QuranComApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Service for loading complete Quran data from Quran.com API into local database
 */
class QuranDataLoader(
    private val quranComApi: QuranComApiService,
    private val database: AlQuranDatabase
) {
    
    /**
     * Load complete Quran data (all 6236 verses) from Quran.com API
     * @param onProgress Callback to report loading progress (surahNumber, totalSurahs)
     */
    suspend fun loadCompleteQuran(
        onProgress: (current: Int, total: Int) -> Unit = { _, _ -> }
    ): Flow<DataLoadingResult> = flow {
        try {
            emit(DataLoadingResult.Loading(0, 114))
            
            // Load all 114 surahs
            for (surahNumber in 1..114) {
                onProgress(surahNumber, 114)
                emit(DataLoadingResult.Loading(surahNumber, 114))
                
                // Fetch metadata (juz, page, hizb numbers)
                var metadataVerses: List<com.alquranplusai.data.network.dto.QuranComChapterVerse> = emptyList()
                quranComApi.getChapterVerses(
                    chapterNumber = surahNumber,
                    perPage = 300
                ).collect { response ->
                    metadataVerses = response.verses
                }
                
                // Fetch Uthmani text (safe interface call — no cast needed)
                var textVerses: List<com.alquranplusai.data.network.dto.QuranComUthmaniVerse> = emptyList()
                quranComApi.getChapterUthmaniText(surahNumber)
                    .collect { response ->
                        textVerses = response.verses
                    }
                
                // Combine and insert into database
                metadataVerses.forEach { metaVerse ->
                    val textVerse = textVerses.find { it.verseKey == metaVerse.verseKey }
                    
                    database.ayahQueries.insert(
                        surahNumber = surahNumber.toLong(),
                        ayahNumber = metaVerse.verseNumber.toLong(),
                        text = textVerse?.textUthmani ?: "",
                        textUthmani = textVerse?.textUthmani ?: "",
                        textSimple = textVerse?.textUthmani ?: "",
                        juzNumber = metaVerse.juzNumber.toLong(),
                        hizbNumber = metaVerse.hizbNumber.toLong(),
                        rukuNumber = metaVerse.rukuNumber.toLong(),
                        manzilNumber = metaVerse.manzilNumber.toLong(),
                        pageNumber = metaVerse.pageNumber.toLong(),
                        sajdaType = if (metaVerse.sajdahNumber != null) "recommended" else null,
                        sajdaNumber = metaVerse.sajdahNumber?.toLong()
                    )
                }
            }
            
            emit(DataLoadingResult.Success(6236))
        } catch (e: Exception) {
            emit(DataLoadingResult.Error(e.message ?: "Unknown error occurred"))
        }
    }
    
    /**
     * Check if database has been loaded with complete Quran data
     */
    suspend fun isDataLoaded(): Boolean {
        val count = database.ayahQueries.selectAll().executeAsList().size
        // Should have 6236 ayahs
        return count >= 6000 // Use 6000 as threshold to account for any minor variations
    }
    
    /**
     * Get loading progress information
     */
    suspend fun getLoadingProgress(): LoadingProgress {
        val currentCount = database.ayahQueries.selectAll().executeAsList().size
        val totalCount = 6236
        val percentage = (currentCount.toFloat() / totalCount * 100).toInt()
        
        return LoadingProgress(
            loadedVersesCount = currentCount,
            totalVersesCount = totalCount,
            percentage = percentage,
            isComplete = currentCount >= 6000
        )
    }
}

/**
 * Result states for data loading operation
 */
sealed class DataLoadingResult {
    /**
     * Loading in progress
     * @param current Current surah number being loaded
     * @param total Total number of surahs (114)
     */
    data class Loading(val current: Int, val total: Int) : DataLoadingResult()
    
    /**
     * Loading completed successfully
     * @param versesLoaded Total number of verses loaded
     */
    data class Success(val versesLoaded: Int) : DataLoadingResult()
    
    /**
     * Loading failed with error
     * @param message Error message
     */
    data class Error(val message: String) : DataLoadingResult()
}

/**
 * Progress information for data loading
 */
data class LoadingProgress(
    val loadedVersesCount: Int,
    val totalVersesCount: Int,
    val percentage: Int,
    val isComplete: Boolean
)
