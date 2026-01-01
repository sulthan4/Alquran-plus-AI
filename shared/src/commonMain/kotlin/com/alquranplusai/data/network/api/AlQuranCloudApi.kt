package com.alquranplusai.data.network.api

import com.alquranplusai.data.network.dto.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class AlQuranCloudApi(private val client: HttpClient) {
    
    private val baseUrl = "https://api.quran.com/api/v4"

    suspend fun getChapters(): List<QuranFoundationChapterDto> {
        val response: QuranFoundationChaptersResponse = client.get("$baseUrl/chapters") {
            parameter("language", "en")
        }.body()
        return response.chapters
    }

    suspend fun getVersesByChapter(chapterId: Int): List<QuranFoundationVerseDto> {
        // Fetch all verses for the chapter
        // Note: For large chapters like Al-Baqarah, pagination might be needed. 
        // But for MVP sync, we'll try to fetch all or loop if needed. 
        // Default per_page is 10. Max is usually 50. 
        // To fetch all, we should really implement pagination loop or request logic.
        // For sync simplicity, let's request a large per_page if possible, or iterate.
        // Documentation says max per_page is 50. So we MUST loop.
        
        val allVerses = mutableListOf<QuranFoundationVerseDto>()
        var page = 1
        var totalPages = 1
        
        do {
            val response: QuranFoundationVersesResponse = client.get("$baseUrl/verses/by_chapter/$chapterId") {
                parameter("language", "en")
                parameter("words", "true")
                parameter("fields", "text_uthmani")
                parameter("word_fields", "text_uthmani")
                parameter("per_page", 50)
                parameter("page", page)
            }.body()
            
            allVerses.addAll(response.verses)
            totalPages = response.pagination?.totalPages ?: 1
            page++
        } while (page <= totalPages)
        
        return allVerses
    }
}
