package com.alquranplusai.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class TranslationDto(
    val id: Int,
    val ayahId: Int,
    val translatorId: Int,
    val text: String,
    val languageCode: String
)

@Serializable
data class TranslatorDto(
    val id: Int,
    val name: String,
    val languageCode: String,
    val languageName: String
)
