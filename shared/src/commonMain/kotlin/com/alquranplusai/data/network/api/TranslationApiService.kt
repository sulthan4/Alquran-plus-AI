package com.alquranplusai.data.network.api

import com.alquranplusai.data.network.dto.TranslationDto
import com.alquranplusai.data.network.dto.TranslatorDto

/**
 * API service for translations
 */
interface TranslationApiService {
    
    suspend fun getAllTranslators(): List<TranslatorDto>
    
    suspend fun getTranslator(id: Int): TranslatorDto
    
    suspend fun getTranslations(ayahId: Int, translatorId: Int): List<TranslationDto>
    
    suspend fun getTranslationsBySurah(surahNumber: Int, translatorId: Int): List<TranslationDto>
    
    suspend fun getAllTafsirs(): List<com.alquranplusai.data.network.dto.QuranFoundationTafsirResourceDto>
    
    suspend fun getTafsirByVerse(tafsirId: Int, verseKey: String): com.alquranplusai.data.network.dto.QuranFoundationTafsirDto
}
