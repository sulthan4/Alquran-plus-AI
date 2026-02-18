package com.alquranplusai.data.repositories

import com.alquranplusai.data.database.AlQuranDatabaseWrapper
import com.alquranplusai.domain.models.*
import com.alquranplusai.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock

import com.alquranplusai.data.network.api.AlQuranCloudApi

class QuranRepositoryImpl(
    private val database: AlQuranDatabaseWrapper,
    private val api: AlQuranCloudApi
) : QuranRepository {

    // Helper to get current user ID (simplified for now)
    private fun getCurrentUserId(): String = "current_user"

    override suspend fun getAllSurahs(): Flow<List<Surah>> = flow {
        // Optimistically sync if current count is less than the total expected 114 surahs
        val count = database.surahQueries.count().executeAsOne()
        if (count < 114) {
            syncSurahs()
        }
        
        val surahs = database.surahQueries.selectAll().executeAsList().map { entity ->
            Surah(
                number = entity.number.toInt(),
                name = entity.name,
                nameArabic = entity.nameArabic,
                nameTransliteration = entity.nameTransliteration,
                nameTranslation = entity.nameTranslation,
                numberOfAyahs = entity.numberOfAyahs.toInt(),
                revelationType = mapRevelationType(entity.revelationType)
            )
        }
        emit(surahs)
    }

    private fun mapRevelationType(type: String?): RevelationType {
        return when (type?.lowercase()) {
            "makkah", "meccan" -> RevelationType.MECCAN
            "madinah", "medinan" -> RevelationType.MEDINAN
            else -> RevelationType.MECCAN // Default to Meccan if unknown
        }
    }

    private suspend fun syncSurahs() {
        try {
            // Sync from API
            val remoteSurahs = api.getChapters()
            remoteSurahs.forEach { dto ->
                database.surahQueries.insert(
                    number = dto.id.toLong(),
                    name = dto.nameArabic,
                    nameArabic = dto.nameArabic, 
                    nameTransliteration = dto.nameSimple,
                    nameTranslation = dto.translatedName.name ?: "",
                    revelationType = dto.revelationPlace,
                    numberOfAyahs = dto.versesCount.toLong(),
                    bismillahPre = if (dto.bismillahPre) 1 else 0,
                    rukuCount = 0 // Not provided in simple list, but can be derived later
                )
            }
        } catch (e: Exception) {
//            println("AlQuranPlusAI: Error syncing ayahs for surah $surahNumber: ${e.message}")
            e.printStackTrace()
        }
    }

    override suspend fun getSurahByNumber(number: Int): Flow<Surah?> = flow {
        var entity = database.surahQueries.selectByNumber(number.toLong()).executeAsOneOrNull()
        
        // Self-healing: if not found, check if we need to sync
        if (entity == null && database.surahQueries.count().executeAsOne() < 114) {
            syncSurahs()
            entity = database.surahQueries.selectByNumber(number.toLong()).executeAsOneOrNull()
        }
        
        emit(
            entity?.let {
                Surah(
                    number = it.number.toInt(),
                    name = it.name,
                    nameArabic = it.nameArabic,
                    nameTransliteration = it.nameTransliteration,
                    nameTranslation = it.nameTranslation,
                    numberOfAyahs = it.numberOfAyahs.toInt(),
                    revelationType = mapRevelationType(it.revelationType)
                )
            }
        )
    }

    override suspend fun getSurahsByRevelationType(type: RevelationType): Flow<List<Surah>> = flow {
        val typeString = if (type == RevelationType.MECCAN) "Meccan" else "Medinan"
        // This query uses specific strings, we might need a better approach if DB has "makkah" 
        // but syncSurahs now maps it to the API field directly.
        // Let's ensure selectByRevelationType is flexible or we map it during retrieval.
        val surahs =
                database.surahQueries.selectAll().executeAsList()
                        .filter { mapRevelationType(it.revelationType) == type }
                        .map {
                        entity ->
                    Surah(
                            number = entity.number.toInt(),
                            name = entity.name,
                            nameArabic = entity.nameArabic,
                            nameTransliteration = entity.nameTransliteration,
                            nameTranslation = entity.nameTranslation,
                            numberOfAyahs = entity.numberOfAyahs.toInt(),
                            revelationType = type
                    )
                }
        emit(surahs)
    }

    override suspend fun searchSurahs(query: String): Flow<List<Surah>> = flow {
        val surahs =
                database.surahQueries
                        .selectAll()
                        .executeAsList()
                        .filter { entity ->
                            entity.name.contains(query, ignoreCase = true) ||
                                    entity.nameTransliteration.contains(query, ignoreCase = true) ||
                                    entity.nameTranslation.contains(query, ignoreCase = true)
                        }
                        .map { entity ->
                            Surah(
                                    number = entity.number.toInt(),
                                    name = entity.name,
                                    nameArabic = entity.nameArabic,
                                    nameTransliteration = entity.nameTransliteration,
                                    nameTranslation = entity.nameTranslation,
                                    numberOfAyahs = entity.numberOfAyahs.toInt(),
                                    revelationType = mapRevelationType(entity.revelationType)
                            )
                        }
        emit(surahs)
    }

    override suspend fun getAyahsBySurah(surahNumber: Int): Flow<List<Ayah>> = flow {
        // Check if we have ayahs strictly for this surah
        val count = database.ayahQueries.selectBySurah(surahNumber.toLong()).executeAsList().size
        if (count == 0) {
            syncAyahs(surahNumber)
        }

        val ayahs =
                database.ayahQueries.selectBySurah(surahNumber.toLong()).executeAsList().map {
                        entity ->
                    val words = database.wordQueries.selectByAyah(entity.id).executeAsList().map { wordEntity ->
                        Word(
                            id = wordEntity.id,
                            ayahId = wordEntity.ayahId,
                            position = wordEntity.position.toInt(),
                            text = wordEntity.text,
                            textUthmani = wordEntity.textUthmani,
                            textSimple = wordEntity.textSimple,
                            translation = wordEntity.translation,
                            transliteration = wordEntity.transliteration,
                            root = wordEntity.root,
                            lemma = wordEntity.lemma,
                            grammar = null, // Grammar info not fully synced yet
                            occurrenceCount = wordEntity.occurrenceCount.toInt(),
                            audioUrl = wordEntity.audioUrl
                        )
                    }
                    Ayah(
                            id = entity.id,
                            surahNumber = entity.surahNumber.toInt(),
                            ayahNumber = entity.ayahNumber.toInt(),
                            text = entity.text,
                            textUthmani = entity.textUthmani,
                            textSimple = entity.textSimple,
                            juzNumber = entity.juzNumber?.toInt() ?: 1,
                            hizbNumber = entity.hizbNumber?.toInt() ?: 1,
                            rukuNumber = entity.rukuNumber?.toInt() ?: 1,
                            manzilNumber = entity.manzilNumber?.toInt() ?: 1,
                            pageNumber = entity.pageNumber?.toInt() ?: 1,
                            words = words
                    )
                }
        emit(ayahs)
    }

    private suspend fun syncAyahs(surahNumber: Int) {
        try {
            val verses = api.getVersesByChapter(surahNumber)
            println("AlQuranPlusAI: Fetched ${verses.size} verses for Surah $surahNumber")
            
            if (verses.isEmpty()) {
                 println("AlQuranPlusAI: Warning - API returned 0 verses!")
            }

            database.transaction {
                verses.forEach { verseDto ->
                    // Positional arguments to avoid named argument issues
                    database.ayahQueries.insert(
                        surahNumber.toLong(),
                        verseDto.verseNumber.toLong(),
                        verseDto.textUthmani ?: "",
                        verseDto.textUthmani ?: "", 
                        verseDto.textUthmani ?: "",
                        verseDto.juzNumber.toLong(),
                        verseDto.hizbNumber.toLong(),
                        verseDto.rukuNumber.toLong(),
                        verseDto.manzilNumber.toLong(),
                        verseDto.pageNumber.toLong(),
                        null,
                        verseDto.sajdahNumber?.toLong()
                    )

                    // Retrieve the ID of the inserted Ayah
                    val ayahId = database.ayahQueries.selectByNumber(
                        surahNumber.toLong(),
                        verseDto.verseNumber.toLong()
                    ).executeAsOne().id

                    // Insert Words
                    verseDto.words.forEach { wordDto ->
                         database.wordQueries.insert(
                            ayahId = ayahId,
                            position = (wordDto.position ?: 0).toLong(),
                            text = wordDto.textUthmani ?: wordDto.text ?: wordDto.codeV1 ?: "",
                            textUthmani = wordDto.textUthmani ?: wordDto.text ?: "",
                            textSimple = wordDto.textUthmani ?: wordDto.text ?: "",
                            translation = wordDto.translation?.name,
                            transliteration = wordDto.transliteration?.name,
                            root = null,
                            lemma = null,
                            partOfSpeech = null,
                            derivation = null,
                            mood = null,
                            wordCase = null,
                            person = null,
                            wordNumber = null,
                            gender = null,
                            verbal = null,
                            state = null,
                            aspect = null,
                            form = null,
                            voice = null,
                            occurrenceCount = 0,
                            audioUrl = wordDto.audioUrl
                        )
                    }
                }
            }
        } catch (e: Exception) {
            println("AlQuranPlusAI: Error syncing ayahs for surah $surahNumber: ${e.message}")
            e.printStackTrace()
        }
    }

    override suspend fun getAyahByNumber(surahNumber: Int, ayahNumber: Int): Flow<Ayah?> = flow {
        val entity =
                database.ayahQueries
                        .selectByNumber(surahNumber.toLong(), ayahNumber.toLong())
                        .executeAsOneOrNull()
        emit(
                entity?.let {
                    Ayah(
                            id = it.id,
                            surahNumber = it.surahNumber.toInt(),
                            ayahNumber = it.ayahNumber.toInt(),
                            text = it.text,
                            textUthmani = it.text,
                            textSimple = it.text,
                            juzNumber = it.juzNumber?.toInt() ?: 1,
                            hizbNumber = it.hizbNumber?.toInt() ?: 1,
                            rukuNumber = it.rukuNumber?.toInt() ?: 1,
                            manzilNumber = it.manzilNumber?.toInt() ?: 1,
                            pageNumber = it.pageNumber?.toInt() ?: 1
                    )
                }
        )
    }

    // Juz, Page, Manzil, Hizb queries
    override suspend fun getAyahsByJuz(juzNumber: Int): Flow<List<Ayah>> = flow {
        val ayahs =
                database.ayahQueries.selectByJuz(juzNumber.toLong()).executeAsList().map { entity ->
                    Ayah(
                            id = entity.id,
                            surahNumber = entity.surahNumber.toInt(),
                            ayahNumber = entity.ayahNumber.toInt(),
                            text = entity.text,
                            textUthmani = entity.text,
                            textSimple = entity.text,
                            juzNumber = entity.juzNumber?.toInt() ?: 1,
                            hizbNumber = entity.hizbNumber?.toInt() ?: 1,
                            rukuNumber = entity.rukuNumber?.toInt() ?: 1,
                            manzilNumber = entity.manzilNumber?.toInt() ?: 1,
                            pageNumber = entity.pageNumber?.toInt() ?: 1
                    )
                }
        emit(ayahs)
    }

    override suspend fun getAyahsByPage(pageNumber: Int): Flow<List<Ayah>> = flow {
        val ayahs =
                database.ayahQueries.selectByPage(pageNumber.toLong()).executeAsList().map { entity
                    ->
                    Ayah(
                            id = entity.id,
                            surahNumber = entity.surahNumber.toInt(),
                            ayahNumber = entity.ayahNumber.toInt(),
                            text = entity.text,
                            textUthmani = entity.text,
                            textSimple = entity.text,
                            juzNumber = entity.juzNumber?.toInt() ?: 1,
                            hizbNumber = entity.hizbNumber?.toInt() ?: 1,
                            rukuNumber = entity.rukuNumber?.toInt() ?: 1,
                            manzilNumber = entity.manzilNumber?.toInt() ?: 1,
                            pageNumber = entity.pageNumber?.toInt() ?: 1
                    )
                }
        emit(ayahs)
    }

    override suspend fun getAyahsByManzil(manzilNumber: Int): Flow<List<Ayah>> = flow {
        val ayahs =
                database.ayahQueries.selectByManzil(manzilNumber.toLong()).executeAsList().map {
                        entity ->
                    Ayah(
                            id = entity.id,
                            surahNumber = entity.surahNumber.toInt(),
                            ayahNumber = entity.ayahNumber.toInt(),
                            text = entity.text,
                            textUthmani = entity.text,
                            textSimple = entity.text,
                            juzNumber = entity.juzNumber?.toInt() ?: 1,
                            hizbNumber = entity.hizbNumber?.toInt() ?: 1,
                            rukuNumber = entity.rukuNumber?.toInt() ?: 1,
                            manzilNumber = entity.manzilNumber?.toInt() ?: 1,
                            pageNumber = entity.pageNumber?.toInt() ?: 1
                    )
                }
        emit(ayahs)
    }

    override suspend fun getAyahsByHizb(hizbNumber: Int): Flow<List<Ayah>> = flow {
        val ayahs =
                database.ayahQueries.selectByHizb(hizbNumber.toLong()).executeAsList().map { entity
                    ->
                    Ayah(
                            id = entity.id,
                            surahNumber = entity.surahNumber.toInt(),
                            ayahNumber = entity.ayahNumber.toInt(),
                            text = entity.text,
                            textUthmani = entity.text,
                            textSimple = entity.text,
                            juzNumber = entity.juzNumber?.toInt() ?: 1,
                            hizbNumber = entity.hizbNumber?.toInt() ?: 1,
                            rukuNumber = entity.rukuNumber?.toInt() ?: 1,
                            manzilNumber = entity.manzilNumber?.toInt() ?: 1,
                            pageNumber = entity.pageNumber?.toInt() ?: 1
                    )
                }
        emit(ayahs)
    }

    override suspend fun getAyahsByRuku(surahNumber: Int, rukuNumber: Int): Flow<List<Ayah>> =
            flow {
                val ayahs =
                        database.ayahQueries
                                .selectByRuku(surahNumber.toLong(), rukuNumber.toLong())
                                .executeAsList()
                                .map { entity ->
                                    Ayah(
                                            id = entity.id,
                                            surahNumber = entity.surahNumber.toInt(),
                                            ayahNumber = entity.ayahNumber.toInt(),
                                            text = entity.text,
                                            textUthmani = entity.text,
                                            textSimple = entity.text,
                                            juzNumber = entity.juzNumber?.toInt() ?: 1,
                                            hizbNumber = entity.hizbNumber?.toInt() ?: 1,
                                            rukuNumber = entity.rukuNumber?.toInt() ?: 1,
                                            manzilNumber = entity.manzilNumber?.toInt() ?: 1,
                                            pageNumber = entity.pageNumber?.toInt() ?: 1
                                    )
                                }
                emit(ayahs)
            }

    // Reading position tracking
    override suspend fun saveReadingPosition(surahNumber: Int, ayahNumber: Int) {
        database.userQueries.updateReadingPosition(
                lastSurah = surahNumber.toLong(),
                lastAyah = ayahNumber.toLong(),
                lastActiveDate = Clock.System.now().toEpochMilliseconds(),
                userId = getCurrentUserId()
        )
    }

    override suspend fun getLastReadingPosition(): Flow<Pair<Int, Int>?> = flow {
        val position = database.userQueries.getReadingPosition(getCurrentUserId()).executeAsOneOrNull()
        emit(position?.let { Pair(it.lastSurah.toInt(), it.lastAyah.toInt()) })
    }

    // Completion tracking
    override suspend fun markSurahAsCompleted(surahNumber: Int) {
        database.userQueries.markSurahComplete(
                lastActiveDate = Clock.System.now().toEpochMilliseconds(),
                userId = getCurrentUserId()
        )
    }

    override suspend fun markJuzAsCompleted(juzNumber: Int) {
        database.userQueries.markJuzComplete(
                lastActiveDate = Clock.System.now().toEpochMilliseconds(),
                userId = getCurrentUserId()
        )
    }

    override suspend fun getCompletedSurahs(): Flow<List<Int>> = flow {
        val count = database.userQueries.getCompletedSurahs(getCurrentUserId()).executeAsOneOrNull() ?: 0
        emit(List(count.toInt()) { it + 1 })
    }

    override suspend fun getCompletedJuz(): Flow<List<Int>> = flow {
        val count = database.userQueries.getCompletedJuz(getCurrentUserId()).executeAsOneOrNull() ?: 0
        emit(List(count.toInt()) { it + 1 })
    }
    override suspend fun getWordsByAyah(ayahId: Long): Flow<List<Word>> = flow { emit(emptyList()) }
    override suspend fun getWordById(wordId: Long): Flow<Word?> = flow { emit(null) }
    override suspend fun searchWordsByRoot(root: String): Flow<List<Word>> = flow {
        emit(emptyList())
    }
    override suspend fun searchWordsByLemma(lemma: String): Flow<List<Word>> = flow {
        emit(emptyList())
    }
    // Static Juz Metadata
    private val juzMetadata = listOf(
        Juz(1, 1, 1, 2, 141, 148),
        Juz(2, 2, 142, 2, 252, 111),
        Juz(3, 2, 253, 3, 92, 126),
        Juz(4, 3, 93, 4, 23, 131),
        Juz(5, 4, 24, 4, 147, 124),
        Juz(6, 4, 148, 5, 81, 110),
        Juz(7, 5, 82, 6, 110, 149),
        Juz(8, 6, 111, 7, 87, 142),
        Juz(9, 7, 88, 8, 40, 159),
        Juz(10, 8, 41, 9, 92, 127),
        Juz(11, 9, 93, 11, 5, 151),
        Juz(12, 11, 6, 12, 52, 170),
        Juz(13, 12, 53, 14, 52, 154),
        Juz(14, 15, 1, 16, 128, 227),
        Juz(15, 17, 1, 18, 74, 185),
        Juz(16, 18, 75, 20, 135, 269),
        Juz(17, 21, 1, 22, 78, 190),
        Juz(18, 23, 1, 25, 20, 202),
        Juz(19, 25, 21, 27, 55, 339),
        Juz(20, 27, 56, 29, 45, 171),
        Juz(21, 29, 46, 33, 30, 178),
        Juz(22, 33, 31, 36, 27, 169),
        Juz(23, 36, 28, 39, 31, 357),
        Juz(24, 39, 32, 41, 46, 175),
        Juz(25, 41, 47, 45, 37, 246),
        Juz(26, 46, 1, 51, 30, 195),
        Juz(27, 51, 31, 57, 29, 399),
        Juz(28, 58, 1, 66, 12, 137),
        Juz(29, 67, 1, 77, 50, 431),
        Juz(30, 78, 1, 114, 6, 564)
    )

    override suspend fun getAllJuz(): Flow<List<Juz>> = flow { emit(juzMetadata) }
    override suspend fun getJuzByNumber(number: Int): Flow<Juz?> = flow { 
        emit(juzMetadata.find { it.number == number }) 
    }
    override suspend fun getAllPages(): Flow<List<Page>> = flow { emit(emptyList()) }
    override suspend fun getPageByNumber(number: Int): Flow<Page?> = flow { emit(null) }
    override suspend fun getAllManzils(): Flow<List<Manzil>> = flow { emit(emptyList()) }
    override suspend fun getManzilByNumber(number: Int): Flow<Manzil?> = flow { emit(null) }
    override suspend fun getAllHizbs(): Flow<List<Hizb>> = flow { emit(emptyList()) }
    override suspend fun getHizbByNumber(number: Int): Flow<Hizb?> = flow { emit(null) }
    override suspend fun getAllSajdaAyahs(): Flow<List<SajdaInfo>> = flow { emit(emptyList()) }
}
