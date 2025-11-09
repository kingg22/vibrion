package io.github.kingg22.vibrion.ui.screens.search

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.touchlab.kermit.Logger
import dev.drewhamilton.poko.Poko
import io.github.kingg22.vibrion.domain.model.ArtistInfo
import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.ModelType
import io.github.kingg22.vibrion.domain.usecase.search.SearchAlbumUseCase
import io.github.kingg22.vibrion.domain.usecase.search.SearchArtistUseCase
import io.github.kingg22.vibrion.domain.usecase.search.SearchPlaylistUseCase
import io.github.kingg22.vibrion.domain.usecase.search.SearchSingleUseCase
import io.github.kingg22.vibrion.ui.asPagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchSingleUseCase: SearchSingleUseCase,
    private val searchPlaylistUseCase: SearchPlaylistUseCase,
    private val searchAlbumUseCase: SearchAlbumUseCase,
    private val searchArtistUseCase: SearchArtistUseCase,
) : ViewModel() {
    companion object {
        private val logger = Logger.withTag("SearchViewModel")
    }
    private val _searchUiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val searchUiState = _searchUiState.asStateFlow()
    private var searchDetailQuery = "" to ""
    private val searchDetailPagedResult =
        MutableStateFlow<Flow<PagingData<DownloadableItem>>>(emptyFlow())

    private val _searchArtistsPagedResult =
        MutableStateFlow<Flow<PagingData<ArtistInfo>>>(emptyFlow())

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchPagedResult = searchDetailPagedResult.asStateFlow().flattenConcat()

    private var searchArtistQuery = ""

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchArtistsPagedResult = _searchArtistsPagedResult.asStateFlow().flattenConcat()

    fun search(query: String, limit: Int = 5) {
        val snapshot = _searchUiState.value
        if (snapshot is SearchUiState.Loaded && snapshot.query == query && snapshot.singles.size >= limit) {
            logger.d { "Search for '$query' is already loaded" }
            return
        }

        _searchUiState.update { SearchUiState.Loading }
        viewModelScope.launch {
            try {
                logger.d { "Searching for '$query'" }
                val singles = searchSingleUseCase(query, limit)
                val playlists = searchPlaylistUseCase(query, limit)
                val albums = searchAlbumUseCase(query, limit)
                if (singles.isNotEmpty() || playlists.isNotEmpty() || albums.isNotEmpty()) {
                    logger.d {
                        "Found results for '$query', singles: ${singles.size}, playlists: ${playlists.size}, albums: ${albums.size}"
                    }
                    _searchUiState.update { SearchUiState.Loaded(query, singles, playlists, albums) }
                } else {
                    _searchUiState.update { SearchUiState.Error("No results found") }
                }
            } catch (e: Exception) {
                currentCoroutineContext().ensureActive()
                logger.e(e) { "Error while searching for '$query'" }
                _searchUiState.update { SearchUiState.Error(e.message ?: "Unknown error") }
            }
        }
    }

    fun searchDetail(query: String, section: ModelType) {
        val (oldQuery, oldSection) = searchDetailQuery
        if (oldQuery == query && oldSection == section.name) {
            logger.d { "Search for '$query' in section '$section' for detail screen is already loaded" }
            return
        }
        if (oldQuery == query) {
            logger.d { "Search for '$query' change the from $oldSection to $section, searching new results" }
        }

        searchDetailQuery = query to section.name
        val pagerFlow = Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                when (section) {
                    // TODO: Add support for other sections
                    ModelType.SINGLE -> searchSingleUseCase
                    ModelType.PLAYLIST -> searchPlaylistUseCase
                    ModelType.ALBUM -> searchAlbumUseCase
                    else -> {
                        logger.e { "Invalid section to search in detail paginated: $section" }
                        error("Invalid section to search in detail paginated: $section")
                    }
                }.getPagedSearch(query)
                    .asPagingSource()
            },
        ).flow.cachedIn(viewModelScope)
        searchDetailPagedResult.update { pagerFlow }
    }

    fun searchArtist(query: String) {
        if (searchArtistQuery == query) {
            logger.d { "Search for artist with q='$query' is already loaded" }
            return
        }
        logger.d { "Search for artist with q='$query' change, searching new results" }
        searchArtistQuery = query

        val pagerFlow = Pager(
            config = PagingConfig(pageSize = 7),
            pagingSourceFactory = { searchArtistUseCase.getPagedSearch(query).asPagingSource() },
        ).flow.cachedIn(viewModelScope)

        _searchArtistsPagedResult.update { pagerFlow }
    }

    sealed interface SearchUiState {
        object Idle : SearchUiState
        object Loading : SearchUiState

        @Stable
        @Immutable
        @Poko
        class Loaded(
            val query: String,
            val singles: List<DownloadableItem>,
            val playlists: List<DownloadableItem>,
            val albums: List<DownloadableItem>,
        ) : SearchUiState

        @Stable
        @Immutable
        @Poko
        class Error(val message: String) : SearchUiState
    }
}
