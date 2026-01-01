package com.alquranplusai.data.network.api

import com.alquranplusai.data.network.dto.SurahDto
import com.alquranplusai.data.network.dto.AyahDto
import com.alquranplusai.data.network.dto.WordDto

/**
 * API service for Quran data
 */
interface QuranApiService {
    
    suspend fun getAllSurahs(): List<SurahDto>
    
    suspend fun getSurah(number: Int): SurahDto
    
    suspend fun getAyahs(surahNumber: Int): List<AyahDto>
    
    suspend fun getAyah(surahNumber: Int, ayahNumber: Int): AyahDto
    
    suspend fun getWords(ayahId: Int): List<WordDto>
    
    suspend fun getJuz(juzNumber: Int): List<AyahDto>
    
    suspend fun getPage(pageNumber: Int): List<AyahDto>
}
