package io.github.kingg22.vibrion.app.di

import io.github.kingg22.deezerSdk.api.DeezerApiClient
import io.github.kingg22.deezerSdk.utils.HttpClientBuilder
import io.github.kingg22.vibrion.app.data.DeezerApiDataSource
import io.github.kingg22.vibrion.app.data.SearchRepositoryImpl
import io.github.kingg22.vibrion.app.domain.repository.SearchRepository
import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module

val dataModule = module {
    single { DeezerApiClient(HttpClientBuilder(httpEngine = CIO.create())) }
    single { DeezerApiDataSource(get()) }
    single<SearchRepository> { SearchRepositoryImpl(get()) }
}
