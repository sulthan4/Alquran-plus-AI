package com.alquranplusai.data

import com.alquranplusai.domain.models.Surah
import com.alquranplusai.domain.models.RevelationType

/**
 * Test Surah data for unit tests
 */
object TestSurahs {
    val alFatiha = Surah(
        number = 1,
        name = "Al-Fatihah",
        nameArabic = "الفاتحة",
        nameTransliteration = "Al-Fatihah",
        nameTranslation = "The Opening",
        revelationType = RevelationType.MECCAN,
        numberOfAyahs = 7
    )
    
    val alBaqarah = Surah(
        number = 2,
        name = "Al-Baqarah",
        nameArabic = "البقرة",
        nameTransliteration = "Al-Baqarah",
        nameTranslation = "The Cow",
        revelationType = RevelationType.MEDINAN,
        numberOfAyahs = 286
    )
    
    val allTestSurahs = listOf(alFatiha, alBaqarah)
}
