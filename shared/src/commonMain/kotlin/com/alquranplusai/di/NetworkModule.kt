package com.alquranplusai.di

import com.alquranplusai.data.network.QuranApiClient
import com.alquranplusai.data.network.TranslationApiClient
import com.alquranplusai.data.network.TafsirApiClient
import com.alquranplusai.data.network.api.TafsirApiService
import com.alquranplusai.data.network.api.TranslationApiService
import com.alquranplusai.data.network.api.TranslationApiServiceImpl
import com.alquranplusai.data.network.api.QuranComApiService
import com.alquranplusai.data.network.api.QuranComApiServiceImpl
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

/**
 * Network module for HTTP client and API services.
 * Includes retry, timeout, and auth header plugins.
 */
val networkModule = module {
    // JSON configuration
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
            prettyPrint = false // Disable in production for performance
        }
    }

    // HTTP Client with retry, timeout, and auth header
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(json = get())
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 30_000
            }
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)
                retryOnException(maxRetries = 3, retryOnTimeout = true)
                exponentialDelay(base = 2.0, maxDelayMs = 10_000)
            }
            // Auth header plugin — adds Bearer token when available
            install(DefaultRequest) {
                // Quran.com API does not require auth for public endpoints.
                // If a token is needed in future, inject it here:
                // header("Authorization", "Bearer $token")
                header("Accept", "application/json")
                header("User-Agent", "AlQuranPlusAI/1.0 (Android)")
            }
        }
    }

    // API Clients
    single { QuranApiClient(get(), get()) }
    single { TranslationApiClient(get()) }
    single { TafsirApiClient(get()) }
    single { com.alquranplusai.data.network.api.AlQuranCloudApi(get()) }
    single<com.alquranplusai.data.network.api.AudioApiService> {
        com.alquranplusai.data.network.api.AudioApiServiceImpl(get())
    }
    single<TranslationApiService> { TranslationApiServiceImpl(get()) }

    // Quran.com API service
    single<QuranComApiService> { QuranComApiServiceImpl(get()) }
    single { com.alquranplusai.data.network.api.TafsirApiService(get()) }
    single<com.alquranplusai.data.network.api.SearchApiService> {
        com.alquranplusai.data.network.api.SearchApiServiceImpl(get())
    }
}
