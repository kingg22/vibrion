package io.github.kingg22.vibrion.ui.screens.search.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.kingg22.vibrion.domain.model.ModelType
import io.github.kingg22.vibrion.ui.screens.download.DownloadViewModel
import io.github.kingg22.vibrion.ui.screens.search.SearchViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchDetailScreen(
    query: String,
    title: ModelType,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = koinViewModel(),
    downloadViewModel: DownloadViewModel = koinViewModel(),
) {
    LaunchedEffect(query, title) {
        searchViewModel.searchDetail(query, title)
    }
    val listResult = searchViewModel.searchPagedResult.collectAsLazyPagingItems()
    val canDownload by downloadViewModel.canDownloadState.collectAsStateWithLifecycle()

    SearchDetailContent(
        title = title.name,
        listResult = listResult,
        onBackClick = onBackClick,
        onDownloadClick = downloadViewModel::download,
        canDownload = canDownload == DownloadViewModel.CanDownloadState.Success,
        onPlayClick = { /* TODO */ },
        modifier = modifier,
    )
}
