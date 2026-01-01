package com.alquranplusai.di

import com.alquranplusai.data.database.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single { DatabaseDriverFactory(androidContext()) }
    single<com.alquranplusai.data.audio.AudioPlayer> { 
        com.alquranplusai.data.audio.AndroidAudioPlayer(androidContext()) 
    }
    single { com.alquranplusai.platform.local.PreferencesManager(androidContext()) }
}
