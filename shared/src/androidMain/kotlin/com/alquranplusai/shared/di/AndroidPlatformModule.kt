package com.alquranplusai.shared.di

import com.alquranplusai.data.audio.AndroidAudioPlayer
import com.alquranplusai.data.audio.AudioPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Android platform module
 */
val androidPlatformModule = module {
    // Android-specific dependencies
    single<AudioPlayer> { AndroidAudioPlayer(androidContext()) }
    single { com.alquranplusai.platform.local.PreferencesManager(androidContext()) }
}
