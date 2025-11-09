package io.github.kingg22.vibrion.di

import io.github.kingg22.vibrion.domain.usecase.search.ClearSearchHistoryUseCase
import io.github.kingg22.vibrion.domain.usecase.search.GetSearchHistoryUseCase
import io.github.kingg22.vibrion.domain.usecase.search.LoadDetailUseCase
import io.github.kingg22.vibrion.domain.usecase.search.SaveSearchQueryUseCase
import io.github.kingg22.vibrion.domain.usecase.search.SearchAlbumUseCase
import io.github.kingg22.vibrion.domain.usecase.search.SearchArtistUseCase
import io.github.kingg22.vibrion.domain.usecase.search.SearchPlaylistUseCase
import io.github.kingg22.vibrion.domain.usecase.search.SearchSingleUseCase
import io.github.kingg22.vibrion.domain.usecase.settings.GetSettingsUseCase
import io.github.kingg22.vibrion.domain.usecase.settings.LoadTokenUseCase
import io.github.kingg22.vibrion.domain.usecase.settings.UpdateSettingsUseCase
import io.github.kingg22.vibrion.domain.usecase.trends.LoadAlbumsTrendsUseCase
import io.github.kingg22.vibrion.domain.usecase.trends.LoadArtistTrendsUseCase
import io.github.kingg22.vibrion.domain.usecase.trends.LoadGenreTrendsUseCase
import io.github.kingg22.vibrion.domain.usecase.trends.LoadPlaylistTrendsUseCase
import io.github.kingg22.vibrion.domain.usecase.trends.LoadTopTracksTrendsUseCase
import org.koin.dsl.module

val domainModule = module {
    // Search History UseCases
    factory { GetSearchHistoryUseCase(get()) }
    factory { SaveSearchQueryUseCase(get()) }
    factory { ClearSearchHistoryUseCase(get()) }
    factory { LoadDetailUseCase(get()) }

    // Search UseCases
    factory { SearchArtistUseCase(get()) }
    factory { SearchSingleUseCase(get()) }
    factory { SearchPlaylistUseCase(get()) }
    factory { SearchAlbumUseCase(get()) }

    // Settings UseCases
    factory { GetSettingsUseCase(get()) }
    factory { UpdateSettingsUseCase(get()) }
    factory { LoadTokenUseCase(get()) }

    // Trends UseCases
    factory { LoadArtistTrendsUseCase(get()) }
    factory { LoadGenreTrendsUseCase(get()) }
    factory { LoadPlaylistTrendsUseCase(get()) }
    factory { LoadTopTracksTrendsUseCase(get()) }
    factory { LoadAlbumsTrendsUseCase(get()) }
}
