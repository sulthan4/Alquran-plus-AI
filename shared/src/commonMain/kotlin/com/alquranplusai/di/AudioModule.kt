package com.alquranplusai.di

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Audio Module for dependency injection
 * Note: Audio subsystem requires platform-specific media player implementations
 */
val audioModule = module {
    // Audio components
    // AudioPlayer provided by Platform Module
    
    single { com.alquranplusai.data.audio.AudioDownloader() }
    single { com.alquranplusai.data.audio.AudioCache() }
    single { com.alquranplusai.data.audio.WordTimingProcessor() }
    single { com.alquranplusai.data.audio.EqualizerController() }
    
    // Audio utilities
    single { com.alquranplusai.data.audio.AudioUtils() }
}
