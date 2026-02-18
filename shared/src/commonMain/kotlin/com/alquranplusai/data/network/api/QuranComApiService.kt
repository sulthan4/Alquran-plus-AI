package com.alquranplusai.data.network.api

import com.alquranplusai.data.network.dto.QuranComVerseResponse
import com.alquranplusai.data.network.dto.QuranComRecitersResponse
import com.alquranplusai.data.network.dto.QuranComChapterVersesResponse
import com.alquranplusai.data.network.dto.QuranComUthmaniTextResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * API Service for Quran.com API
 * Provides access to reciters list and word-level timing data
 */
interface QuranComApiService {
    /**
     * Fetch all available reciters from Quran.com
     */
    suspend fun getReciters(): Flow<QuranComRecitersResponse>
    
    /**
     * Fetch verse data with word timings for a specific reciter
     * @param recitationId Quran.com recitation ID (1-12)
     * @param surahNumber Surah number (1-114)
     * @param ayahNumber Ayah number
     */
    suspend fun getVerseWithTimings(
        recitationId: Int,
        surahNumber: Int,
        ayahNumber: Int
    ): Flow<QuranComVerseResponse>
    
    /**
     * Fetch all verses for a chapter with full metadata
     * @param chapterNumber Chapter/Surah number (1-114)
     * @param page Page number for pagination (default: 1)
     * @param perPage Number of verses per page (default: 300, max supported)
     */
    suspend fun getChapterVerses(
        chapterNumber: Int,
        page: Int = 1,
        perPage: Int = 300
    ): Flow<QuranComChapterVersesResponse>

    /**
     * Fetch Uthmani text for all verses in a chapter
     * @param chapterNumber Chapter/Surah number (1-114)
     */
    suspend fun getChapterUthmaniText(
        chapterNumber: Int
    ): Flow<QuranComUthmaniTextResponse>
}

class QuranComApiServiceImpl(
    private val client: HttpClient
) : QuranComApiService {
    
    companion object {
        private const val BASE_URL = "https://api.quran.com/api/v4"
    }
    
    override suspend fun getReciters(): Flow<QuranComRecitersResponse> = flow {
        val response = client.get("$BASE_URL/resources/recitations")
        emit(response.body())
    }
    
    override suspend fun getVerseWithTimings(
        recitationId: Int,
        surahNumber: Int,
        ayahNumber: Int
    ): Flow<QuranComVerseResponse> = flow {
        val response = client.get("$BASE_URL/verses/by_key/$surahNumber:$ayahNumber") {
            parameter("words", "true")
            parameter("audio", recitationId)
        }
        emit(response.body())
    }
    
    override suspend fun getChapterVerses(
        chapterNumber: Int,
        page: Int,
        perPage: Int
    ): Flow<QuranComChapterVersesResponse> = flow {
        val response = client.get("$BASE_URL/verses/by_chapter/$chapterNumber") {
            parameter("language", "ar")
            parameter("words", "false")  // Don't need words, just metadata
            parameter("page", page)
            parameter("per_page", perPage)
        }
        emit(response.body())
    }
    
    /**
     * Fetch Uthmani text for all verses in a chapter
     */
    override suspend fun getChapterUthmaniText(
        chapterNumber: Int
    ): Flow<QuranComUthmaniTextResponse> = flow {
        val response = client.get("$BASE_URL/quran/verses/uthmani") {
            parameter("chapter_number", chapterNumber)
        }
        emit(response.body())
    }
}
