package io.github.kingg22.vibrion.di

import io.github.kingg22.deezer.client.api.DeezerApiClient
import io.github.kingg22.deezer.client.utils.HttpClientBuilder
import io.github.kingg22.vibrion.data.PlatformHelper
import io.github.kingg22.vibrion.data.datasource.music.DeezerApiDataSource
import io.github.kingg22.vibrion.data.datasource.music.DeezerGwDataSource
import io.github.kingg22.vibrion.data.datasource.preferences.PreferencesDataSource
import io.github.kingg22.vibrion.data.repository.SearchHistoryRepositoryImpl
import io.github.kingg22.vibrion.data.repository.SearchRepositoryImpl
import io.github.kingg22.vibrion.data.repository.SettingsRepositoryImpl
import io.github.kingg22.vibrion.data.repository.TrendsRepositoryImpl
import io.github.kingg22.vibrion.data.service.DownloadServiceImpl
import io.github.kingg22.vibrion.domain.repository.SearchHistoryRepository
import io.github.kingg22.vibrion.domain.repository.SearchRepository
import io.github.kingg22.vibrion.domain.repository.SettingsRepository
import io.github.kingg22.vibrion.domain.repository.TrendsRepository
import io.github.kingg22.vibrion.domain.service.DownloadService
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import co.touchlab.kermit.Logger as KermitLogger
import io.ktor.client.plugins.logging.Logger as KtorLogger

/** All data-related modules */
val dataModule = module {
    // Raw data-sources
    factory { CIO.create() }
    factory {
        HttpClientBuilder()
            .httpEngine(get())
            .logger(
                object : KtorLogger {
                    override fun log(message: String) {
                        // verbose level
                        KermitLogger.v("HttpClient") { message }
                    }
                },
            )
    }
    factory { DeezerApiClient(get()) }

    // DataSources
    factory { DeezerApiDataSource(get()) }
    single { DeezerGwDataSource(get(), get(), get()) }
    factory { PreferencesDataSource(get()) }

    // Services
    factory<DownloadService> { DownloadServiceImpl(get(), get()) }
    factory { PlatformHelper(androidContext()) }

    // Repositories
    factory<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get()) }
    factory<SettingsRepository> { SettingsRepositoryImpl(get(), get()) }
    factory<SearchRepository> { SearchRepositoryImpl(get()) }
    factory<TrendsRepository> { TrendsRepositoryImpl(get()) }
}
