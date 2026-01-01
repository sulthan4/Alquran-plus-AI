package com.alquranplusai.domain.repositories

import com.alquranplusai.domain.models.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository for Quran data operations
 */
interface QuranRepository {
    // Surah operations
    suspend fun getAllSurahs(): Flow<List<Surah>>
    suspend fun getSurahByNumber(number: Int): Flow<Surah?>
    suspend fun getSurahsByRevelationType(type: RevelationType): Flow<List<Surah>>
    suspend fun searchSurahs(query: String): Flow<List<Surah>>
    
    // Ayah operations
    suspend fun getAyahsBySurah(surahNumber: Int): Flow<List<Ayah>>
    suspend fun getAyahByNumber(surahNumber: Int, ayahNumber: Int): Flow<Ayah?>
    suspend fun getAyahsByJuz(juzNumber: Int): Flow<List<Ayah>>
    suspend fun getAyahsByPage(pageNumber: Int): Flow<List<Ayah>>
    suspend fun getAyahsByManzil(manzilNumber: Int): Flow<List<Ayah>>
    suspend fun getAyahsByHizb(hizbNumber: Int): Flow<List<Ayah>>
    suspend fun getAyahsByRuku(surahNumber: Int, rukuNumber: Int): Flow<List<Ayah>>
    
    // Word operations
    suspend fun getWordsByAyah(ayahId: Long): Flow<List<Word>>
    suspend fun getWordById(wordId: Long): Flow<Word?>
    suspend fun searchWordsByRoot(root: String): Flow<List<Word>>
    suspend fun searchWordsByLemma(lemma: String): Flow<List<Word>>
    
    // Juz, Page, Manzil operations
    suspend fun getAllJuz(): Flow<List<Juz>>
    suspend fun getJuzByNumber(number: Int): Flow<Juz?>
    suspend fun getAllPages(): Flow<List<Page>>
    suspend fun getPageByNumber(number: Int): Flow<Page?>
    suspend fun getAllManzils(): Flow<List<Manzil>>
    suspend fun getManzilByNumber(number: Int): Flow<Manzil?>
    suspend fun getAllHizbs(): Flow<List<Hizb>>
    suspend fun getHizbByNumber(number: Int): Flow<Hizb?>
    
    // Sajda operations
    suspend fun getAllSajdaAyahs(): Flow<List<SajdaInfo>>
    
    // Reading progress
    suspend fun saveReadingPosition(surahNumber: Int, ayahNumber: Int)
    suspend fun getLastReadingPosition(): Flow<Pair<Int, Int>?>
    suspend fun markSurahAsCompleted(surahNumber: Int)
    suspend fun markJuzAsCompleted(juzNumber: Int)
    suspend fun getCompletedSurahs(): Flow<List<Int>>
    suspend fun getCompletedJuz(): Flow<List<Int>>
}
