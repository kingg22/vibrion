package io.github.kingg22.vibrion.ui.screens.search

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import co.touchlab.kermit.Logger
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
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
    val searchUiState: StateFlow<SearchUiState>
        field = MutableStateFlow<SearchUiState>(SearchUiState.Idle)

    private val searchDetailParams = MutableStateFlow<SearchDetailParams?>(null)

    // --- Estado para búsqueda de artistas ---
    private val searchArtistQuery = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchPagedResult = searchDetailParams
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { params ->
            logger.d { "Building pager for section=${params.section}, query='${params.query}'" }
            val pagingSourceFactory = when (params.section) {
                ModelType.SINGLE -> searchSingleUseCase
                ModelType.PLAYLIST -> searchPlaylistUseCase
                ModelType.ALBUM -> searchAlbumUseCase
                else -> error("Invalid section for paged search: ${params.section}")
            }.getPagedSearch(params.query).asPagingSource()

            Pager(
                config = PagingConfig(pageSize = 10),
                pagingSourceFactory = { pagingSourceFactory },
            ).flow
        }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchArtistsPagedResult = searchArtistQuery
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { query ->
            logger.d { "Building artist pager for query='$query'" }
            Pager(
                config = PagingConfig(pageSize = 7),
                pagingSourceFactory = {
                    searchArtistUseCase.getPagedSearch(query).asPagingSource()
                },
            ).flow
        }
        .cachedIn(viewModelScope)

    fun search(query: String, limit: Int = 5) {
        val snapshot = searchUiState.value
        if (snapshot is SearchUiState.Loaded && snapshot.query == query && snapshot.singles.size >= limit) {
            logger.d { "Search for '$query' is already loaded" }
            return
        }

        searchUiState.update { SearchUiState.Loading }

        viewModelScope.launch {
            try {
                logger.d { "Searching for '$query'" }
                val singles = searchSingleUseCase(query, limit)
                val playlists = searchPlaylistUseCase(query, limit)
                val albums = searchAlbumUseCase(query, limit)

                if (singles.isNotEmpty() || playlists.isNotEmpty() || albums.isNotEmpty()) {
                    searchUiState.update {
                        SearchUiState.Loaded(query, singles, playlists, albums)
                    }
                } else {
                    searchUiState.update { SearchUiState.Error("No results found") }
                }
            } catch (e: Exception) {
                currentCoroutineContext().ensureActive()
                logger.e(e) { "Error while searching for '$query'" }
                searchUiState.update { SearchUiState.Error(e.message ?: "Unknown error") }
            }
        }
    }

    fun searchDetail(query: String, section: ModelType) {
        val current = searchDetailParams.value
        if (current?.query == query && current.section == section) {
            logger.d { "SearchDetail for '$query' and section '$section' already active" }
            return
        }

        logger.d { "Updating SearchDetail params to query='$query', section='$section'" }
        searchDetailParams.update { SearchDetailParams(query, section) }
    }

    fun searchArtist(query: String) {
        if (searchArtistQuery.value == query) {
            logger.d { "Artist search for '$query' already active" }
            return
        }

        logger.d { "Updating artist search query='$query'" }
        searchArtistQuery.value = query
    }

    // --- Estado para búsqueda de detalle ---
    private data class SearchDetailParams(val query: String, val section: ModelType)

    sealed interface SearchUiState {
        data object Idle : SearchUiState
        data object Loading : SearchUiState

        @Stable
        @Immutable
        data class Loaded(
            val query: String,
            val singles: List<DownloadableItem>,
            val playlists: List<DownloadableItem>,
            val albums: List<DownloadableItem>,
        ) : SearchUiState

        @Stable
        @Immutable
        data class Error(val message: String) : SearchUiState
    }
}
