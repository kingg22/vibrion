package io.github.kingg22.vibrion.app.ui.screens.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel

// Se encarga del VM y estado
@Composable
fun SearchResultScreen(
    query: String,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val results by viewModel.searchResults.collectAsState()

    LaunchedEffect(query) {
        // TODO use string resources
        val query = query.trim()
        if (query.isBlank()) viewModel.modifyState(SearchViewModel.SearchResultUiState.Error("Query cannot be empty"))
        if (query.isNotBlank()) viewModel.search(query)
    }
    SearchResult(query, results, onBackClick, onSearch = viewModel::search, onDownloadClick = {
        if (viewModel.canDownload()) {
            viewModel.download(it)
        } else {
            onSettingsClick()
        }
    }, modifier)
}
