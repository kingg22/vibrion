@file:OptIn(UnofficialDeezerApi::class)

package io.github.kingg22.vibrion.di

import io.github.kingg22.deezer.client.api.DeezerApiClient
import io.github.kingg22.deezer.client.api.DeezerClientPlugin
import io.github.kingg22.deezer.client.gw.DeezerGwPlugin
import io.github.kingg22.deezer.client.utils.UnofficialDeezerApi
import io.github.kingg22.vibrion.data.PlatformHelper
import io.github.kingg22.vibrion.data.datasource.music.DeezerApiDataSource
import io.github.kingg22.vibrion.data.datasource.music.DeezerGwDataSource
import io.github.kingg22.vibrion.data.datasource.preferences.PreferencesDataSource
import io.github.kingg22.vibrion.data.repository.SearchHistoryRepositoryImpl
import io.github.kingg22.vibrion.data.repository.SearchRepositoryImpl
import io.github.kingg22.vibrion.data.repository.SettingsRepositoryImpl
import io.github.kingg22.vibrion.data.repository.TrendsRepositoryImpl
import io.github.kingg22.vibrion.data.service.DownloadServiceImpl
import io.github.kingg22.vibrion.data.service.ExoPlayerAudioPlayerService
import io.github.kingg22.vibrion.domain.repository.SearchHistoryRepository
import io.github.kingg22.vibrion.domain.repository.SearchRepository
import io.github.kingg22.vibrion.domain.repository.SettingsRepository
import io.github.kingg22.vibrion.domain.repository.TrendsRepository
import io.github.kingg22.vibrion.domain.service.AudioPlayerService
import io.github.kingg22.vibrion.domain.service.DownloadService
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.BodyProgress
import io.ktor.client.plugins.Charsets
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.LoggingFormat
import io.ktor.serialization.kotlinx.json.json
import io.sentry.ktorClient.SentryKtorClientPlugin
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.dsl.onClose
import kotlin.time.Duration.Companion.milliseconds
import co.touchlab.kermit.Logger as KermitLogger
import io.ktor.client.plugins.logging.Logger as KtorLogger

/** All data-related modules */
val dataModule = module {
    // Raw data-sources
    single { _ ->
        HttpClient(CIO) {
            install(HttpCookies)
            install(BodyProgress)
            install(ContentNegotiation) {
                json(
                    Json {
                        encodeDefaults = true
                        isLenient = true
                        allowSpecialFloatingPointValues = true
                        allowStructuredMapKeys = true

                        prettyPrint = true
                        ignoreUnknownKeys = true
                        explicitNulls = false
                        useArrayPolymorphism = true
                    },
                )
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 20.milliseconds.inWholeMilliseconds
            }
            install(HttpRequestRetry) {
                maxRetries = 3
                exponentialDelay()
            }
            install(Logging) {
                logger = object : KtorLogger {
                    override fun log(message: String) {
                        // verbose level
                        KermitLogger.v("HttpClient") { message }
                    }
                }
                format = LoggingFormat.OkHttp
            }

            Charsets {
                register(Charsets.UTF_8)
                sendCharset = Charsets.UTF_8
                responseCharsetFallback = Charsets.UTF_8
            }

            install(DeezerClientPlugin)
            install(DeezerGwPlugin)
            install(SentryKtorClientPlugin)
            install(HttpRedirect)
            install(HttpCache) {
                publicStorage(FileStorage(androidContext().cacheDir.resolve("ktor_image_cache")))
                privateStorage(FileStorage(androidContext().cacheDir.resolve("ktor_private_image_cache")))
            }
            expectSuccess = true
        }
    } onClose { httpClient -> httpClient?.close() }
    single { _ ->
        DeezerApiClient(get<HttpClient>())
    }

    // DataSources
    factory { _ -> DeezerApiDataSource(get()) }
    single { _ -> DeezerGwDataSource(get(), get(), get()) }
    factory { _ -> PreferencesDataSource(get()) }

    // Services
    factory<DownloadService> { _ -> DownloadServiceImpl(get(), get()) }
    single<AudioPlayerService> { _ -> ExoPlayerAudioPlayerService(androidContext()) }
    factory { _ -> PlatformHelper(androidContext()) }

    // Repositories
    factory<SearchHistoryRepository> { _ -> SearchHistoryRepositoryImpl(get()) }
    factory<SettingsRepository> { _ -> SettingsRepositoryImpl(get(), get()) }
    factory<SearchRepository> { _ -> SearchRepositoryImpl(get()) }
    factory<TrendsRepository> { _ -> TrendsRepositoryImpl(get()) }
}
