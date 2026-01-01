package com.alquranplusai.data.audio

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Word timing data model
 */
@Serializable
data class WordTiming(
    val wordId: Long,
    val startTime: Long,  // milliseconds
    val endTime: Long,    // milliseconds
    val duration: Long,   // milliseconds
    val text: String = "",
    val position: Int = 0  // word position in ayah
)

/**
 * Processes word timing data for audio synchronization
 */
class WordTimingProcessor {
    
    private val json = Json { ignoreUnknownKeys = true }
    
    /**
     * Process timings from audio URL (legacy method for compatibility)
     */
    suspend fun processTimings(audioUrl: String): Map<Int, Long> {
        // Generate approximate timings for backward compatibility
        val timings = mutableMapOf<Int, Long>()
        for (i in 0 until 100) {
            timings[i] = i * 500L // 500ms per word
        }
        return timings
    }
    
    /**
     * Get word at time (legacy method for compatibility)
     */
    fun getWordAtTime(timings: Map<Int, Long>, currentTime: Long): Int? {
        return timings.entries
            .lastOrNull { it.value <= currentTime }
            ?.key
    }
    
    /**
     * Parse word timing data from JSON
     */
    fun parseWordTimings(jsonData: String): List<WordTiming> {
        return try {
            json.decodeFromString(jsonData)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * Get word at specific audio position
     */
    fun getWordAtPosition(
        timings: List<WordTiming>,
        positionMs: Long
    ): WordTiming? {
        return timings.firstOrNull { timing ->
            positionMs >= timing.startTime && positionMs <= timing.endTime
        }
    }
    
    /**
     * Get words in time range
     */
    fun getWordsInRange(
        timings: List<WordTiming>,
        startMs: Long,
        endMs: Long
    ): List<WordTiming> {
        return timings.filter { timing ->
            (timing.startTime >= startMs && timing.startTime <= endMs) ||
            (timing.endTime >= startMs && timing.endTime <= endMs) ||
            (timing.startTime <= startMs && timing.endTime >= endMs)
        }
    }
    
    /**
     * Calculate word boundaries
     */
    fun calculateWordBoundaries(
        timings: List<WordTiming>
    ): List<Pair<Long, Long>> {
        return timings.map { it.startTime to it.endTime }
    }
    
    /**
     * Get next word timing
     */
    fun getNextWord(
        timings: List<WordTiming>,
        currentWordId: Long
    ): WordTiming? {
        val currentIndex = timings.indexOfFirst { it.wordId == currentWordId }
        return if (currentIndex >= 0 && currentIndex < timings.size - 1) {
            timings[currentIndex + 1]
        } else null
    }
    
    /**
     * Get previous word timing
     */
    fun getPreviousWord(
        timings: List<WordTiming>,
        currentWordId: Long
    ): WordTiming? {
        val currentIndex = timings.indexOfFirst { it.wordId == currentWordId }
        return if (currentIndex > 0) {
            timings[currentIndex - 1]
        } else null
    }
    
    /**
     * Validate timing data consistency
     */
    fun validateTimings(timings: List<WordTiming>): Boolean {
        if (timings.isEmpty()) return true
        
        // Check for overlaps and gaps
        for (i in 0 until timings.size - 1) {
            val current = timings[i]
            val next = timings[i + 1]
            
            // Check if current end time is before or equal to next start time
            if (current.endTime > next.startTime) {
                return false // Overlap detected
            }
        }
        
        return true
    }
    
    /**
     * Adjust timings for playback speed
     */
    fun adjustForPlaybackSpeed(
        timings: List<WordTiming>,
        speed: Float
    ): List<WordTiming> {
        return timings.map { timing ->
            timing.copy(
                startTime = (timing.startTime / speed).toLong(),
                endTime = (timing.endTime / speed).toLong(),
                duration = (timing.duration / speed).toLong()
            )
        }
    }
    
    /**
     * Get total duration from timings
     */
    fun getTotalDuration(timings: List<WordTiming>): Long {
        return timings.maxOfOrNull { it.endTime } ?: 0L
    }
}
