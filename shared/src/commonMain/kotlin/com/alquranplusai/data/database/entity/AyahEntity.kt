package com.alquranplusai.data.database.entity

/**
 * Entity representing an Ayah row from the SQLDelight database.
 * Maps all 13 columns from Ayah.sq schema.
 * Note: SQLDelight generates Long for INTEGER columns.
 */
data class AyahEntity(
    val id: Long,
    val surahNumber: Long,
    val ayahNumber: Long,
    val text: String,
    val textUthmani: String,
    val textSimple: String,
    val juzNumber: Long,
    val hizbNumber: Long,
    val rukuNumber: Long,
    val manzilNumber: Long,
    val pageNumber: Long,
    val sajdaType: String?,
    val sajdaNumber: Long?
)
