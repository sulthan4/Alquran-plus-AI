package com.alquranplusai.data.network

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json

class QuranApiClient(
    private val httpClient: HttpClient,
    private val json: Json
) {
    private val baseUrl = "https://api.alquran.cloud/v1"
    
    suspend fun getSurah(number: Int): String {
        return httpClient.get("$baseUrl/surah/$number").bodyAsText()
    }
    
    suspend fun getAyah(surahNumber: Int, ayahNumber: Int): String {
        return httpClient.get("$baseUrl/ayah/$surahNumber:$ayahNumber").bodyAsText()
    }
    
    suspend fun getTranslation(surahNumber: Int, translationId: String): String {
        return httpClient.get("$baseUrl/surah/$surahNumber/$translationId").bodyAsText()
    }
    
    suspend fun searchQuran(query: String): String {
        return httpClient.get("$baseUrl/search/$query/all/en").bodyAsText()
    }
    
    suspend fun getReciters(): String {
        return httpClient.get("$baseUrl/edition/format/audio").bodyAsText()
    }
    
    suspend fun getAudioUrl(surahNumber: Int, reciterId: String): String {
        return "$baseUrl/surah/$surahNumber/$reciterId"
    }
}

class TranslationApiClient(
    private val httpClient: HttpClient
) {
    private val baseUrl = "https://api.alquran.cloud/v1"
    
    suspend fun getAvailableTranslations(): String {
        return httpClient.get("$baseUrl/edition/type/translation").bodyAsText()
    }
    
    suspend fun downloadTranslation(translationId: String): String {
        return httpClient.get("$baseUrl/quran/$translationId").bodyAsText()
    }
}

class TafsirApiClient(
    private val httpClient: HttpClient
) {
    private val baseUrl = "https://api.alquran.cloud/v1"
    
    suspend fun getTafsir(surahNumber: Int, ayahNumber: Int, tafsirId: String): String {
        return httpClient.get("$baseUrl/ayah/$surahNumber:$ayahNumber/$tafsirId").bodyAsText()
    }
    
    suspend fun getAvailableTafsirs(): String {
        return httpClient.get("$baseUrl/edition/type/tafsir").bodyAsText()
    }
}
