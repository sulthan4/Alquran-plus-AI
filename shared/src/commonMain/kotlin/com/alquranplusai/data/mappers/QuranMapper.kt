package com.alquranplusai.data.mappers

import com.alquranplusai.data.network.dto.*
import com.alquranplusai.domain.models.*

/** Mapper for Quran-related data transformations */
class QuranMapper {

    fun mapSurahDtoToDomain(dto: SurahDto): Surah {
        return Surah(
                number = dto.number,
                name = dto.name,
                nameArabic = dto.nameArabic,
                nameTransliteration = dto.nameTransliteration,
                nameTranslation = dto.nameTransliteration, // Use transliteration as fallback
                revelationType =
                        when (dto.revelationType.uppercase()) {
                            "MECCAN" -> RevelationType.MECCAN
                            "MEDINAN" -> RevelationType.MEDINAN
                            else -> RevelationType.MECCAN
                        },
                numberOfAyahs = dto.numberOfAyahs,
                bismillahPre = dto.bismillahPre
        )
    }

    fun mapAyahDtoToDomain(dto: AyahDto): Ayah {
        return Ayah(
                id = dto.id.toLong(),
                surahNumber = dto.surahNumber,
                ayahNumber = dto.ayahNumber,
                text = dto.text,
                textUthmani = dto.text, // Use text as fallback
                textSimple = dto.textSimple ?: dto.text,
                juzNumber = dto.juzNumber,
                hizbNumber = dto.hizbNumber ?: 0,
                rukuNumber = dto.rukuNumber ?: 0,
                manzilNumber = dto.manzilNumber ?: 0,
                pageNumber = dto.pageNumber
        )
    }

    fun mapWordDtoToDomain(dto: WordDto): Word {
        return Word(
                id = dto.id.toLong(),
                ayahId = dto.ayahId.toLong(),
                position = dto.position,
                text = dto.text,
                textUthmani = dto.text, // Use text as fallback
                textSimple = dto.text,
                translation = dto.translation,
                transliteration = dto.transliteration
        )
    }
}
