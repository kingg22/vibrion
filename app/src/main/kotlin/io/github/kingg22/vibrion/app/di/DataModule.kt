package io.github.kingg22.vibrion.app.di

import io.github.kingg22.deezerSdk.api.DeezerApiClient
import io.github.kingg22.vibrion.app.data.DeezerApiDataSource
import io.github.kingg22.vibrion.app.data.SearchRepositoryImpl
import io.github.kingg22.vibrion.app.domain.repository.SearchRepository
import org.koin.dsl.module

val dataModule = module {
    single { DeezerApiClient() }
    single { DeezerApiDataSource(get()) }
    single<SearchRepository> { SearchRepositoryImpl(get()) }
}
