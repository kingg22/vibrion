package io.github.kingg22.vibrion.app.ui.screens.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.app.ui.theme.VibrionAppTheme
import org.koin.compose.viewmodel.koinViewModel

// Se encarga del VM
@Composable
fun SearchResultScreen(
    query: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val results by viewModel.searchResults.collectAsState()
    SearchResult(query, results, onBackClick, viewModel::search, modifier)
}

// Se encarga del scaffold y topbar
@Composable
fun SearchResult(
    query: String,
    results: List<ResultItemData>,
    onBackClick: () -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { ResultTopBar(query, onBackClick, onSearch) },
    ) { paddingValues ->
        ResultListItems(results, modifier = Modifier.padding(paddingValues))
    }
}

// Se encarga de la lista
@Composable
fun ResultListItems(results: List<ResultItemData>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.padding(top = 8.dp)) {
        items(results) {
            ResultItem({}, {})
        }
    }
}

@PreviewScreenSizes
@Preview
@Composable
private fun ResultPreview() {
    val results = List(5) { ResultItemData("", "", "", "", "") }
    VibrionAppTheme {
        SearchResult("query", results, {}, {})
    }
}
