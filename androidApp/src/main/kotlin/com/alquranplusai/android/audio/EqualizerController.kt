package com.alquranplusai.android.audio

import android.media.audiofx.Equalizer

/**
 * Equalizer Controller for audio effects
 */
class EqualizerController(private val audioSessionId: Int) {
    
    private var equalizer: Equalizer? = null
    
    init {
        try {
            equalizer = Equalizer(0, audioSessionId).apply {
                enabled = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * Get number of bands
     */
    fun getNumberOfBands(): Short = equalizer?.numberOfBands ?: 5
    
    /**
     * Get band frequency range
     */
    fun getBandFrequencyRange(band: Short): IntArray? {
        return equalizer?.getBandFreqRange(band)
    }
    
    /**
     * Get center frequency for band
     */
    fun getCenterFrequency(band: Short): Int {
        return equalizer?.getCenterFreq(band) ?: 0
    }
    
    /**
     * Get band level range
     */
    fun getBandLevelRange(): ShortArray {
        return equalizer?.bandLevelRange ?: shortArrayOf(-1500, 1500)
    }
    
    /**
     * Set band level
     */
    fun setBandLevel(band: Short, level: Short) {
        equalizer?.setBandLevel(band, level)
    }
    
    /**
     * Get band level
     */
    fun getBandLevel(band: Short): Short {
        return equalizer?.getBandLevel(band) ?: 0
    }
    
    /**
     * Get all band levels
     */
    fun getAllBandLevels(): List<Short> {
        val numBands = getNumberOfBands()
        return (0 until numBands).map { getBandLevel(it.toShort()) }
    }
    
    /**
     * Set all band levels
     */
    fun setAllBandLevels(levels: List<Short>) {
        levels.forEachIndexed { index, level ->
            setBandLevel(index.toShort(), level)
        }
    }
    
    /**
     * Reset to flat
     */
    fun reset() {
        val numBands = getNumberOfBands()
        for (i in 0 until numBands) {
            setBandLevel(i.toShort(), 0)
        }
    }
    
    /**
     * Apply preset
     */
    fun applyPreset(preset: EqualizerPreset) {
        setAllBandLevels(preset.levels)
    }
    
    /**
     * Enable/disable equalizer
     */
    fun setEnabled(enabled: Boolean) {
        equalizer?.enabled = enabled
    }
    
    /**
     * Check if enabled
     */
    fun isEnabled(): Boolean = equalizer?.enabled ?: false
    
    /**
     * Release resources
     */
    fun release() {
        equalizer?.release()
        equalizer = null
    }
    
    data class EqualizerPreset(
        val name: String,
        val levels: List<Short>
    )
    
    companion object {
        val PRESETS = listOf(
            EqualizerPreset("Flat", listOf(0, 0, 0, 0, 0)),
            EqualizerPreset("Bass Boost", listOf(800, 600, 200, 0, 0)),
            EqualizerPreset("Treble Boost", listOf(0, 0, 200, 600, 800)),
            EqualizerPreset("Vocal", listOf(-200, 200, 400, 400, 200)),
            EqualizerPreset("Classical", listOf(400, 300, 0, 300, 400))
        )
    }
}
