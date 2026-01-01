package com.alquranplusai.domain.models

import kotlinx.serialization.Serializable

/**
 * Represents a Quran reciter
 */
@Serializable
data class Reciter(
    val id: String,
    val name: String,
    val nameArabic: String,
    val style: RecitationStyle,
    val bitrate: Int = 128,
    val format: AudioFormat = AudioFormat.MP3,
    val isDownloaded: Boolean = false,
    val downloadSize: Long = 0,
    val imageUrl: String? = null,
    val bio: String? = null,
    val country: String? = null,
    val hasWordTiming: Boolean = false
)

/**
 * Audio file for a Surah or Ayah
 */
@Serializable
data class AudioFile(
    val id: String,
    val reciterId: String,
    val surahNumber: Int,
    val ayahNumber: Int? = null,
    val url: String,
    val localPath: String? = null,
    val duration: Long = 0,
    val fileSize: Long = 0,
    val isDownloaded: Boolean = false,
    val downloadProgress: Int = 0,
    val wordTimings: List<WordTiming> = emptyList()
)

/**
 * Word-level timing for synchronized highlighting
 */
@Serializable
data class WordTiming(
    val verseNumber: Int,
    val wordPosition: Int,
    val startTime: Long,
    val endTime: Long,
    val duration: Long
)

/**
 * Playlist for organizing audio
 */
@Serializable
data class Playlist(
    val id: String,
    val name: String,
    val description: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val itemCount: Int = 0,
    val totalDuration: Long = 0,
    val coverImageUrl: String? = null,
    val isDefault: Boolean = false
)

/**
 * Item in a playlist
 */
@Serializable
data class PlaylistItem(
    val id: String,
    val playlistId: String,
    val reciterId: String,
    val surahNumber: Int,
    val ayahStart: Int? = null,
    val ayahEnd: Int? = null,
    val position: Int,
    val addedAt: Long
)

/**
 * Audio playback settings
 */
@Serializable
data class AudioSettings(
    val defaultReciterId: String? = null,
    val playbackSpeed: Float = 1.0f,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val shuffleEnabled: Boolean = false,
    val autoPlay: Boolean = true,
    val gapBetweenAyahs: Long = 1000,
    val gapBetweenSurahs: Long = 3000,
    val enableEqualizer: Boolean = false,
    val equalizerSettings: EqualizerSettings? = null,
    val sleepTimerMinutes: Int = 0,
    val wordByWordMode: Boolean = false,
    val highlightWords: Boolean = true
)

/**
 * Equalizer settings
 */
@Serializable
data class EqualizerSettings(
    val preset: EqualizerPreset = EqualizerPreset.NORMAL,
    val bands: List<EqualizerBand> = emptyList(),
    val bassBoost: Int = 0,
    val virtualizerStrength: Int = 0
)

/**
 * Equalizer band
 */
@Serializable
data class EqualizerBand(
    val frequency: Int,
    val gain: Float
)

/**
 * Playback state
 */
@Serializable
data class PlaybackState(
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0,
    val duration: Long = 0,
    val currentSurah: Int? = null,
    val currentAyah: Int? = null,
    val currentReciter: String? = null,
    val buffering: Boolean = false,
    val error: String? = null
)

/**
 * Download status for audio
 */
@Serializable
data class AudioDownloadStatus(
    val reciterId: String,
    val surahNumber: Int,
    val status: DownloadStatus,
    val progress: Int = 0,
    val downloadedBytes: Long = 0,
    val totalBytes: Long = 0,
    val error: String? = null
)

/**
 * Recitation style
 */
@Serializable
enum class RecitationStyle {
    MURATTAL,
    MUJAWWAD,
    MUALLIM,
    TRANSLATION
}

/**
 * Audio format
 */
@Serializable
enum class AudioFormat {
    MP3,
    OGG,
    M4A,
    WAV
}

/**
 * Repeat mode
 */
@Serializable
enum class RepeatMode {
    OFF,
    ONE,
    ALL,
    RANGE
}

/**
 * Audio quality
 */
@Serializable
enum class AudioQuality {
    LOW,
    MEDIUM,
    HIGH
}

/**
 * Equalizer preset
 */
@Serializable
enum class EqualizerPreset {
    NORMAL,
    CLASSICAL,
    VOCAL,
    BASS_BOOST,
    TREBLE_BOOST,
    CUSTOM
}
