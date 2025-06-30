package io.github.kingg22.vibrion.app.di

import io.github.kingg22.deezerSdk.api.DeezerApiClient
import io.github.kingg22.deezerSdk.utils.HttpClientBuilder
import io.github.kingg22.vibrion.app.data.SearchRepositoryImpl
import io.github.kingg22.vibrion.app.data.SettingsRepositoryImpl
import io.github.kingg22.vibrion.app.data.datasource.DeezerApiDataSource
import io.github.kingg22.vibrion.app.data.datasource.PreferencesDataSource
import io.github.kingg22.vibrion.app.dataStore
import io.github.kingg22.vibrion.app.domain.repository.SearchRepository
import io.github.kingg22.vibrion.app.domain.repository.SettingsRepository
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    // Raw data-sources
    single { CIO.create() }
    factory { HttpClientBuilder(httpEngine = get()) }
    single { DeezerApiClient(get()) }
    single { androidContext().dataStore }
    // data-sources
    single { DeezerApiDataSource(get()) }
    single { PreferencesDataSource(get()) }
    // Repositories
    single<SearchRepository> { SearchRepositoryImpl(get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
}
