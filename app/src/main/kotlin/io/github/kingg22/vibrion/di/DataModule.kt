@file:OptIn(ExperimentalDeezerClient::class, ExperimentalUuidApi::class, InternalDeezerClient::class)

package io.github.kingg22.vibrion.di

import io.github.kingg22.deezer.client.api.DeezerApiClient
import io.github.kingg22.deezer.client.utils.ExperimentalDeezerClient
import io.github.kingg22.deezer.client.utils.HttpClientBuilder
import io.github.kingg22.deezer.client.utils.InternalDeezerClient
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
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.sentry.ktorClient.SentryKtorClientPlugin
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.dsl.onClose
import kotlin.uuid.ExperimentalUuidApi
import co.touchlab.kermit.Logger as KermitLogger
import io.ktor.client.plugins.logging.Logger as KtorLogger

/** All data-related modules */
val dataModule = module {
    // Raw data-sources
    factory { (name: String) ->
        HttpClientBuilder()
            .httpEngine(CIO.create())
            .addCustomConfig {
                install(SentryKtorClientPlugin)
                install(HttpRedirect)
                install(HttpCache) {
                    publicStorage(FileStorage(androidContext().cacheDir.resolve("ktor_image_cache")))
                    privateStorage(FileStorage(androidContext().cacheDir.resolve("ktor_private_image_cache")))
                }
                expectSuccess = true
            }
            .apply {
                logger = object : KtorLogger {
                    override fun log(message: String) {
                        // verbose level
                        KermitLogger.v("HttpClient $name") { message }
                    }
                }
            }
    }
    single { _ ->
        DeezerApiClient.initialize(get { parametersOf("D API") })
    } onClose { deezerApiClient -> deezerApiClient?.httpClient?.close() }

    // DataSources
    factory { _ -> DeezerApiDataSource(get()) }
    single { _ -> DeezerGwDataSource(get { parametersOf("D GW API") }, get(), get()) }
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
