package io.github.kingg22.vibrion.ui.screens.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.kingg22.vibrion.domain.model.ArtistInfo
import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.DownloadableSingle
import io.github.kingg22.vibrion.domain.model.ModelType
import io.github.kingg22.vibrion.ui.components.ErrorScreen
import io.github.kingg22.vibrion.ui.components.LoadingScreen
import io.github.kingg22.vibrion.ui.screens.download.DownloadViewModel
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreen(
    query: String,
    onBack: () -> Unit,
    onListDetailClick: (query: String, type: ModelType) -> Unit,
    onItemDetailClick: (type: ModelType, id: String) -> Unit,
    modifier: Modifier = Modifier,
    historyViewModel: SearchHistoryViewModel = koinViewModel(),
    searchViewModel: SearchViewModel = koinViewModel(),
    downloadViewModel: DownloadViewModel = koinViewModel(),
) {
    var query by rememberSaveable { mutableStateOf(query) }
    val history by historyViewModel.searchHistory.collectAsStateWithLifecycle()
    val state by searchViewModel.searchUiState.collectAsStateWithLifecycle()
    val canDownload by downloadViewModel.canDownloadState.collectAsStateWithLifecycle()
    val artists = searchViewModel.searchArtistsPagedResult.collectAsLazyPagingItems()

    LaunchedEffect(query) {
        searchViewModel.search(query)
        searchViewModel.searchArtist(query)
    }

    SearchAnimatedContent(
        state = state,
        canDownload = canDownload.isSuccess,
        artists = artists,
        onDownloadClick = downloadViewModel::download,
        onListDetailClick = { onListDetailClick(query, it) },
        onDetailClick = onItemDetailClick,
        modifier = modifier,
    ) {
        SearchTopBar(
            query = query,
            history = history,
            onSaveHistory = historyViewModel::saveQuery,
            onClearHistoryClick = historyViewModel::clearHistory,
            onSearch = { queryToSearch ->
                // Obtain the latest search query to navigate to the detail screen with the latest query and section
                query = queryToSearch
                searchViewModel.search(queryToSearch)
            },
            onBack = onBack,
        )
    }
}

@Composable
private fun SearchAnimatedContent(
    state: SearchViewModel.SearchUiState,
    canDownload: Boolean,
    artists: LazyPagingItems<ArtistInfo>,
    onDownloadClick: (item: DownloadableItem) -> Unit,
    onListDetailClick: (type: ModelType) -> Unit,
    onDetailClick: (type: ModelType, id: String) -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
) {
    Scaffold(modifier = modifier.fillMaxSize(), topBar = topBar) { paddingValues ->
        AnimatedContent(
            state,
            Modifier.padding(paddingValues),
            transitionSpec = { fadeIn(tween(2000)) togetherWith fadeOut(tween(2000)) },
            label = "Animated Search Content",
        ) { targetState ->
            when (targetState) {
                SearchViewModel.SearchUiState.Loading, SearchViewModel.SearchUiState.Idle -> {
                    LoadingScreen()
                }

                is SearchViewModel.SearchUiState.Error -> {
                    ErrorScreen(message = targetState.message)
                }

                is SearchViewModel.SearchUiState.Loaded -> {
                    SearchContent(
                        singles = targetState.singles,
                        playlists = targetState.playlists,
                        albums = targetState.albums,
                        artists = artists,
                        canDownload = canDownload,
                        onDownloadClick = onDownloadClick,
                        onSectionClick = onListDetailClick,
                        onDetailClick = onDetailClick,
                    )
                }
            }
        }
    }
}

@Composable
@Preview(group = "Search")
private fun SearchLoadingPreview() {
    val pagingData = PagingData.empty<ArtistInfo>(
        LoadStates(
            LoadState.NotLoading(true),
            LoadState.NotLoading(true),
            LoadState.NotLoading(true),
        ),
    )
    val fakeFlow = MutableStateFlow(pagingData)
    val listItems = fakeFlow.collectAsLazyPagingItems()

    VibrionAppTheme {
        SearchAnimatedContent(
            state = SearchViewModel.SearchUiState.Loading,
            canDownload = true,
            onDownloadClick = {},
            onListDetailClick = {},
            onDetailClick = { _, _ -> },
            artists = listItems,
        ) {
            SearchTopBar(
                query = "Test",
                history = emptyList(),
                onSaveHistory = {},
                onClearHistoryClick = {},
                onSearch = { _ -> },
                onBack = {},
            )
        }
    }
}

@Composable
@Preview(group = "Search")
private fun SearchErrorPreview() {
    val pagingData = PagingData.empty<ArtistInfo>(
        LoadStates(
            LoadState.NotLoading(true),
            LoadState.NotLoading(true),
            LoadState.NotLoading(true),
        ),
    )
    val fakeFlow = MutableStateFlow(pagingData)
    val listItems = fakeFlow.collectAsLazyPagingItems()

    VibrionAppTheme {
        SearchAnimatedContent(
            state = SearchViewModel.SearchUiState.Error("Error"),
            artists = listItems,
            canDownload = true,
            onDownloadClick = {},
            onListDetailClick = {},
            onDetailClick = { _, _ -> },
        ) {
            SearchTopBar(
                query = "Test",
                history = emptyList(),
                onSaveHistory = {},
                onClearHistoryClick = {},
                onSearch = { _ -> },
                onBack = {},
            )
        }
    }
}

@Composable
@Preview(group = "Search")
private fun SearchLoadedPreview() {
    val pagingData = PagingData.empty<ArtistInfo>(
        LoadStates(
            LoadState.NotLoading(true),
            LoadState.NotLoading(true),
            LoadState.NotLoading(true),
        ),
    )
    val fakeFlow = MutableStateFlow(pagingData)
    val listItems = fakeFlow.collectAsLazyPagingItems()

    VibrionAppTheme {
        SearchAnimatedContent(
            state = SearchViewModel.SearchUiState.Loaded(
                query = "Test",
                singles = listOf(DownloadableSingle.previewDefault),
                playlists = listOf(DownloadableSingle.previewDefault),
                albums = listOf(DownloadableSingle.previewDefault),
            ),
            artists = listItems,
            canDownload = true,
            onDownloadClick = {},
            onListDetailClick = {},
            onDetailClick = { _, _ -> },
        ) {
            SearchTopBar(
                query = "Test",
                history = emptyList(),
                onSaveHistory = {},
                onClearHistoryClick = {},
                onSearch = { _ -> },
                onBack = {},
            )
        }
    }
}
