package com.alquranplusai.repositories

import com.alquranplusai.domain.models.*
import com.alquranplusai.domain.repositories.QuranRepository
import com.alquranplusai.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Fake implementation of QuranRepository for testing
 */
class FakeQuranRepository : QuranRepository {
    private val surahs = mutableListOf<Surah>()
    private val ayahs = mutableListOf<Ayah>()
    
    override suspend fun getAllSurahs(): Flow<List<Surah>> = flowOf(surahs)
    
    override suspend fun getSurahByNumber(number: Int): Flow<Surah?> = flowOf(surahs.find { it.number == number })
    
    override suspend fun getSurahsByRevelationType(type: RevelationType): Flow<List<Surah>> = flowOf(surahs.filter { it.revelationType == type })
    
    override suspend fun searchSurahs(query: String): Flow<List<Surah>> = flowOf(surahs.filter { it.name.contains(query, ignoreCase = true) })
    
    override suspend fun getAyahsBySurah(surahNumber: Int): Flow<List<Ayah>> = flowOf(ayahs.filter { it.surahNumber == surahNumber })
    
    override suspend fun getAyahByNumber(surahNumber: Int, ayahNumber: Int): Flow<Ayah?> = flowOf(ayahs.find { it.surahNumber == surahNumber && it.ayahNumber == ayahNumber })
    
    override suspend fun getAyahsByJuz(juzNumber: Int): Flow<List<Ayah>> = flowOf(ayahs.filter { it.juzNumber == juzNumber })
    
    // Stub other methods required by interface
    override suspend fun getAyahsByPage(pageNumber: Int): Flow<List<Ayah>> = flowOf(emptyList())
    override suspend fun getAyahsByManzil(manzilNumber: Int): Flow<List<Ayah>> = flowOf(emptyList())
    override suspend fun getAyahsByHizb(hizbNumber: Int): Flow<List<Ayah>> = flowOf(emptyList())
    override suspend fun getAyahsByRuku(surahNumber: Int, rukuNumber: Int): Flow<List<Ayah>> = flowOf(emptyList())
    override suspend fun getWordsByAyah(ayahId: Long): Flow<List<Word>> = flowOf(emptyList())
    override suspend fun getWordById(wordId: Long): Flow<Word?> = flowOf(null)
    override suspend fun searchWordsByRoot(root: String): Flow<List<Word>> = flowOf(emptyList())
    override suspend fun searchWordsByLemma(lemma: String): Flow<List<Word>> = flowOf(emptyList())
    override suspend fun getAllJuz(): Flow<List<Juz>> = flowOf(emptyList())
    override suspend fun getJuzByNumber(number: Int): Flow<Juz?> = flowOf(null)
    override suspend fun getAllPages(): Flow<List<Page>> = flowOf(emptyList())
    override suspend fun getPageByNumber(number: Int): Flow<Page?> = flowOf(null)
    override suspend fun getAllManzils(): Flow<List<Manzil>> = flowOf(emptyList())
    override suspend fun getManzilByNumber(number: Int): Flow<Manzil?> = flowOf(null)
    override suspend fun getAllHizbs(): Flow<List<Hizb>> = flowOf(emptyList())
    override suspend fun getHizbByNumber(number: Int): Flow<Hizb?> = flowOf(null)
    override suspend fun getAllSajdaAyahs(): Flow<List<SajdaInfo>> = flowOf(emptyList())
    override suspend fun saveReadingPosition(surahNumber: Int, ayahNumber: Int) {}
    override suspend fun getLastReadingPosition(): Flow<Pair<Int, Int>?> = flowOf(null)
    override suspend fun markSurahAsCompleted(surahNumber: Int) {}
    override suspend fun markJuzAsCompleted(juzNumber: Int) {}
    override suspend fun getCompletedSurahs(): Flow<List<Int>> = flowOf(emptyList())
    override suspend fun getCompletedJuz(): Flow<List<Int>> = flowOf(emptyList())

    fun addSurah(surah: Surah) { surahs.add(surah) }
    fun addAyah(ayah: Ayah) { ayahs.add(ayah) }
}
