package io.github.kingg22.vibrion.ui.screens.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.ModelType
import io.github.kingg22.vibrion.domain.service.AudioPlayerService
import io.github.kingg22.vibrion.ui.components.ErrorScreen
import io.github.kingg22.vibrion.ui.components.LoadingScreen
import io.github.kingg22.vibrion.ui.getModelType
import io.github.kingg22.vibrion.ui.screens.download.DownloadViewModel
import io.sentry.Sentry
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MusicDetailScreen(
    type: ModelType,
    id: String,
    onBackClick: () -> Unit,
    onDetailClick: (type: ModelType, id: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = koinViewModel(),
    downloadViewModel: DownloadViewModel = koinViewModel(),
    playerService: AudioPlayerService = koinInject(),
) {
    val canDownload by downloadViewModel.canDownloadState.collectAsState()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(type, id) {
        viewModel.loadDetail(type, id)
    }

    AnimatedContent(state) { targetState ->
        when (targetState) {
            DetailViewModel.UiState.Error -> ErrorScreen(modifier)
            DetailViewModel.UiState.Loading -> LoadingScreen(modifier)
            is DetailViewModel.UiState.Success -> MusicDetailContent(
                detail = targetState.detail,
                onBackClick = onBackClick,
                canDownload = { canDownload == DownloadViewModel.CanDownloadState.Success },
                onDownloadClick = { downloadViewModel.download(it) },
                onTrackClick = { item -> onDetailClick(item.getModelType(), item.id) },
                onTrackPlayClick = { item ->
                    Sentry.configureScope { scope ->
                        scope.setTransaction("Play ${item.getModelType()}")
                        scope.setExtra("item", item.toString())
                    }
                    playerService.setTrack(item)
                    playerService.play()
                },
                onPlayClick = { item ->
                    Sentry.configureScope { scope ->
                        scope.setTransaction("Play ${item.getModelType()}")
                        scope.setExtra("item", item.toString())
                    }
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
    }
}
