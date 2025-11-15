package io.github.kingg22.vibrion.ui.screens.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.kingg22.vibrion.domain.model.ModelType
import io.github.kingg22.vibrion.domain.service.AudioPlayerService
import io.github.kingg22.vibrion.ui.components.ErrorScreen
import io.github.kingg22.vibrion.ui.components.LoadingScreen
import io.github.kingg22.vibrion.ui.screens.download.DownloadViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MusicDetailScreen(
    type: ModelType,
    id: String,
    onBackClick: () -> Unit,
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
                onTrackClick = { /* TODO */ },
                onTrackPlayClick = { item ->
                    playerService.setTrack(item)
                    playerService.play()
                },
                modifier = modifier,
            )
        }
    }
}
