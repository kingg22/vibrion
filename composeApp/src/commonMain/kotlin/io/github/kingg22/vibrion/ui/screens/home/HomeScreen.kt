package io.github.kingg22.vibrion.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.ModelType
import io.github.kingg22.vibrion.domain.service.AudioPlayerService
import io.github.kingg22.vibrion.ui.components.TopSearchBar
import io.github.kingg22.vibrion.ui.screens.search.SearchHistoryViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    onSearch: (query: String) -> Unit,
    onDetailClick: (type: ModelType, id: String) -> Unit,
    modifier: Modifier = Modifier,
    historyViewModel: SearchHistoryViewModel = koinViewModel(),
    homeViewModel: HomeViewModel = koinViewModel(),
    audioService: AudioPlayerService = koinInject(),
    bottomBar: @Composable (() -> Unit),
) {
    var bottomBarPadding by remember { mutableStateOf(0.dp) }
    val history by historyViewModel.searchHistory.collectAsStateWithLifecycle()
    val artistList = homeViewModel.artistsPaged.collectAsLazyPagingItems()
    val genresList = homeViewModel.genrePaged.collectAsLazyPagingItems()
    val carouselItems = homeViewModel.topTracksPaged.collectAsLazyPagingItems()
    val playlistsList = homeViewModel.playlistsPaged.collectAsLazyPagingItems()

    HomeContent(
        genresList = genresList,
        carouselItems = carouselItems,
        artistList = artistList,
        playlistsList = playlistsList,
        modifier = modifier,
        bottomBar = bottomBar,
        bottomBarPadding = {
            // false positive, remove with 2.3.0 if is fixed
            @Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
            bottomBarPadding = it
        },
        onDetailClick = onDetailClick,
        onPlayTrackClick = { item ->
            /*
            val sentryTransaction = Sentry.startTransaction("Play a ${item.getModelType()}", "play")
            sentryTransaction.setData("item", item.toString())
             */
            try {
                if (item is DownloadableItem.StreamableItem) {
                    audioService.setTrack(item)
                    audioService.play()
                } else if (item is DownloadableItem.ItemWithTracks) {
                    @Suppress("UNCHECKED_CAST")
                    audioService.setQueue(
                        item.tracks.filterIsInstance<DownloadableItem.StreamableItem>() as List<DownloadableItem>,
                    )
                }
            } catch (_: Throwable) {
                /*
                sentryTransaction.throwable = e
                sentryTransaction.status = SpanStatus.INTERNAL_ERROR
                 */
            } finally {
                // sentryTransaction.finish()
            }
        },
    ) {
        TopSearchBar(
            bottomBarPadding = bottomBarPadding,
            history = history,
            onSearch = onSearch,
            onSaveHistory = historyViewModel::saveQuery,
            onClearHistory = historyViewModel::clearHistory,
        )
    }
}
