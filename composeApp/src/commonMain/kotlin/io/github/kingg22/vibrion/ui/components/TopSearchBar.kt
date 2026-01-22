package io.github.kingg22.vibrion.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.Icons
import io.github.kingg22.vibrion.Res
import io.github.kingg22.vibrion.clear_search
import io.github.kingg22.vibrion.close
import io.github.kingg22.vibrion.domain.model.SearchHistoryItem
import io.github.kingg22.vibrion.filled.Close
import io.github.kingg22.vibrion.filled.Search
import io.github.kingg22.vibrion.search
import io.github.kingg22.vibrion.search_placeholder
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme
import org.jetbrains.compose.resources.stringResource

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
                    expanded = it
                },
                placeholder = { Text(stringResource(Res.string.search_placeholder)) },
                leadingIcon = {
                    IconButton(
                        onClick = {
                            onSearch(text)
                            onSaveHistory(text)
                        },
                        enabled = text.isNotBlank(),
                    ) {
                        Icon(
                            Icons.Filled.Search,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = stringResource(Res.string.search),
                        )
                    }
                },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(onClick = {
                            text = ""
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = stringResource(Res.string.clear_search),
                            )
                        }
                    } else if (expanded) {
                        IconButton(onClick = {
                            expanded = false
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = stringResource(Res.string.close),
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
