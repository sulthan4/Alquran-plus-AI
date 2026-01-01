package com.alquranplusai.domain.models

import kotlinx.serialization.Serializable

/**
 * Represents a bookmark
 */
@Serializable
data class Bookmark(
    val id: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val folderId: String? = null,
    val title: String? = null,
    val note: String? = null,
    val tags: List<String> = emptyList(),
    val category: BookmarkCategory = BookmarkCategory.GENERAL,
    val priority: BookmarkPriority = BookmarkPriority.NORMAL,
    val color: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val lastAccessedAt: Long? = null,
    val accessCount: Int = 0,
    val hasReminder: Boolean = false
)

/**
 * Bookmark folder for organization
 */
@Serializable
data class BookmarkFolder(
    val id: String,
    val name: String,
    val description: String? = null,
    val parentId: String? = null,
    val color: String? = null,
    val icon: String? = null,
    val position: Int = 0,
    val bookmarkCount: Int = 0,
    val createdAt: Long,
    val updatedAt: Long
)

/**
 * Reminder associated with a bookmark
 */
@Serializable
data class BookmarkReminder(
    val id: String,
    val bookmarkId: String,
    val title: String,
    val message: String? = null,
    val reminderTime: Long,
    val repeatType: ReminderRepeatType = ReminderRepeatType.ONCE,
    val isEnabled: Boolean = true,
    val lastTriggered: Long? = null,
    val createdAt: Long
)

/**
 * Tag for categorizing bookmarks
 */
@Serializable
data class BookmarkTag(
    val id: String,
    val name: String,
    val color: String? = null,
    val usageCount: Int = 0
)

/**
 * Note attached to a verse
 */
@Serializable
data class Note(
    val id: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val title: String? = null,
    val content: String,
    val tags: List<String> = emptyList(),
    val isPrivate: Boolean = true,
    val createdAt: Long,
    val updatedAt: Long
)

/**
 * Quick collection for fast access
 */
@Serializable
data class QuickCollection(
    val id: String,
    val name: String,
    val icon: String,
    val bookmarks: List<String> = emptyList(),
    val position: Int = 0
)

/**
 * Bookmark category
 */
@Serializable
enum class BookmarkCategory {
    GENERAL,
    FAVORITE,
    TO_MEMORIZE,
    TO_STUDY,
    REFLECTION,
    DUA,
    STORY,
    RULING,
    REMINDER,
    CUSTOM
}

/**
 * Bookmark priority
 */
@Serializable
enum class BookmarkPriority {
    LOW,
    NORMAL,
    HIGH,
    URGENT
}

/**
 * Reminder repeat type
 */
@Serializable
enum class ReminderRepeatType {
    ONCE,
    DAILY,
    WEEKLY,
    MONTHLY,
    CUSTOM
}

/**
 * Bookmark export format
 */
@Serializable
enum class BookmarkExportFormat {
    JSON,
    CSV,
    TEXT,
    PDF
}
