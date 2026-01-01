package com.alquranplusai.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class BookmarkDto(
    val id: Int,
    val userId: Int,
    val surahNumber: Int,
    val ayahNumber: Int,
    val folderId: Int? = null,
    val note: String? = null,
    val tags: List<String> = emptyList(),
    val createdAt: Long,
    val updatedAt: Long
)

@Serializable
data class FolderDto(
    val id: Int,
    val userId: Int,
    val name: String,
    val color: String? = null,
    val parentId: Int? = null,
    val createdAt: Long
)

@Serializable
data class NoteDto(
    val id: Int,
    val bookmarkId: Int,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long
)
