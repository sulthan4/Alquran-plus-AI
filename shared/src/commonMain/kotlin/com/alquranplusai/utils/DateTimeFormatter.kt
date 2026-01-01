package com.alquranplusai.utils

import kotlinx.datetime.*

/** Date and time formatting utilities */
object DateTimeFormatter {

    /** Get current timestamp in milliseconds */
    fun now(): Long = Clock.System.now().toEpochMilliseconds()

    /** Get current date string (YYYY-MM-DD) */
    fun today(): String {
        val now = Clock.System.now()
        val localDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return localDate.toString()
    }

    /** Format timestamp to readable date */
    fun formatDate(timestamp: Long): String {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return "${localDate.dayOfMonth} ${localDate.month.name.take(3)} ${localDate.year}"
    }

    /** Format timestamp to readable time */
    fun formatTime(timestamp: Long): String {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val localTime = instant.toLocalDateTime(TimeZone.currentSystemDefault()).time
        return "${localTime.hour.toString().padStart(2, '0')}:${localTime.minute.toString().padStart(2, '0')}"
    }

    /** Format timestamp to readable date and time */
    fun formatDateTime(timestamp: Long): String {
        return "${formatDate(timestamp)} ${formatTime(timestamp)}"
    }

    /** Format duration in milliseconds to readable string */
    fun formatDuration(durationMillis: Long): String {
        val seconds = (durationMillis / 1000) % 60
        val minutes = (durationMillis / (1000 * 60)) % 60
        val hours = (durationMillis / (1000 * 60 * 60))

        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            minutes > 0 -> "${minutes}m ${seconds}s"
            else -> "${seconds}s"
        }
    }

    /** Format audio duration (MM:SS or HH:MM:SS) */
    fun formatAudioDuration(durationMillis: Long): String {
        val seconds = (durationMillis / 1000) % 60
        val minutes = (durationMillis / (1000 * 60)) % 60
        val hours = (durationMillis / (1000 * 60 * 60))

        return if (hours > 0) {
            "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
        } else {
            "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
        }
    }

    /** Get relative time string (e.g., "2 hours ago", "yesterday") */
    fun getRelativeTime(timestamp: Long): String {
        val now = Clock.System.now().toEpochMilliseconds()
        val diff = now - timestamp

        return when {
            diff < 60 * 1000 -> "Just now"
            diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)} minutes ago"
            diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)} hours ago"
            diff < 7 * 24 * 60 * 60 * 1000 -> "${diff / (24 * 60 * 60 * 1000)} days ago"
            diff < 30 * 24 * 60 * 60 * 1000 -> "${diff / (7 * 24 * 60 * 60 * 1000)} weeks ago"
            else -> formatDate(timestamp)
        }
    }

    /** Check if date is today */
    fun isToday(timestamp: Long): Boolean {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        return date == today
    }

    /** Get week start date */
    fun getWeekStart(date: String = today()): String {
        val localDate = LocalDate.parse(date)
        val dayOfWeek = localDate.dayOfWeek.isoDayNumber
        val daysToSubtract = dayOfWeek - 1
        val weekStart = localDate.minus(daysToSubtract, DateTimeUnit.DAY)
        return weekStart.toString()
    }

    /** Get month start date */
    fun getMonthStart(date: String = today()): String {
        val localDate = LocalDate.parse(date)
        return LocalDate(localDate.year, localDate.month, 1).toString()
    }
}
