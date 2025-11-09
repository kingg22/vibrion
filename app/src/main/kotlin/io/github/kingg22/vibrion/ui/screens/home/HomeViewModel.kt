package io.github.kingg22.vibrion.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import io.github.kingg22.vibrion.domain.usecase.trends.LoadAlbumsTrendsUseCase
import io.github.kingg22.vibrion.domain.usecase.trends.LoadArtistTrendsUseCase
import io.github.kingg22.vibrion.domain.usecase.trends.LoadGenreTrendsUseCase
import io.github.kingg22.vibrion.domain.usecase.trends.LoadPlaylistTrendsUseCase
import io.github.kingg22.vibrion.domain.usecase.trends.LoadTopTracksTrendsUseCase
import io.github.kingg22.vibrion.ui.asPagingSource

class HomeViewModel(
    loadArtistTrendsUseCase: LoadArtistTrendsUseCase,
    loadGenreTrendsUseCase: LoadGenreTrendsUseCase,
    loadTopTracksTrendsUseCase: LoadTopTracksTrendsUseCase,
    loadPlaylistTrendsUseCase: LoadPlaylistTrendsUseCase,
    loadAlbumsTrendsUseCase: LoadAlbumsTrendsUseCase,
) : ViewModel() {
    val genrePaged = Pager(
        config = PagingConfig(pageSize = 10, initialLoadSize = 10),
        pagingSourceFactory = { loadGenreTrendsUseCase().asPagingSource() },
    ).flow.cachedIn(viewModelScope)

    val topTracksPaged = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { loadTopTracksTrendsUseCase().asPagingSource() },
    ).flow.cachedIn(viewModelScope)

    val albumsPaged = Pager(
        config = PagingConfig(pageSize = 15, initialLoadSize = 20),
        pagingSourceFactory = { loadAlbumsTrendsUseCase().asPagingSource() },
    ).flow.cachedIn(viewModelScope)

    val playlistsPaged = Pager(
        config = PagingConfig(pageSize = 15, initialLoadSize = 20),
        pagingSourceFactory = { loadPlaylistTrendsUseCase().asPagingSource() },
    ).flow.cachedIn(viewModelScope)

    val artistsPaged = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { loadArtistTrendsUseCase().asPagingSource() },
    ).flow.cachedIn(viewModelScope)
}
