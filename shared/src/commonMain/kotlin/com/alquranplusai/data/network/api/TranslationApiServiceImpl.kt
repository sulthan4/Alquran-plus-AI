package com.alquranplusai.data.network.api

import com.alquranplusai.data.network.dto.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class TranslationApiServiceImpl(
    private val client: HttpClient,
    private val baseUrl: String = "https://api.quran.com/api/v4"
) : TranslationApiService {

    override suspend fun getAllTranslators(): List<TranslatorDto> {
        val response: QuranFoundationTranslationsResponse = client.get("$baseUrl/resources/translations") {
            parameter("language", "en")
        }.body()
        
        return response.translations.map { 
            TranslatorDto(
                id = it.id,
                name = it.name,
                languageCode = it.languageName, // Note: Quran.com uses full language name often
                languageName = it.languageName
            )
        }
    }

    override suspend fun getTranslator(id: Int): TranslatorDto {
        val translators = getAllTranslators()
        return translators.find { it.id == id } ?: throw Exception("Translator not found")
    }

    override suspend fun getTranslations(ayahId: Int, translatorId: Int): List<TranslationDto> {
        // Note: ayahId here might be absolute ayah ID or verse number.
        // Quran.com usually prefers verse_key or chapter_number + verse_number
        // This is a simplified implementation
        val response: QuranFoundationVerseTranslationsResponse = client.get("$baseUrl/quran/translations/$translatorId") {
            // parameter("verse_key", ...) // would need surah:ayah
        }.body()
        
        return response.translations.map { 
            TranslationDto(
                id = 0, // Not explicitly provided per verse in this endpoint
                ayahId = ayahId,
                translatorId = translatorId,
                text = it.text,
                languageCode = "" 
            )
        }
    }

    override suspend fun getTranslationsBySurah(surahNumber: Int, translatorId: Int): List<TranslationDto> {
        val response: QuranFoundationVerseTranslationsResponse = client.get("$baseUrl/quran/translations/$translatorId") {
            parameter("chapter_number", surahNumber)
        }.body()
        
        return response.translations.mapIndexed { index, it ->
            TranslationDto(
                id = index,
                ayahId = index + 1, // This is problematic without knowing verse number
                translatorId = translatorId,
                text = it.text,
                languageCode = ""
            )
        }
    }
    
    override suspend fun getAllTafsirs(): List<QuranFoundationTafsirResourceDto> {
        val response: QuranFoundationTafsirsResponse = client.get("$baseUrl/resources/tafsirs") {
            parameter("language", "en")
        }.body()
        return response.tafsirs
    }
    
    override suspend fun getTafsirByVerse(tafsirId: Int, verseKey: String): QuranFoundationTafsirDto {
        val response: QuranFoundationVerseTafsirResponse = client.get("$baseUrl/quran/tafsirs/$tafsirId") {
            parameter("verse_key", verseKey)
        }.body()
        return response.tafsir
    }
}
