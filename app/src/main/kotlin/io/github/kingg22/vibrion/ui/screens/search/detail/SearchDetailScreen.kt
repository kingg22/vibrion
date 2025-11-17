package io.github.kingg22.vibrion.ui.screens.search.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.ModelType
import io.github.kingg22.vibrion.domain.service.AudioPlayerService
import io.github.kingg22.vibrion.ui.getModelType
import io.github.kingg22.vibrion.ui.screens.download.DownloadViewModel
import io.github.kingg22.vibrion.ui.screens.search.SearchViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchDetailScreen(
    query: String,
    modelType: ModelType,
    onBackClick: () -> Unit,
    onDetailClick: (type: ModelType, id: String) -> Unit,
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = koinViewModel(),
    downloadViewModel: DownloadViewModel = koinViewModel(),
    playerService: AudioPlayerService = koinInject(),
) {
    LaunchedEffect(query, modelType) {
        searchViewModel.searchDetail(query, modelType)
    }
    val listResult = searchViewModel.searchPagedResult.collectAsLazyPagingItems()
    val canDownload by downloadViewModel.canDownloadState.collectAsStateWithLifecycle()

    SearchDetailContent(
        modelType = modelType,
        listResult = listResult,
        onBackClick = onBackClick,
        onDownloadClick = downloadViewModel::download,
        canDownload = canDownload == DownloadViewModel.CanDownloadState.Success,
        onItemClick = { item -> onDetailClick(item.getModelType(), item.id) },
        onPlayClick = { item ->
            if (item is DownloadableItem.StreamableItem) {
                playerService.setTrack(item)
                playerService.play()
            } else if (item is DownloadableItem.ItemWithTracks) {
                @Suppress("UNCHECKED_CAST")
                playerService.setQueue(
                    item.tracks.filterIsInstance<DownloadableItem.StreamableItem>() as List<DownloadableItem>,
                )
            }
        },
        modifier = modifier,
    )
}
