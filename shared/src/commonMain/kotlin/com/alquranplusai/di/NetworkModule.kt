package com.alquranplusai.di

import com.alquranplusai.data.network.QuranApiClient
import com.alquranplusai.data.network.TranslationApiClient
import com.alquranplusai.data.network.TafsirApiClient
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

/**
 * Network module for HTTP client and API services
 */
val networkModule = module {
    // JSON configuration
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
            prettyPrint = true
        }
    }
    
    // HTTP Client
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(json = get())
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO
            }
        }
    }
    
    // Audio Repository

    
    // API Clients
    single { QuranApiClient(get(), get()) }
    single { TranslationApiClient(get()) }
    single { TafsirApiClient(get()) }
    single { com.alquranplusai.data.network.api.AlQuranCloudApi(get()) }
    single<com.alquranplusai.data.network.api.AudioApiService> { 
        com.alquranplusai.data.network.api.AudioApiServiceImpl(get()) 
    }
    single<com.alquranplusai.data.network.api.TranslationApiService> { 
        com.alquranplusai.data.network.api.TranslationApiServiceImpl(get()) 
    }
    single { com.alquranplusai.data.network.api.TafsirApiService(get()) }
    single<com.alquranplusai.data.network.api.SearchApiService> { 
        com.alquranplusai.data.network.api.SearchApiServiceImpl(get()) 
    }
}
