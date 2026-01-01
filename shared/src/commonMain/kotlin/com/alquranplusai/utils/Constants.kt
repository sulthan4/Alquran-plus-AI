package com.alquranplusai.utils

object Constants {
    
    // Quran Constants
    const val TOTAL_SURAHS = 114
    const val TOTAL_AYAHS = 6236
    const val TOTAL_JUZ = 30
    const val TOTAL_PAGES = 604
    const val TOTAL_HIZBS = 60
    const val TOTAL_MANZILS = 7
    
    // Database
    const val DATABASE_NAME = "alquran.db"
    const val DATABASE_VERSION = 1
    
    // Preferences
    const val PREF_FONT_SIZE = "font_size"
    const val PREF_DEFAULT_TRANSLATION = "default_translation"
    const val PREF_DARK_MODE = "dark_mode"
    const val PREF_DEFAULT_RECITER = "default_reciter"
    const val PREF_AUTO_PLAY = "auto_play"
    const val PREF_PLAYBACK_SPEED = "playback_speed"
    const val PREF_LAST_READ_SURAH = "last_read_surah"
    const val PREF_LAST_READ_AYAH = "last_read_ayah"
    
    // Default Values
    const val DEFAULT_FONT_SIZE = 28f
    const val DEFAULT_TRANSLATION = "en_saheeh"
    const val DEFAULT_RECITER = "abdul_basit"
    const val DEFAULT_PLAYBACK_SPEED = 1.0f
    
    // API
    const val API_BASE_URL = "https://api.alquran.cloud/v1/"
    const val API_TIMEOUT = 30000L
    
    // Audio
    const val AUDIO_CACHE_SIZE = 100 * 1024 * 1024L // 100 MB
    const val AUDIO_FORMAT = "mp3"
    
    // Notifications
    const val NOTIFICATION_CHANNEL_REMINDERS = "reminders"
    const val NOTIFICATION_CHANNEL_DOWNLOADS = "downloads"
    const val NOTIFICATION_CHANNEL_AUDIO = "audio_playback"
    
    // Worker Tags
    const val WORKER_TAG_SYNC = "sync"
    const val WORKER_TAG_BACKUP = "backup"
    const val WORKER_TAG_ANALYTICS = "analytics"
    const val WORKER_TAG_REMINDER = "reminder"
    
    // Deep Links
    const val DEEP_LINK_SCHEME = "alquran"
    const val DEEP_LINK_HOST = "app"
}

object ErrorMessages {
    const val NETWORK_ERROR = "Network connection failed"
    const val DATABASE_ERROR = "Database operation failed"
    const val INVALID_SURAH = "Invalid surah number"
    const val INVALID_AYAH = "Invalid ayah number"
    const val AUDIO_PLAYBACK_ERROR = "Audio playback failed"
    const val DOWNLOAD_ERROR = "Download failed"
    const val SYNC_ERROR = "Synchronization failed"
    const val UNKNOWN_ERROR = "An unknown error occurred"
}

object SuccessMessages {
    const val BOOKMARK_ADDED = "Bookmark added successfully"
    const val BOOKMARK_REMOVED = "Bookmark removed"
    const val FOLDER_CREATED = "Folder created"
    const val QUIZ_COMPLETED = "Quiz completed!"
    const val SYNC_COMPLETED = "Sync completed successfully"
    const val BACKUP_COMPLETED = "Backup completed"
}
