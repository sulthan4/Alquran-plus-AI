package com.alquranplusai.data.mappers

import com.alquranplusai.data.network.dto.*
import com.alquranplusai.domain.models.*

/** Mapper for Translation data */
class TranslationMapper {

    fun mapTranslationDtoToDomain(dto: TranslationDto, translatorDto: TranslatorDto): Translation {
        return Translation(
                id = dto.id.toString(),
                name = translatorDto.name,
                author = translatorDto.name,
                language = translatorDto.languageName,
                languageCode = dto.languageCode
        )
    }

    fun mapAyahTranslationDtoToDomain(
            dto: TranslationDto,
            surahNumber: Int,
            ayahNumber: Int
    ): AyahTranslation {
        return AyahTranslation(
                translationId = dto.id.toString(),
                surahNumber = surahNumber,
                ayahNumber = ayahNumber,
                text = dto.text
        )
    }
}
