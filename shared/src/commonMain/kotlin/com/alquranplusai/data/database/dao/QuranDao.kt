package com.alquranplusai.data.database.dao

import com.alquranplusai.data.database.entity.QuranEntity

/**
 * DAO for Quran metadata operations
 */
interface QuranDao {
    suspend fun getQuranMetadata(): QuranEntity?
    suspend fun insertQuranMetadata(quran: QuranEntity)
    suspend fun updateQuranMetadata(quran: QuranEntity)
    suspend fun getTotalSurahs(): Int
    suspend fun getTotalAyahs(): Int
    suspend fun getTotalJuz(): Int
    suspend fun getTotalManzils(): Int
    suspend fun getTotalHizbs(): Int
    suspend fun getTotalRukus(): Int
}
