package com.alquranplusai.data

import com.alquranplusai.domain.models.*

/**
 * Test data for unit tests
 */
object TestData {
    val testSurah = Surah(
        number = 1,
        name = "Al-Fatihah",
        nameArabic = "الفاتحة",
        nameTransliteration = "Al-Fatihah",
        nameTranslation = "The Opening",
        revelationType = RevelationType.MECCAN,
        numberOfAyahs = 7
    )
    
    val testAyah = Ayah(
        id = 1,
        surahNumber = 1,
        ayahNumber = 1,
        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
        textUthmani = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
        textSimple = "بسم الله الرحمن الرحيم",
        juzNumber = 1,
        hizbNumber = 1,
        rukuNumber = 1,
        manzilNumber = 1,
        pageNumber = 1
    )
    
    val testBookmark = Bookmark(
        id = "1",
        surahNumber = 1,
        ayahNumber = 1,
        note = "Test bookmark",
        createdAt = 0L,
        updatedAt = 0L,
        folderId = null
    )
    
    val testUser = User(
        id = "test-user-1",
        email = "test@example.com",
        username = "testuser",
        profile = UserProfile("Test User"),
        preferences = UserPreferences(),
        createdAt = 0L,
        updatedAt = 0L
    )
}
