package com.alquranplusai.domain.usecases

import com.alquranplusai.domain.models.Ayah
import com.alquranplusai.domain.models.Surah
import com.alquranplusai.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.Flow

class GetSurahWithAyahsUseCase(
    private val quranRepository: QuranRepository
) {
    suspend operator fun invoke(surahNumber: Int): Flow<Pair<Surah?, List<Ayah>>> {
        return kotlinx.coroutines.flow.combine(
            quranRepository.getSurahByNumber(surahNumber),
            quranRepository.getAyahsBySurah(surahNumber)
        ) { surah, ayahs ->
            Pair(surah, ayahs)
        }
    }
}

class GetAyahWithTranslationUseCase(
    private val quranRepository: QuranRepository
) {
    suspend operator fun invoke(
        surahNumber: Int,
        ayahNumber: Int,
        translationId: String
    ): Pair<Ayah?, String?> {
        // TODO: Implement translation fetching
        return Pair(null, null)
    }
}

class SearchQuranUseCase(
    private val quranRepository: QuranRepository
) {
    suspend operator fun invoke(
        query: String,
        surahFilter: List<Int>? = null,
        translationFilter: List<String>? = null
    ): List<com.alquranplusai.domain.models.SearchResult> {
        // TODO: Implement search functionality
        return emptyList()
    }
}

class GetReadingProgressUseCase(
    private val quranRepository: QuranRepository
) {
    suspend operator fun invoke(): Flow<ReadingProgress> {
        return kotlinx.coroutines.flow.combine(
            quranRepository.getCompletedSurahs(),
            quranRepository.getLastReadingPosition()
        ) { completed, lastRead ->
            ReadingProgress(
                completedSurahs = completed.size,
                totalSurahs = 114,
                lastReadSurah = lastRead?.first ?: 1,
                lastReadAyah = lastRead?.second ?: 1,
                percentageComplete = (completed.size * 100) / 114
            )
        }
    }
}

data class ReadingProgress(
    val completedSurahs: Int,
    val totalSurahs: Int,
    val lastReadSurah: Int,
    val lastReadAyah: Int,
    val percentageComplete: Int
)
