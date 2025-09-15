package io.github.kingg22.vibrion.app.ui.screens.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.app.R
import io.github.kingg22.vibrion.app.domain.model.Single
import io.github.kingg22.vibrion.app.ui.screens.components.ErrorScreen
import io.github.kingg22.vibrion.app.ui.screens.components.LoadingScreen
import io.github.kingg22.vibrion.app.ui.theme.VibrionAppTheme
import io.github.kingg22.vibrion.core.domain.model.Download
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

// Se encarga del scaffold y topbar
@Composable
fun SearchResult(
    query: String,
    state: SearchViewModel.SearchResultUiState,
    downloadState: SearchViewModel.DownloadResultUiState,
    onBackClick: () -> Unit,
    onSearch: (String) -> Unit,
    onDownloadClick: (Single) -> Unit,
    onDownloadCancel: (Download) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val onDownloadCancelLambda by rememberUpdatedState(onDownloadCancel)

    LaunchedEffect(downloadState) {
        when (downloadState) {
            SearchViewModel.DownloadResultUiState.Idle -> Unit
            SearchViewModel.DownloadResultUiState.Loading -> {
                scope.launch { snackbarHostState.showSnackbar("Descarga en preparación") }
            }
            is SearchViewModel.DownloadResultUiState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Ha ocurrido un error al descargar: ${downloadState.message}")
                }
            }
            is SearchViewModel.DownloadResultUiState.Success -> {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = "Se ha enqueued la descarga de ${downloadState.download.fileName}",
                        actionLabel = "Cancelar",
                        duration = SnackbarDuration.Short,
                    )
                    when (result) {
                        SnackbarResult.Dismissed -> Unit
                        SnackbarResult.ActionPerformed -> {
                            scope.launch {
                                onDownloadCancelLambda(downloadState.download)
                                snackbarHostState.showSnackbar("Descarga cancelada")
                            }
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { ResultTopBar(query, onBackClick, onSearch) },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        AnimatedContent(state, Modifier.padding(paddingValues)) { state ->
            when (state) {
                SearchViewModel.SearchResultUiState.Idle -> LoadingScreen()
                SearchViewModel.SearchResultUiState.Loading -> LoadingScreen()
                is SearchViewModel.SearchResultUiState.Error -> ErrorScreen()
                is SearchViewModel.SearchResultUiState.Success -> {
                    if (state.results.isEmpty()) {
                        ErrorScreen(message = stringResource(R.string.no_results))
                    } else {
                        ResultListItems(state.results, onDownloadClick)
                    }
                }
            }
        }
    }
}

// Se encarga de la lista
@Composable
fun ResultListItems(results: List<Single>, onDownloadClick: (Single) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.padding(top = 8.dp)) {
        items(results) { item ->
            ResultItem(item, onDownloadClick = { onDownloadClick(item) })
        }
    }
}

@PreviewScreenSizes
@Preview
@Composable
private fun ResultPreview() {
    val results = List(5) { Single("", "title", "description", "", "", "Today", 3.minutes) }
    VibrionAppTheme {
        SearchResult(
            query = "query",
            state = SearchViewModel.SearchResultUiState.Success(results),
            downloadState = SearchViewModel.DownloadResultUiState.Idle,
            onBackClick = {},
            onSearch = {},
            onDownloadClick = {},
            onDownloadCancel = {},
        )
    }
}
