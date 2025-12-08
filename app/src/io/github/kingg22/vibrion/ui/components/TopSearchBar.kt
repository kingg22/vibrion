package io.github.kingg22.vibrion.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.R
import io.github.kingg22.vibrion.domain.model.SearchHistoryItem
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme

// false positive ASSIGNED_VALUE_IS_NEVER_READ remove in 2.3.0 if is fixed https://youtrack.jetbrains.com/issue/KT-78664
@Composable
fun TopSearchBar(
    bottomBarPadding: Dp,
    history: List<SearchHistoryItem>,
    onSearch: (String) -> Unit,
    onSaveHistory: (String) -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    DisposableEffect(true) {
        onDispose {
            text = ""
            expanded = false
        }
    }

    // Top Bar (Search)
    SearchBar(
        modifier = modifier.fillMaxWidth(),
        inputField = {
            SearchBarDefaults.InputField(
                query = text,
                onQueryChange = {
                    @Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
                    text = it
                },
                onSearch = {
                    if (it.isNotBlank()) {
                        onSearch(it)
                        onSaveHistory(it)
                    }
                },
                expanded = expanded,
                onExpandedChange = {
                    @Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
                    expanded = it
                },
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                leadingIcon = {
                    IconButton(
                        onClick = {
                            onSearch(text)
                            onSaveHistory(text)
                        },
                        enabled = text.isNotBlank(),
                    ) {
                        Icon(
                            Icons.Default.Search,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = stringResource(R.string.search),
                        )
                    }
                },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(onClick = {
                            @Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
                            text = ""
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = stringResource(R.string.clear_search),
                            )
                        }
                    } else if (expanded) {
                        IconButton(onClick = {
                            @Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
                            expanded = false
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = stringResource(R.string.close),
                            )
                        }
                    }
                },
            )
        },
        expanded = expanded,
        onExpandedChange = {
            @Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
            expanded = it
        },
    ) {
        SearchSuggestions(
            query = text,
            history = history,
            onSuggestionClick = {
                onSearch(it)
                onSaveHistory(it)
            },
            onClearHistory = onClearHistory,
            bottomBarPadding = bottomBarPadding,
        )
    }
}

@Composable
@Preview
private fun TopBarPreview() {
    VibrionAppTheme {
        TopSearchBar(
            history = listOf(SearchHistoryItem("Test")),
            onSearch = {},
            onSaveHistory = {},
            onClearHistory = {},
            bottomBarPadding = 0.dp,
        )
    }
}
