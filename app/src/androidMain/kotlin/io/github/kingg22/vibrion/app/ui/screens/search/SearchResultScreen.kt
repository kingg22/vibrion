package io.github.kingg22.vibrion.app.ui.screens.search

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.app.ui.theme.VibrionAppTheme
import io.github.kingg22.vibrion.app.R as Res

@Composable
fun SearchResultScreen(query: String, onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    var text by rememberSaveable { mutableStateOf(query) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val results: List<ResultItemData> = remember(text) {
        List(5) { ResultItemData("", "", "", "", "") }
    }
    val onSearch: (String) -> Unit = {
        // TODO aquí será la busqueda con un VM
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            DockedSearchBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .fillMaxWidth()
                    .padding(start = 6.dp, end = 6.dp),
                expanded = expanded,
                onExpandedChange = { expanded = it },
                inputField = {
                    SearchBarDefaults.InputField(
                        query = text,
                        onQueryChange = { text = it },
                        onSearch = {
                            expanded = false
                            text = it
                            onSearch(it)
                        },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        placeholder = { Text(stringResource(Res.string.search_placeholder)) },
                        leadingIcon = {
                            IconButton(onBackClick) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.back))
                            }
                        },
                        trailingIcon = {
                            IconButton({
                                if (text.isNotBlank()) {
                                    onSearch(text)
                                }
                            }) {
                                Icon(Icons.Default.Search, stringResource(Res.string.search))
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                content = {},
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 8.dp),
        ) {
            items(results) {
                ResultItem({}, {})
            }
        }
    }
}

@PreviewScreenSizes
@Preview
@Composable
private fun ResultPreview() {
    VibrionAppTheme {
        SearchResultScreen("query", {})
    }
}
