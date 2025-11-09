package io.github.kingg22.vibrion.ui.screens.download

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DownloadScreen(
    modifier: Modifier = Modifier,
    downloadStatsViewModel: DownloadStatsViewModel = koinViewModel(),
    bottomBar: @Composable (() -> Unit),
) {
    val state by downloadStatsViewModel.state.collectAsStateWithLifecycle()
    val downloads by downloadStatsViewModel.downloads.collectAsStateWithLifecycle()

    DownloadContent(
        state = state,
        downloads = downloads,
        onCancelAllClick = downloadStatsViewModel::cancelDownload,
        onResumeAllClick = downloadStatsViewModel::resumeDownload,
        onResumeClick = downloadStatsViewModel::resumeDownload,
        onCancelClick = downloadStatsViewModel::cancelDownload,
        modifier = modifier,
        bottomBar = bottomBar,
    )
}
