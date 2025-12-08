package io.github.kingg22.vibrion.ui.screens.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.kingg22.vibrion.R
import io.github.kingg22.vibrion.domain.model.SearchHistoryItem
import io.github.kingg22.vibrion.ui.components.SearchSuggestions

// TODO remove all suppression with kotlin 2.3.0
@Composable
fun SearchTopBar(
    query: String,
    history: List<SearchHistoryItem>,
    onSaveHistory: (query: String) -> Unit,
    onClearHistoryClick: () -> Unit,
    onSearch: (query: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by rememberSaveable { mutableStateOf(query) }
    var expanded by rememberSaveable { mutableStateOf(false) }

    DockedSearchBar(
        // Needs to add windows padding because going to put this in Scaffold, need to respect the status bars
        // See the difference between (Top)SearchBar and DockedSearchBar
        modifier = modifier.windowInsetsPadding(SearchBarDefaults.windowInsets).fillMaxWidth(),
        inputField = {
            SearchBarDefaults.InputField(
                query = text,
                onQueryChange = {
                    @Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
                    text = it
                },
                onSearch = {
                    @Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
                    expanded = false
                    @Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
                    text = it
                    onSearch(it.trim())
                    onSaveHistory(it.trim())
                },
                expanded = expanded,
                onExpandedChange = {
                    @Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
                    expanded = it
                },
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                leadingIcon = {
                    IconButton(onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            stringResource(R.string.back),
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
                modifier = Modifier.fillMaxWidth(),
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
            onSuggestionClick = { suggestion ->
                @Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
                expanded = false
                @Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
                text = suggestion
                onSearch(suggestion.trim())
                onSaveHistory(suggestion.trim())
            },
            onClearHistory = onClearHistoryClick,
        )
    }
}
