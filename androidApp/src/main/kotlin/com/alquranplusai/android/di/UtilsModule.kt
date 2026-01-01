package com.alquranplusai.android.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val utilsModule = module {
    single { com.alquranplusai.data.ai.SpeechRecognitionEngine(androidContext()) }
    single { com.alquranplusai.data.ai.TFLiteInterpreter(androidContext(), "quran_model.tflite") }
    single { com.alquranplusai.android.utils.AnalyticsTracker(androidContext(), get()) }
}

