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
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val results by viewModel.searchResults.collectAsState()

    LaunchedEffect(query) {
        // TODO use string resources
        if (query.isBlank()) viewModel.modifyState(SearchViewModel.SearchResultUiState.Error("Query cannot be empty"))
        if (query.isNotBlank()) viewModel.search(query)
    }
    SearchResult(query, results, onBackClick, viewModel::search, viewModel::download, modifier)
}
