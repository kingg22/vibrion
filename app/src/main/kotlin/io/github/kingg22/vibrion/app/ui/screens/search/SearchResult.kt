package io.github.kingg22.vibrion.app.ui.screens.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import kotlin.time.Duration.Companion.minutes

// Se encarga del scaffold y topbar
@Composable
fun SearchResult(
    query: String,
    state: SearchViewModel.SearchResultUiState,
    onBackClick: () -> Unit,
    onSearch: (String) -> Unit,
    onDownloadClick: (Single) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { ResultTopBar(query, onBackClick, onSearch) },
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
            ResultItem(item, { onDownloadClick(item) })
        }
    }
}

@PreviewScreenSizes
@Preview
@Composable
private fun ResultPreview() {
    val results = List(5) { Single("", "title", "description", "", "", "Today", 3.minutes) }
    VibrionAppTheme {
        SearchResult("query", SearchViewModel.SearchResultUiState.Success(results), {}, {}, {})
    }
}
